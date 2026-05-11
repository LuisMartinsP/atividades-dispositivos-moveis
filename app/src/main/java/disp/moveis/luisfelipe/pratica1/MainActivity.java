package disp.moveis.luisfelipe.pratica1;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String menu[] = new String[]{
            "Minha casa na cidade natal",
            "Minha casa em Viçosa",
            "Meu departamento",
            "Relatório",
            "Sair da aplicação"
        };



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
    this,
            android.R.layout.simple_list_item_1,
            menu
            );

        setListAdapter(arrayAdapter);

        ListView listView = getListView();

        int topPadding = (int) (50 * getResources().getDisplayMetrics().density);

        listView.setPadding(0, topPadding, 0, 0);
        listView.setClipToPadding(false);
        };

    public void onListItemClick(ListView l, View v, int position, long id){
        if(position == 4){
            finish();
            return;
        }
        if(position == 3){

            Intent it = new Intent(this, Report.class);

            startActivity(it);

            return;
        }

        Instant tempoClique = Instant.now();

        DatabaseSingleton db = DatabaseSingleton.getInstance(this);

        Cursor c = db.buscar(
                "Location",
                null,
                "",
                "id ASC"
        );

        ArrayList<LatLng> locais = new ArrayList<>();

        while(c.moveToNext()){

            double latitude = c.getDouble(
                    c.getColumnIndexOrThrow("latitude")
            );

            double longitude = c.getDouble(
                    c.getColumnIndexOrThrow("longitude")
            );

            locais.add(
                    new LatLng(latitude, longitude)
            );
        }

        c.close();

        ContentValues valores = new ContentValues();

        valores.put("timestamp", tempoClique+"");
        valores.put("id_location", position+1);

        long idInsercao;
        switch(position){
            case 0:
                valores.put("msg", "Ponte Nova");
                idInsercao = db.inserir("Logs", valores);
                break;
            case 1:
                valores.put("msg", "Viçosa");
                idInsercao = db.inserir("Logs", valores);
                break;
            case 2:
                valores.put("msg", "DPI");
                idInsercao = db.inserir("Logs", valores);
                break;

        }


        Intent it = new Intent(this, MapActivity.class);
        String aux = l.getItemAtPosition(position).toString();

        Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();

        it.putExtra("indice", position);

        it.putExtra("locais", locais);

        startActivity(it);
    }
}