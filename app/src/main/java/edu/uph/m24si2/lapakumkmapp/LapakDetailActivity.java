package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LapakDetailActivity extends AppCompatActivity {

    private ImageView ivLapakDetail;
    private TextView tvNamaLapakDetail, tvKategoriLapakDetail, tvDeskripsiLapakDetail, tvLokasiLapakDetail;
    private Button btnAjukanSewa;
    private int eventImage;
    private String eventPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapak_detail);

        ivLapakDetail = findViewById(R.id.ivLapakDetail);
        tvNamaLapakDetail = findViewById(R.id.tvNamaLapakDetail);
        tvKategoriLapakDetail = findViewById(R.id.tvKategoriLapakDetail);
        tvDeskripsiLapakDetail = findViewById(R.id.tvDeskripsiLapakDetail);
        tvLokasiLapakDetail = findViewById(R.id.tvLokasiLapakDetail);
        btnAjukanSewa = findViewById(R.id.btnAjukanSewa);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nama = extras.getString("nama_lapak", "Nama UMKM");
            String kategori = extras.getString("kategori_lapak", "Kuliner");
            String deskripsi = extras.getString("deskripsi_lapak", "Deskripsi tidak tersedia.");
            String lokasi = extras.getString("lokasi_lapak", "Lokasi tidak tersedia.");
            eventImage = extras.getInt("gambar_lapak", R.drawable.festival_kuliner);
            eventPrice = extras.getString("harga_lapak", "Rp0");

            tvNamaLapakDetail.setText(nama);
            tvKategoriLapakDetail.setText("Kategori: " + kategori);
            tvDeskripsiLapakDetail.setText(deskripsi);
            tvLokasiLapakDetail.setText(lokasi);
            ivLapakDetail.setImageResource(eventImage);
        }

        btnAjukanSewa.setOnClickListener(v -> {
            Intent intent = new Intent(LapakDetailActivity.this, PengajuanSewaActivity.class);
            intent.putExtra("nama_event", tvNamaLapakDetail.getText().toString());
            intent.putExtra("lokasi_event", tvLokasiLapakDetail.getText().toString());
            intent.putExtra("gambar_event", eventImage);
            intent.putExtra("harga_event", eventPrice);
            startActivity(intent);
        });
    }
}
