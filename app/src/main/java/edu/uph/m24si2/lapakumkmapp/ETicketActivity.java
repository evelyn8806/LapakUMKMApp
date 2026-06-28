package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ETicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);

        ImageView btnBack = findViewById(R.id.btnBackTicket);
        Button btnUnduh = findViewById(R.id.btnUnduhTiket);
        Button btnBagikan = findViewById(R.id.btnBagikan);

        btnBack.setOnClickListener(v -> finish());

        btnUnduh.setOnClickListener(v -> {
            Toast.makeText(this, "Mengunduh Tiket PDF...", Toast.LENGTH_SHORT).show();
        });

        btnBagikan.setOnClickListener(v -> {
            Toast.makeText(this, "Membuka Menu Bagikan...", Toast.LENGTH_SHORT).show();
        });
    }
}
