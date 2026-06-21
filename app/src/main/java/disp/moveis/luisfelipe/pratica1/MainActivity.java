package disp.moveis.luisfelipe.pratica1;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private LocationListener locationListener;
    AutoCompleteTextView campo;
    ArrayAdapter<String> adapter;

    Spinner spinnerCategoria;
    ArrayAdapter<String> adapterCategoria;

    DatabaseSingleton banco;

    Handler handler = new Handler();

    LocationManager locationManager;

    TextView textLatitude;
    TextView textLongitude;

    double latitude = 0;
    double longitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                });

        textLatitude = findViewById(R.id.textLatitude);
        textLongitude = findViewById(R.id.textLongitude);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("CheckInLocais");
        }


        banco = DatabaseSingleton.getInstance(this);

        locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);


        pegarLocalizacao();

        campo = findViewById(R.id.autoComplete);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>()
        );

        campo.setAdapter(adapter);
        campo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {}


            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {


                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(() -> {

                    if(s.length() >= 2){

                        buscarLocais(
                                s.toString()
                        );

                    }

                },300);
            }

            @Override
            public void afterTextChanged(
                    Editable s) {}

        });

        spinnerCategoria =
                findViewById(R.id.spinnerCategoria);



        ArrayList<String> categorias =
                buscarCategorias();



        adapterCategoria = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                categorias
        );

        adapterCategoria.setDropDownViewResource(R.layout.item_spinner);


        adapterCategoria.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerCategoria.setAdapter(adapterCategoria);

    }
    private void buscarLocais(String termo){

        Cursor c = banco.buscar(
                "Checkin",

                new String[]{"Local"},
                "Local LIKE '%" + termo + "%'",
                ""
        );


        ArrayList<String> locais =
                new ArrayList<>();


        while(c.moveToNext()){


            locais.add(
                    c.getString(
                            c.getColumnIndexOrThrow("Local")
                    )
            );

        }

        c.close();

        runOnUiThread(() -> {

            adapter.clear();
            adapter.addAll(locais);
            adapter.notifyDataSetChanged();

            campo.showDropDown();

        });

    }

    private ArrayList<String> buscarCategorias(){

        ArrayList<String> lista =
                new ArrayList<>();

        Cursor c = banco.buscar(
                "Categoria",
                new String[]{"nome"},
                "",
                "nome"
        );

        while(c.moveToNext()){
            lista.add(
                    c.getString(
                            c.getColumnIndexOrThrow("nome")
                    )
            );

        }
        c.close();

        return lista;
    }
    public void checkIn(View v){


        String local =
                campo.getText().toString();


        String categoria =
                spinnerCategoria
                        .getSelectedItem()
                        .toString();



        if(local.isEmpty()){
            return;
        }



        if(existeLocal(local)){


            atualizarCheckin(local);


        }else{


            cadastrarCheckin(
                    local,
                    categoria
            );

        }

    }







    private boolean existeLocal(String local){


        Cursor c = banco.buscar(
                "Checkin",
                new String[]{"Local"},
                "Local = '" + local + "'",
                ""
        );


        boolean existe =
                c.moveToFirst();


        c.close();


        return existe;

    }







    private void atualizarCheckin(String local){
        int visitas =
                buscarVisitas(local);

        ContentValues valores =
                new ContentValues();

        valores.put(
                "qtdVisitas",
                visitas + 1
        );

        banco.atualizar(
                "Checkin",
                valores,
                "Local = '" + local + "'"
        );

    }

    private int buscarVisitas(String local){

        Cursor c = banco.buscar(
                "Checkin",
                new String[]{"qtdVisitas"},
                "Local = '" + local + "'",
                ""
        );

        int qtd = 0;

        if(c.moveToFirst()){

            qtd =
                    c.getInt(
                            c.getColumnIndexOrThrow(
                                    "qtdVisitas"
                            )
                    );

        }
        c.close();
        return qtd;

    }

    private void cadastrarCheckin(
            String local,
            String categoria){

        int idCategoria =
                buscarIdCategoria(categoria);

        ContentValues valores =
                new ContentValues();

        valores.put("Local", local);

        valores.put(
                "qtdVisitas",
                1
        );

        valores.put(
                "cat",
                idCategoria
        );


        // depois trocar por GPS
        valores.put(
                "latitude",
                String.valueOf(latitude)
        );


        valores.put(
                "longitude",
                String.valueOf(longitude)
        );

        banco.inserir(
                "Checkin",
                valores
        );

    }

    private int buscarIdCategoria(String nome){
        Cursor c = banco.buscar(
                "Categoria",
                new String[]{"idCategoria"},
                "nome = '" + nome + "'",
                ""
        );

        int id = -1;

        if(c.moveToFirst()){

            id =
                    c.getInt(
                            c.getColumnIndexOrThrow(
                                    "idCategoria"
                            )
                    );

        }


        c.close();


        return id;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater()
                .inflate(
                        R.menu.menu_main,
                        menu
                );

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.item_mapa){
            Intent intent = new Intent(this, MapaCheckIn.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
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

        return super.onOptionsItemSelected(item);
    }


    private void pegarLocalizacao() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);

        if (provider == null) {
            Log.d("GPS", "Nenhum provider disponível");
            return;
        }

        Log.d("GPS", "Usando: " + provider);

        Location ultimoLocal = locationManager.getLastKnownLocation(provider);
        if (ultimoLocal != null) {
            latitude = ultimoLocal.getLatitude();
            longitude = ultimoLocal.getLongitude();
            textLatitude.setText("Latitude: " + latitude);
            textLongitude.setText("Longitude: " + longitude);
        }

        locationManager.requestLocationUpdates(provider, 5000, 5,
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        runOnUiThread(() -> {
                            textLatitude.setText("Latitude: " + latitude);
                            textLongitude.setText("Longitude: " + longitude);
                        });
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pegarLocalizacao();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pegarLocalizacao();
    }

}

