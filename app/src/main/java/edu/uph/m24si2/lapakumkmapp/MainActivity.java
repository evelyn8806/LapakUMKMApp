package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check login status
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPref.getBoolean("isLoggedIn", false);
        if (isRemembered) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Button btnMasuk = findViewById(R.id.btnMasuk);
        Button btnDaftar = findViewById(R.id.btnDaftar);
        LinearLayout roleUMKM = findViewById(R.id.roleUMKM);
        LinearLayout roleAdmin = findViewById(R.id.roleAdmin);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        roleUMKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Future role-based logic
            }
        });

        roleAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Future role-based logic
            }
        });
    }
}