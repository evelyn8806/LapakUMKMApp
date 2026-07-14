package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setupBottomNav();

    }

    private void setupBottomNav() {
        findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(this, ExplorationMapActivity.class));
        });

        findViewById(R.id.navLapak).setOnClickListener(v -> {
            startActivity(new Intent(this, MyStallsActivity.class));
            finish();
        });

        findViewById(R.id.navAkun).setOnClickListener(v -> {
            startActivity(new Intent(this, AccountActivity.class));
            finish();
        });
    }
}
