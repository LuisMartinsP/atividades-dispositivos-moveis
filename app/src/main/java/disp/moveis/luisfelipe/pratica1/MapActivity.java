package disp.moveis.luisfelipe.pratica1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private ArrayList<LatLng> locais;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker markerUsuario;

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupButtons();

        Button btnLocalizacao = findViewById(R.id.btnLocalizacao);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnLocalizacao.setOnClickListener(v -> {
            if (mMap == null) return;
            pegarLocalizacao();
        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );
        }
    }

    private void setupButtons(){
        Button btnCasaNatal = findViewById(R.id.btnCasaNatal);
        Button btnVicosa = findViewById(R.id.btnVicosa);
        Button btnDepartamento = findViewById(R.id.btnDepartamento);

        btnCasaNatal.setOnClickListener(v -> moverCamera(0));
        btnVicosa.setOnClickListener(v -> moverCamera(1));
        btnDepartamento.setOnClickListener(v -> moverCamera(2));
    }

    private void pegarLocalizacao() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location == null) {
                        Toast.makeText(this, "Localização ainda não disponível", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LatLng posicao = new LatLng(
                            location.getLatitude(),
                            location.getLongitude()
                    );

                    atualizarMarcador(posicao);
                });
    }

    private void atualizarMarcador(LatLng posicao) {

        if (markerUsuario != null) {
            markerUsuario.remove();
        }

        markerUsuario = mMap.addMarker(
                new MarkerOptions()
                        .position(posicao)
                        .title("Minha localização atual")
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_BLUE
                        ))
        );
        markerUsuario.showInfoWindow();


        Location loc1 = new Location("");
        loc1.setLatitude(locais.get(1).latitude);
        loc1.setLongitude(locais.get(1).longitude);



        Location loc2 = new Location("");
        LatLng pos = markerUsuario.getPosition();
        loc2.setLatitude(pos.latitude);
        loc2.setLongitude(pos.longitude);

        float distancia = loc1.distanceTo(loc2); // em metros

        Toast.makeText(this, "Distancia (m): "+distancia, Toast.LENGTH_LONG).show();
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(posicao, 16)
        );
    }
    private void moverCamera(int indice){
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(locais.get(indice), 15)
        );
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Intent it = getIntent();
        int coordenada = it.getIntExtra("indice", 0);

        locais = it.getParcelableArrayListExtra("locais");

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        for(LatLng latlng : locais){
            mMap.addMarker(new MarkerOptions().position(latlng).title("Viçosa"));
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locais.get(coordenada), 15));
    }
}