package disp.moveis.luisfelipe.pratica1appb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private float lux;
    private float proximidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupera os valores enviados pelo App A
        lux = getIntent().getFloatExtra("lux", 0f);
        proximidade = getIntent().getFloatExtra("proximidade", 0f);

        // Exibe os valores na tela
        TextView textoLux = findViewById(R.id.textoLux);
        TextView textoProximidade = findViewById(R.id.textoProximidade);

        textoLux.setText("Luminosidade: " + lux + " lx");
        textoProximidade.setText("Proximidade: " + proximidade + " cm");
    }

    public void devolverClassificacoes(View v) {
        Intent resultado = new Intent();

        // < 20 lx → ligar lanterna
        resultado.putExtra("ligarLanterna", lux < 20.0f);

        // > 3 cm → ligar vibração
        resultado.putExtra("ligarVibracao", proximidade > 3.0f);

        setResult(RESULT_OK, resultado);
        finish(); // fecha o App B e volta para o App A
    }
}
