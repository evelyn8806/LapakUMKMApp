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

        // Update UI with request data
        if (request != null) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Status " + request.getStatus().getLabel());
            // Other fields can be updated if needed, for demo we just use the event name
        }

        btnBack.setOnClickListener(v -> finish());

        btnLihatTiket.setOnClickListener(v -> {
            Intent intent = new Intent(this, ETicketActivity.class);
            intent.putExtra("rental_request", request);
            startActivity(intent);
        });

        if (request != null && request.getStatus() == RentalRequest.Status.AKTIF) {
            btnEventSelesai.setVisibility(View.VISIBLE);
        }

        btnEventSelesai.setOnClickListener(v -> {
            if (request != null) {
                request.setStatus(RentalRequest.Status.SELESAI);
                Toast.makeText(this, "Event Selesai! Tersimpan di History.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
