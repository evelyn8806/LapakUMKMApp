package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView btnBack;
    private Spinner spinnerCity;
    private View cvNoEventOverlay;
    private Button btnRequestEvent;
    private FloatingActionButton fabMyLocation;

    private final Map<String, LatLng> cityCoordinates = new HashMap<String, LatLng>() {{
        put("Bandung", new LatLng(-6.9175, 107.6191));
        put("Jakarta", new LatLng(-6.2088, 106.8456));
        put("Yogyakarta", new LatLng(-7.7956, 110.3695));
        put("Surabaya", new LatLng(-7.2575, 112.7521));
        put("Medan", new LatLng(3.5952, 98.6722));
    }};

    private List<EventModel> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        allEvents = EventManager.getAllEvents();

        btnBack = findViewById(R.id.btnBack);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerCity.setVisibility(View.VISIBLE);
        
        cvNoEventOverlay = findViewById(R.id.cvNoEventOverlay);
        btnRequestEvent = findViewById(R.id.btnRequestEvent);
        fabMyLocation = findViewById(R.id.fabMyLocation);
        
        // Hide list-related views
        findViewById(R.id.llSearch).setVisibility(View.GONE);
        findViewById(R.id.rvExploration).setVisibility(View.GONE);

        setupCitySpinner();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.ivSearchIcon).setOnClickListener(v -> {
            if (findViewById(R.id.llSearch).getVisibility() == View.GONE) {
                findViewById(R.id.llSearch).setVisibility(View.VISIBLE);
                findViewById(R.id.rvExploration).setVisibility(View.VISIBLE);
                findViewById(R.id.map).setVisibility(View.GONE);
                spinnerCity.setVisibility(View.GONE);
                fabMyLocation.setVisibility(View.GONE);
            } else {
                findViewById(R.id.llSearch).setVisibility(View.GONE);
                findViewById(R.id.rvExploration).setVisibility(View.GONE);
                findViewById(R.id.map).setVisibility(View.VISIBLE);
                spinnerCity.setVisibility(View.VISIBLE);
                fabMyLocation.setVisibility(View.VISIBLE);
            }
        });

        btnRequestEvent.setOnClickListener(v -> {
            String selectedCity = spinnerCity.getSelectedItem().toString();
            Toast.makeText(this, "Request event untuk kota " + selectedCity + " terkirim!", Toast.LENGTH_LONG).show();
            cvNoEventOverlay.setVisibility(View.GONE);
        });
    }

    private void setupCitySpinner() {
        List<String> cities = new ArrayList<>(cityCoordinates.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities.get(position);
                updateMapForCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateMapForCity(String cityName) {
        if (mMap == null) return;

        LatLng coordinates = cityCoordinates.get(cityName);
        if (coordinates != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f));
        }

        mMap.clear();
        boolean hasEvent = false;
        
        // Pastikan allEvents tidak null
        if (allEvents == null) allEvents = EventManager.getAllEvents();

        // Filter menggunakan data terpusat dari EventManager
        for (EventModel event : allEvents) {
            if (event.getKota().trim().equalsIgnoreCase(cityName.trim())) {
                LatLng pos = new LatLng(event.getLatitude(), event.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(event.getNama())
                        .snippet(event.getKategori() + " | " + event.getHarga())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                hasEvent = true;
            }
        }

        mMap.setOnInfoWindowClickListener(marker -> {
            String title = marker.getTitle();
            for (EventModel event : allEvents) {
                if (event.getNama().equals(title)) {
                    Intent intent = new Intent(MapsActivity.this, LapakDetailActivity.class);
                    intent.putExtra("nama_lapak", event.getNama());
                    intent.putExtra("kategori_lapak", event.getKategori());
                    intent.putExtra("deskripsi_lapak", event.getDeskripsi());
                    intent.putExtra("lokasi_lapak", event.getLokasi());
                    intent.putExtra("gambar_lapak", event.getGambar());
                    startActivity(intent);
                    break;
                }
            }
        });

        cvNoEventOverlay.setVisibility(hasEvent ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        
        // Tambahkan ini agar InfoWindow bisa diklik
        mMap.setOnInfoWindowClickListener(marker -> {
            String title = marker.getTitle();
            for (EventModel event : allEvents) {
                if (event.getNama().equals(title)) {
                    Intent intent = new Intent(MapsActivity.this, LapakDetailActivity.class);
                    intent.putExtra("nama_lapak", event.getNama());
                    intent.putExtra("kategori_lapak", event.getKategori());
                    intent.putExtra("deskripsi_lapak", event.getDeskripsi());
                    intent.putExtra("lokasi_lapak", event.getLokasi());
                    intent.putExtra("gambar_lapak", event.getGambar());
                    startActivity(intent);
                    break;
                }
            }
        });

        if (spinnerCity.getSelectedItem() != null) {
            updateMapForCity(spinnerCity.getSelectedItem().toString());
        }
    }
}
