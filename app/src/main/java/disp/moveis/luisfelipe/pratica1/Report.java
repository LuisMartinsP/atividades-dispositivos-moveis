package disp.moveis.luisfelipe.pratica1;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Report extends ListActivity {

    ArrayList<String> logs = new ArrayList<>();
    ArrayList<Double> latitudes = new ArrayList<>();
    ArrayList<Double> longitudes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseSingleton db = DatabaseSingleton.getInstance(this);

        Cursor c = db.executarSQL(
                "SELECT Logs.msg, Logs.timestamp, " +
                        "Location.latitude, Location.longitude " +
                        "FROM Logs " +
                        "INNER JOIN Location " +
                        "ON Logs.id_location = Location.id"
        );


        while(c.moveToNext()){

            String msg = c.getString(
                    c.getColumnIndexOrThrow("msg")
            );

            String timestamp = c.getString(
                    c.getColumnIndexOrThrow("timestamp")
            );

            double latitude = c.getDouble(
                    c.getColumnIndexOrThrow("latitude")
            );

            double longitude = c.getDouble(
                    c.getColumnIndexOrThrow("longitude")
            );

            logs.add(msg + " - " + timestamp);

            latitudes.add(latitude);

            longitudes.add(longitude);
        }
        c.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                logs
        );

        int padding = (int) (50 * getResources().getDisplayMetrics().density);

        ListView listView = getListView();

        listView.setPadding(0, padding, 0, padding);
        listView.setClipToPadding(false);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        double latitude = latitudes.get(position);

        double longitude = longitudes.get(position);

        Toast.makeText(
                this,
                "Lat: " + latitude + "\nLng: " + longitude,
                Toast.LENGTH_SHORT
        ).show();

    }
}