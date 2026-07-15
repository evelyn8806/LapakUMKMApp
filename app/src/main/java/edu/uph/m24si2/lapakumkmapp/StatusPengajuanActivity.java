package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.graphics.Color;
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
                RentalManager.getInstance().updateRequest(request);
                
                NotificationManager.getInstance().addNotification("Event Selesai", 
                    "Event " + request.getEventName() + " telah berhasil diselesaikan. Terima kasih atas partisipasinya.");
                
                Toast.makeText(this, "Event Selesai! Tersimpan di History.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        updateUI();
    }

    private void updateUI() {
        if (request == null) return;

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvStatusHeader = findViewById(R.id.tvStatusHeader);
        TextView tvStatusDesc = findViewById(R.id.tvStatusDesc);
        View iconBg = findViewById(R.id.iconBg);
        Button btnLihatTiket = findViewById(R.id.btnLihatTiket);
        Button btnEventSelesai = findViewById(R.id.btnEventSelesai);

        tvTitle.setText("Status " + request.getStatus().getLabel());
        
        if (request.getStatus() == RentalRequest.Status.MENUNGGU_PERSETUJUAN) {
            tvStatusHeader.setText("Menunggu Persetujuan");
            tvStatusHeader.setTextColor(Color.parseColor("#FF8F00"));
            tvStatusDesc.setText("Pembayaran Anda berhasil diverifikasi. Mohon tunggu admin menyetujui pengajuan Anda.");
            btnLihatTiket.setVisibility(View.GONE);
            btnEventSelesai.setVisibility(View.GONE);
            iconBg.setBackgroundResource(R.drawable.bg_circle_orange);
        } else if (request.getStatus() == RentalRequest.Status.AKTIF) {
            tvStatusHeader.setText("Pengajuan Disetujui!");
            tvStatusHeader.setTextColor(Color.parseColor("#2E7D32"));
            tvStatusDesc.setText("Lapak Anda telah disetujui dan siap digunakan sesuai jadwal event.");
            btnLihatTiket.setVisibility(View.VISIBLE);
            btnEventSelesai.setVisibility(View.VISIBLE);
            iconBg.setBackgroundResource(R.drawable.bg_circle_green);
        } else if (request.getStatus() == RentalRequest.Status.SELESAI) {
            tvStatusHeader.setText("Event Selesai");
            tvStatusHeader.setTextColor(Color.parseColor("#616161"));
            tvStatusDesc.setText("Terima kasih telah berpartisipasi dalam event ini.");
            btnLihatTiket.setVisibility(View.GONE);
            btnEventSelesai.setVisibility(View.GONE);
            iconBg.setBackgroundResource(R.drawable.bg_circle_gray);
        } else {
            // Default/Menunggu Pembayaran
            btnLihatTiket.setVisibility(View.GONE);
            btnEventSelesai.setVisibility(View.GONE);
        }
    }
}
