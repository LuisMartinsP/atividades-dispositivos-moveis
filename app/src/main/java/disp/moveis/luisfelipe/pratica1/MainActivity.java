package disp.moveis.luisfelipe.pratica1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private boolean ultimoFoiEquals = false;

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
    public void printButton(View view){
        EditText _visor = (EditText) findViewById(R.id.visor);
        String tag = view.getTag().toString();

        if (tag.equals("+") || tag.equals("-") || tag.equals("X") || tag.equals("/")) {
            ultimoFoiEquals = false;
            _visor.append(" " + tag + " ");
        } else {
            if (ultimoFoiEquals) {
                // usuário digitou número após equals, descarta resultado
                _visor.setText("");
                ultimoFoiEquals = false;
            }
            _visor.append(tag);
        }
    }
    @SuppressLint("SetTextI18n")
    public void backspaceButton(View view){
        EditText _visor = (EditText) findViewById(R.id.visor);
        String texto = _visor.getText().toString();

        if (!texto.isEmpty()) {
            if (texto.endsWith(" ")) {
                _visor.setText(texto.substring(0, texto.length() - 3));
            } else {
                _visor.setText(texto.substring(0, texto.length() - 1));
            }
        }
    }

    public void clearButton(View view){
        EditText _visor = (EditText) findViewById(R.id.visor);
        _visor.setText("");
    }

    public void equalsButton(View view){
        EditText _visor = (EditText) findViewById(R.id.visor);
        String texto = _visor.getText().toString().trim();
        String[] partes = texto.split(" ");
        ultimoFoiEquals = true;

        if (partes.length != 3) {
            _visor.setText("Erro");
            return;
        }
        try {
            double num1 = Double.parseDouble(partes[0]);
            String operador = partes[1];
            double num2 = Double.parseDouble(partes[2]);

            double resultado = 0;

            switch (operador) {
                case "+":
                    resultado = num1 + num2;
                    break;
                case "-":
                    resultado = num1 - num2;
                    break;
                case "X":
                    resultado = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0) {
                        _visor.setText("Erro: divisão por zero");
                        return;
                    }
                    resultado = num1 / num2;
                    break;
                default:
                    _visor.setText("Erro");
                    return;
            }
            _visor.setText(String.valueOf(resultado));
        }catch(NumberFormatException e){
            _visor.setText("Erro:número inválido");
        }
    }
}