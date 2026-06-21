package disp.moveis.luisfelipe.pratica1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaCheckIn extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    DatabaseSingleton banco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa_check_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        banco = DatabaseSingleton.getInstance(this);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa)).getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        double lat = getIntent().getDoubleExtra("latitude", 0);
        double lng = getIntent().getDoubleExtra("longitude", 0);

        LatLng minhaLocalizacao = new LatLng(lat, lng);

        Cursor c = banco.buscar(
                "Checkin",
        new String[]{"Local", "qtdVisitas", "latitude", "longitude", "cat"},
        "",
                ""
        );

        while(c.moveToNext()){
            String local = c.getString(c.getColumnIndexOrThrow("Local"));
            String visitas = c.getString(c.getColumnIndexOrThrow("qtdVisitas"));
            String categoria = c.getString(c.getColumnIndexOrThrow("cat"));
            double latLocal = Double.parseDouble(c.getString(c.getColumnIndexOrThrow("latitude")));
            double lngLocal = Double.parseDouble(c.getString(c.getColumnIndexOrThrow("longitude")));

            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(latLocal, lngLocal)).
                    title(local).
                    snippet("Categoria: "+categoria+"   Visitas: " + visitas));
        }

        c.close();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhaLocalizacao, 15));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa_check_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.item_home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }

        if(item.getItemId() == R.id.item_gestao){
            Intent intent = new Intent(this, GestaoCheckIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }

        if(item.getItemId() == R.id.item_mais_visitados){
            Intent intent = new Intent(this, Relatorio.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.item_mapa_normal) {
            item.setChecked(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        }
        if (item.getItemId() == R.id.item_mapa_hibrido) {
            item.setChecked(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        }
        if (item.getItemId() == R.id.item_mapa_satelite) {
            item.setChecked(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}