package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StatusPengajuanActivity extends AppCompatActivity {

    private RentalRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pengajuan);

        request = (RentalRequest) getIntent().getSerializableExtra("rental_request");

        ImageView btnBack = findViewById(R.id.btnBackStatus);
        Button btnLihatTiket = findViewById(R.id.btnLihatTiket);
        Button btnEventSelesai = findViewById(R.id.btnEventSelesai);

        btnBack.setOnClickListener(v -> finish());

        btnLihatTiket.setOnClickListener(v -> {
            Intent intent = new Intent(this, ETicketActivity.class);
            intent.putExtra("rental_request", request);
            startActivity(intent);
        });

        btnEventSelesai.setOnClickListener(v -> {
            if (request != null) {
                request.setStatus(RentalRequest.Status.SELESAI);
                Toast.makeText(this, "Event Selesai! Tersimpan di History.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Update UI with request data
        if (request != null) {
            TextView tvTitle = findViewById(R.id.tvTitle);
            TextView tvStatusHeader = findViewById(R.id.tvStatusHeader);
            TextView tvStatusDesc = findViewById(R.id.tvStatusDesc);

            tvTitle.setText("Status " + request.getStatus().getLabel());
            
            if (request.getStatus() == RentalRequest.Status.MENUNGGU_PERSETUJUAN) {
                tvStatusHeader.setText("Menunggu Persetujuan");
                tvStatusDesc.setText("Pembayaran Anda berhasil diverifikasi. Mohon tunggu admin menyetujui pengajuan Anda.");
                btnLihatTiket.setVisibility(View.GONE);
                btnEventSelesai.setVisibility(View.GONE);
            } else if (request.getStatus() == RentalRequest.Status.AKTIF) {
                tvStatusHeader.setText("Pengajuan Disetujui!");
                tvStatusDesc.setText("Lapak Anda telah disetujui and siap digunakan sesuai jadwal event.");
                btnLihatTiket.setVisibility(View.VISIBLE);
                btnEventSelesai.setVisibility(View.VISIBLE);
            } else if (request.getStatus() == RentalRequest.Status.SELESAI) {
                tvStatusHeader.setText("Event Selesai");
                tvStatusDesc.setText("Terima kasih telah berpartisipasi dalam event ini.");
                btnLihatTiket.setVisibility(View.GONE);
                btnEventSelesai.setVisibility(View.GONE);
            }
        }
    }
}
