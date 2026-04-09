package disp.moveis.luisfelipe.pratica1;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    public void clickCalculoImc(View view){
        EditText nome = (EditText) findViewById(R.id.campoNome);
        EditText idade = (EditText) findViewById(R.id.campoIdade);
        EditText peso = (EditText) findViewById(R.id.campoPeso);
        EditText altura = (EditText) findViewById(R.id.campoAltura);

        String nomeStr = nome.getText().toString();
        String idadeStr = idade.getText().toString();
        String pesoStr = peso.getText().toString();
        String alturaStr = altura.getText().toString();


        if(nomeStr.isEmpty() || idadeStr.isEmpty() || pesoStr.isEmpty() || alturaStr.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        double pesoDouble = Double.parseDouble(pesoStr);
        double alturaDouble = Double.parseDouble(alturaStr);

        double imc = pesoDouble / (alturaDouble*alturaDouble);
        String classificacao = "";
        if(imc < 18.5){
            classificacao = "Abaixo do peso";
        }
        else if(imc >= 18.5 && imc <=24.9){
            classificacao = "Saudável";
        }
        else if(imc >= 25 && imc <= 29.9){
            classificacao = "Sobrepeso";
        }
        else if(imc >= 30 && imc <= 34.9){
            classificacao = "Obesidade grau I";
        }
        else if(imc >= 35 && imc <= 39.9){
            classificacao = "Obesidade grau II (severa)";
        }
        else if(imc >= 40){
            classificacao = "Obesidade grau III (mórbida)";
        }

        Intent intent = new Intent(getBaseContext(), tela2.class);

        intent.putExtra("nome", nomeStr);
        intent.putExtra("idade", idadeStr);
        intent.putExtra("peso", pesoDouble);
        intent.putExtra("altura", alturaDouble);
        intent.putExtra("imc", imc);
        intent.putExtra("classificacao", classificacao);

        startActivity(intent);

    }

    private String getClassName(){
        String s = getClass().getName();
        return s;
    }

    protected void onStart(){
        super.onStart();
        Log.i("Ciclo de Vida", getClassName() + ".onStart() chamado.");
    }

    protected void onRestart(){
        super.onRestart();
        Log.i("Ciclo de Vida", getClassName() + ".onRestart() chamado.");
    }
    protected void onResume(){
        super.onResume();
        Log.i("Ciclo de Vida", getClassName() + ".onResume() chamado.");
    }

    protected void onPause(){
        super.onPause();
        Log.i("Ciclo de Vida", getClassName() + ".onPause() chamado.");
    }
    protected void onStop(){
        super.onStop();
        Log.i("Ciclo de Vida", getClassName() + ".onStop() chamado.");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Ciclo de Vida", getClassName() + ".onDestroy() chamado.");
    }

}