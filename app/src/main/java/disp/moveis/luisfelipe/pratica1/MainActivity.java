package disp.moveis.luisfelipe.pratica1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String menu[] = new String[]{
            "Minha casa na cidade natal",
            "Minha casa em Viçosa",
            "Meu departamento",
            "Sair da aplicação"
        };

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
    this,
            android.R.layout.simple_list_item_1,
            menu
            );

        setListAdapter(arrayAdapter);
        };

    public void onListItemClick(ListView l, View v, int position, long id){
        Intent it = new Intent(this, MapActivity.class);
        String aux = l.getItemAtPosition(position).toString();

        switch(position){
            case 0:
                Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
                startActivity(it);
                break;
            case 1:
                Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
                startActivity(it);
                break;
            case 2:
                Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
                startActivity(it);
                break;
            case 3:
                finish();
        }
    }
}