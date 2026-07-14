package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.bottomNav).findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        
        findViewById(R.id.bottomNav).findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(this, ExplorationMapActivity.class));
            finish();
        });

        findViewById(R.id.bottomNav).findViewById(R.id.navLapak).setOnClickListener(v -> {
            startActivity(new Intent(this, MyStallsActivity.class));
            finish();
        });

        findViewById(R.id.bottomNav).findViewById(R.id.navNotif).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
            finish();
        });
    }
}
