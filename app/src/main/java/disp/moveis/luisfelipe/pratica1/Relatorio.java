package disp.moveis.luisfelipe.pratica1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Relatorio extends AppCompatActivity {
    DatabaseSingleton banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_relatorio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        banco = DatabaseSingleton.getInstance(this);
        carregarLugares();
    }

    private void carregarLugares() {
        LinearLayout colunaLocais = findViewById(R.id.colunaLocais);
        LinearLayout colunaVisitas = findViewById(R.id.colunaVisitas);

        colunaLocais.removeAllViews();
        colunaVisitas.removeAllViews();

        Cursor c = banco.buscar(
                "Checkin",
                new String[]{"Local", "qtdVisitas"},
                "",
                "qtdVisitas DESC"
        );

        int dp10 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()
        );

        while (c.moveToNext()) {
            String local = c.getString(c.getColumnIndexOrThrow("Local"));
            int visitas = c.getInt(c.getColumnIndexOrThrow("qtdVisitas"));

            TextView textLocal = (TextView) LayoutInflater.from(this)
                    .inflate(R.layout.item_local, colunaLocais, false);
            textLocal.setText(local);

            TextView textVisitas = (TextView) LayoutInflater.from(this)
                    .inflate(R.layout.item_visitas, colunaVisitas, false);
            textVisitas.setText(String.valueOf(visitas));

            colunaLocais.addView(textLocal);
            colunaVisitas.addView(textVisitas);
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