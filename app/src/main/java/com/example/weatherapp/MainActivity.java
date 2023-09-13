package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView nomeDaCidadeTV, temperaturaTV, condicaoTV;
    private RecyclerView tempoRV;
    private TextInputEditText cidadeEdt;
    private ImageView backIV, iconeIV, pesquisaIV;
    private ArrayList<TempoRVModal> tempoRVModalArrayList;
    private TempoRVAdapter tempoRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String nomeDaCidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        nomeDaCidadeTV = findViewById(R.id.idTVNomeCidade);
        temperaturaTV = findViewById(R.id.idTVTemperatura);
        condicaoTV = findViewById(R.id.idTVCondicao);
        tempoRV = findViewById(R.id.idRVTempo);
        cidadeEdt = findViewById(R.id.idEdtCidade);
        backIV = findViewById(R.id.idIVBack);
        iconeIV = findViewById(R.id.idIVIcone);
        pesquisaIV = findViewById(R.id.idIVPesquisar);
        tempoRVModalArrayList = new ArrayList<>();
        tempoRVAdapter = new TempoRVAdapter(this, tempoRVModalArrayList);
        tempoRV.setAdapter(tempoRVAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        nomeDaCidade = getNomeDaCidade(location.getLongitude(),location.getLatitude());
        getTempoInfo(nomeDaCidade);

        pesquisaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidade = cidadeEdt.getText().toString();
                if(cidade.isEmpty()){
                    Toast.makeText(MainActivity.this, "Por favor, digite o nome da cidade", Toast.LENGTH_SHORT).show();
                }else{
                    nomeDaCidadeTV.setText(nomeDaCidade);
                    getTempoInfo(cidade);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissões garantidas", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Por favor, autorize as permissões de acesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getNomeDaCidade(double longitude, double latitude){
        String nomeDaCidade = "Não encontrado";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address adr : addresses){
                if(adr!=null){
                    String cidade = adr.getLocality();
                    if(cidade!=null && !cidade.equals("")){
                        nomeDaCidade = cidade;
                    }else{
                        Log.d("TAG", "CIDADE NÃO ENCONTRADA");
                        Toast.makeText(this, "Cidade do usuário não encontrado!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return nomeDaCidade;
    }
    private void getTempoInfo(String nomeDaCidade){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=81561d09fb4740b3a39150444230709&q="+nomeDaCidade+"&days=1&aqi=yes&alerts=yes";
        nomeDaCidadeTV.setText(nomeDaCidade);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                tempoRVModalArrayList.clear();

                try {
                    String temperatura = response.getJSONObject("current").getString("temp_c");
                    temperaturaTV.setText(temperatura+"°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condicao = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String condicaoIcone = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("").into(backIV);
                    condicaoTV.setText(condicao);
                    if (isDay == 1) {
                        //manhã
                        Picasso.get().load("https://images.unsplash.com/photo-1566228015668-4c45dbc4e2f5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80").into(backIV);
                    }else{
                        Picasso.get().load("https://images.unsplash.com/photo-1532074534361-bb09a38cf917?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80").into(backIV);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastO.getJSONArray("hour");

                    for (int i = 0; i<hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String hora = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String vento = hourObj.getString("wind_kph");
                        tempoRVModalArrayList.add(new TempoRVModal(hora, temper, img, vento));
                    }

                    tempoRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Por favor, digite o nome de uma cidade válida", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}