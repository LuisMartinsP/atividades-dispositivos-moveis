package disp.moveis.luisfelipe.pratica1;

import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorL;
    private Sensor sensorP;
    private MaterialSwitch switchLanterna;
    private MaterialSwitch switchViber;
    private MotorHelper motor;
    private LanternaHelper lanterna;

    private float ultimoLux = 0;
    private float ultimaProximidade = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorL = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorP = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        switchLanterna = findViewById(R.id.lanternaSwitcher);
        switchViber = findViewById(R.id.viberSwitcher);
        lanterna = new LanternaHelper(this);
        motor = new MotorHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                this,
                sensorL,
                SensorManager.SENSOR_DELAY_NORMAL
        );

        sensorManager.registerListener(
                this,
                sensorP,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        lanterna.desligar();
        motor.pararVibracao();
    }
    @Override
    protected void onDestroy() {
        lanterna.desligar();
        motor.pararVibracao();
        super.onDestroy();
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            ultimoLux = sensorEvent.values[0];
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            ultimaProximidade = sensorEvent.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void classificar_leituras(View v){

        if (ultimoLux < 5) {
            switchLanterna.setChecked(true);
            lanterna.ligar();
        } else {
            switchLanterna.setChecked(false);
            lanterna.desligar();
        }

        if (ultimaProximidade < (sensorP.getMaximumRange())) {
            switchViber.setChecked(true);
            motor.iniciarVibracao();
        } else {
            switchViber.setChecked(false);
            motor.pararVibracao();
        }
    }
}