package disp.moveis.luisfelipe.pratica1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GestaoCheckIn extends AppCompatActivity {
    DatabaseSingleton banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestao_check_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        banco = DatabaseSingleton.getInstance(this);
        carregarCheckins();
    }

    private void carregarCheckins() {
        LinearLayout colunaLocais = findViewById(R.id.colunaLocais);
        LinearLayout colunaBotoes = findViewById(R.id.colunaBotoes);

        colunaLocais.removeAllViews();
        colunaBotoes.removeAllViews();

        Cursor c = banco.buscar(
                "Checkin",
                new String[]{"Local"},
                "",
                "Local"
        );

        while (c.moveToNext()) {
            String local = c.getString(c.getColumnIndexOrThrow("Local"));

            TextView textLocal = (TextView) LayoutInflater.from(this)
                    .inflate(R.layout.item_local, colunaLocais, false);
            textLocal.setText(local);

            ImageButton btnExcluir = (ImageButton) LayoutInflater.from(this)
                    .inflate(R.layout.item_botao, colunaBotoes, false);
            btnExcluir.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Excluir")
                        .setMessage("Tem certeza que deseja excluir \"" + local + "\"?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            banco.deletar("Checkin", "Local = '" + local + "'");
                            carregarCheckins();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });

            colunaLocais.addView(textLocal);
            colunaBotoes.addView(btnExcluir);
        }

        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_to_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_home){

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}