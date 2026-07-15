package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String selectedRole = "UMKM"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load persistent users
        UserManager.getInstance().loadUsers(this);

        // Check login status
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPref.getBoolean("isLoggedIn", false);
        if (isRemembered) {
            String email = sharedPref.getString("email", "");
            String role = sharedPref.getString("role", "");
            Intent intent;
            if ("ADMIN".equals(role) || email.endsWith("@umkm.com")) {
                intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(MainActivity.this, DashboardActivity.class);
            }
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Button btnMasuk = findViewById(R.id.btnMasuk);
        Button btnDaftar = findViewById(R.id.btnDaftar);
        final LinearLayout roleUMKM = findViewById(R.id.roleUMKM);
        final LinearLayout roleAdmin = findViewById(R.id.roleAdmin);

        // Initial visual state
        roleUMKM.setBackgroundResource(R.drawable.bg_role_card_selected);
        roleAdmin.setBackgroundResource(R.drawable.bg_role_card);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("ROLE", selectedRole);
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("ROLE", selectedRole);
                startActivity(intent);
            }
        });

        roleUMKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRole = "UMKM";
                roleUMKM.setBackgroundResource(R.drawable.bg_role_card_selected);
                roleAdmin.setBackgroundResource(R.drawable.bg_role_card);
            }
        });

        roleAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRole = "ADMIN";
                roleAdmin.setBackgroundResource(R.drawable.bg_role_card_selected);
                roleUMKM.setBackgroundResource(R.drawable.bg_role_card);
            }
        });
    }
}