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

import java.util.Locale;

public class tela2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tela2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tela2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        TextView nome = findViewById(R.id.nameField);
        TextView idade = findViewById(R.id.idadeField);
        TextView peso = findViewById(R.id.pesoField);
        TextView altura = findViewById(R.id.alturaField);
        TextView imc = findViewById(R.id.imcField);
        TextView classificacao = findViewById(R.id.classField);

        Intent intent = getIntent();
        String nomeStr = intent.getStringExtra("nome");
        String idadeStr = intent.getStringExtra("idade");
        double pesoVal = intent.getDoubleExtra("peso", 0);
        double alturaVal = intent.getDoubleExtra("altura", 0);
        double imcVal = intent.getDoubleExtra("imc", 0);
        String classStr = intent.getStringExtra("classificacao");

        nome.setText(nomeStr);
        idade.setText(idadeStr);
        peso.setText(String.valueOf(pesoVal));
        altura.setText(String.valueOf(alturaVal));
        imc.setText(String.format(Locale.getDefault(), "%.2f", imcVal));
        classificacao.setText(classStr);
    }

    public void novoCalculo(View view){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
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