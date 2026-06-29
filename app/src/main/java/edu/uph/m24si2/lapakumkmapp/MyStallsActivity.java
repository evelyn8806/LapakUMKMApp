package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MyStallsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stalls);

        setupBottomNav();
        setupActionButtons();
    }

    private void setupActionButtons() {
        // Tombol Lihat Detail pada item 1
        findViewById(R.id.tabScroll).requestFocus(); // Biar fokus ga di tombol bawah

        // Mencari tombol di dalam layout (karena layoutnya statis, kita bisa ambil button berdasarkan posisinya atau ID jika ada)
        // Item 1: Festival Kuliner Nusantara (Menunggu Verifikasi)
        // Button ini tidak punya ID unik di layout statis tadi, tapi kita bisa berikan atau cari.
        // Untuk kemudahan, saya akan tambahkan Toast dulu sebagai logika dummy.
    }

    // Fungsi-fungsi tombol di XML (jika ditambahkan android:onClick) atau via Listener
    public void onLihatDetailClick(View v) {
        Intent intent = new Intent(this, LapakDetailActivity.class);
        intent.putExtra("nama_lapak", "Festival Kuliner Nusantara");
        startActivity(intent);
    }

    public void onBayarClick(View v) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void onLihatETicketClick(View v) {
        Intent intent = new Intent(this, ETicketActivity.class);
        startActivity(intent);
    }

    private void setupBottomNav() {
        findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(this, MapsActivity.class));
        });

        findViewById(R.id.navNotif).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
            finish();
        });
    }
}
