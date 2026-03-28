package disp.moveis.luisfelipe.pratica1;

import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
    }

    public void clickButtonOperation(View view){
        EditText input1 = (EditText) findViewById(R.id.input1);
        EditText input2 = (EditText) findViewById(R.id.input2);

        TextView output = (TextView) findViewById(R.id.output);

        String tag = view.getTag().toString();

        double valor1 = Double.parseDouble(input1.getText().toString().replace(",","."));
        double valor2 = Double.parseDouble(input2.getText().toString());
        double valorFinal = 0.0;
        if(tag.equals("soma")){
            valorFinal = valor1 + valor2;

        }
        else if(tag.equals("subtracao")){
            valorFinal = valor1 - valor2;
        }
        else if(tag.equals("multiplicacao")){
            valorFinal = valor1*valor2;
        }
        else if(tag.equals("divisao")){
            valorFinal = valor1/valor2;
        }
        output.setText(String.valueOf(valorFinal));
    }
}