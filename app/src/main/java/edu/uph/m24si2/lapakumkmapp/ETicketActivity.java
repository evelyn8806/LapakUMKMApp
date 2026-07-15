package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
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

        btnBack.setOnClickListener(v -> backToDashboard());

        btnUnduh.setOnClickListener(v -> {
            Toast.makeText(this, "Mengunduh Tiket PDF...", Toast.LENGTH_SHORT).show();
        });

    }

    private void backToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToDashboard();
    }
}
