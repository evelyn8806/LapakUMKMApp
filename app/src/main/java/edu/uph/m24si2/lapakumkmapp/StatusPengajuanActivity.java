package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StatusPengajuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pengajuan);

        ImageView btnBack = findViewById(R.id.btnBackStatus);
        Button btnLihatTiket = findViewById(R.id.btnLihatTiket);

        btnBack.setOnClickListener(v -> finish());

        btnLihatTiket.setOnClickListener(v -> {
            Intent intent = new Intent(this, ETicketActivity.class);
            startActivity(intent);
        });
    }
}
