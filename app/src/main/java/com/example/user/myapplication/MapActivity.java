package com.example.user.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {
    private ServerApi api;
    private List<Place> places = new ArrayList<>();
    private List<Integer> selectedPlaceTypes = new ArrayList<>();
    private GoogleMap map;
    private Chip chip1, chip2, chip3, chip4, chip5, chip6, chip7, chip8, chip9, chip10;
    private Map<Integer, Float> colorTypes= new HashMap<Integer, Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pastebin.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerApi.class);

        api.getPlaces().enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    places = response.body();
                    if (map != null)
                        fillMap();
                } else {
                    onNetworkError();
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                onNetworkError();
            }
        });
        colorTypes.put(1, BitmapDescriptorFactory.HUE_GREEN);
        colorTypes.put(2, BitmapDescriptorFactory.HUE_BLUE);
        colorTypes.put(3, BitmapDescriptorFactory.HUE_AZURE);
        colorTypes.put(4, BitmapDescriptorFactory.HUE_CYAN);
        colorTypes.put(5, BitmapDescriptorFactory.HUE_MAGENTA);
        colorTypes.put(6, BitmapDescriptorFactory.HUE_ORANGE);
        colorTypes.put(7, BitmapDescriptorFactory.HUE_RED);
        colorTypes.put(8, BitmapDescriptorFactory.HUE_ROSE);
        colorTypes.put(9, BitmapDescriptorFactory.HUE_VIOLET);
        colorTypes.put(10, BitmapDescriptorFactory.HUE_YELLOW);
        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        chip8 = findViewById(R.id.chip8);
        chip9 = findViewById(R.id.chip9);
        chip10 = findViewById(R.id.chip10);
        chip1.setOnCheckedChangeListener(this);
        chip2.setOnCheckedChangeListener(this);
        chip3.setOnCheckedChangeListener(this);
        chip4.setOnCheckedChangeListener(this);
        chip5.setOnCheckedChangeListener(this);
        chip6.setOnCheckedChangeListener(this);
        chip7.setOnCheckedChangeListener(this);
        chip8.setOnCheckedChangeListener(this);
        chip9.setOnCheckedChangeListener(this);
        chip10.setOnCheckedChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        this.map = map;

        if (places != null) {
            fillMap();
        }
    }

    private void fillMap() {
        map.clear();
        for (Place p : places) {
            int placeType = p.getType();
            boolean isSelected = false;
            for (int pt : selectedPlaceTypes)
                if (pt == placeType)
                    isSelected = true;

            if (!isSelected)
                continue;
            Position position = p.getPosition();
            LatLng latLng = new LatLng(position.getLat(), position.getLng());

            Float hue = colorTypes.get(placeType);
            if (hue == null)
                hue = BitmapDescriptorFactory.HUE_RED;
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(p.getTitle())
                    .snippet(p.getWorkTime())
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))
            );
            // Select marker color based on placeType
        }

    }

    private void onNetworkError() {
        if (map != null)
            map.clear();
        Toast.makeText(this, "An error occurred during network call", Toast.LENGTH_SHORT).show();
    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Chip[] chips = new Chip[]{chip1, chip2, chip3, chip4, chip5, chip6, chip7, chip8, chip9, chip10};
        selectedPlaceTypes.clear();

        for (Chip chip : chips) {
            if (chip.isChecked()) {
                selectedPlaceTypes.add(Integer.parseInt((String) chip.getTag()));
            }
        }
        fillMap();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}