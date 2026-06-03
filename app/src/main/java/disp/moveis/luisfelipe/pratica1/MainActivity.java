package disp.moveis.luisfelipe.pratica1;

import android.content.Intent;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorL;
    private Sensor sensorP;

    private float ultimoLux;
    private float ultimaProximidade;

    private MaterialSwitch switchLanterna;
    private MaterialSwitch switchViber;

    private LanternaHelper lanterna;
    private MotorHelper motor;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        switchLanterna = findViewById(R.id.lanternaSwitcher);
        switchViber = findViewById(R.id.viberSwitcher);

        sensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorL =
                sensorManager.getDefaultSensor(
                        Sensor.TYPE_LIGHT);

        sensorP =
                sensorManager.getDefaultSensor(
                        Sensor.TYPE_PROXIMITY);

        lanterna = new LanternaHelper(this);
        motor = new MotorHelper(this);

        launcher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            if(result.getResultCode() == RESULT_OK &&
                                    result.getData() != null){

                                Intent data = result.getData();

                                boolean ligarLanterna =
                                        data.getBooleanExtra(
                                                "ligarLanterna",
                                                false);

                                boolean ligarVibracao =
                                        data.getBooleanExtra(
                                                "ligarVibracao",
                                                false);

                                if(ligarLanterna){
                                    lanterna.ligar();
                                    switchLanterna.setChecked(true);
                                }else{
                                    lanterna.desligar();
                                    switchLanterna.setChecked(false);
                                }

                                if(ligarVibracao){
                                    motor.iniciarVibracao();
                                    switchViber.setChecked(true);
                                }else{
                                    motor.pararVibracao();
                                    switchViber.setChecked(false);
                                }
                            }
                        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                this,
                sensorL,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(
                this,
                sensorP,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        lanterna.desligar();
        motor.pararVibracao();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()
                == Sensor.TYPE_LIGHT){

            ultimoLux = event.values[0];
        }

        if(event.sensor.getType()
                == Sensor.TYPE_PROXIMITY){

            ultimaProximidade = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(
            Sensor sensor,
            int accuracy) {
    }

    public void classificar_leituras(View v){

        Intent intent =
                new Intent(
                        "disp.moveis.luisfelipe.CLASSIFICAR_LEITURAS");

        intent.putExtra(
                "lux",
                ultimoLux);

        intent.putExtra(
                "proximidade",
                ultimaProximidade);

        launcher.launch(intent);
    }
}