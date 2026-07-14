package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String role = displayUserData();
        initMenuItems();
        setupNavigation(role);
        setupLogout();
    }

    private String displayUserData() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPref.getString("nama", "User");
        String email = sharedPref.getString("email", "email@example.com");
        String role = sharedPref.getString("role", "UMKM");

        ((TextView) findViewById(R.id.tvUserName)).setText(name);
        ((TextView) findViewById(R.id.tvUserEmail)).setText(email);
        ((TextView) findViewById(R.id.tvUserRole)).setText(role.equals("UMKM") ? "Pelaku UMKM" : "Admin");
        
        return role;
    }

    private void initMenuItems() {
        // Profil Saya
        setupMenuItem(R.id.menuProfil, ProfileActivity.class, "Profil Saya", "Lihat dan edit profil", android.R.drawable.ic_menu_edit);
        
        // Pengaturan
        setupMenuItem(R.id.menuPengaturan, SettingsActivity.class, "Pengaturan", "Ubah pengaturan aplikasi", android.R.drawable.ic_menu_manage);
        
        // Keamanan
        setupMenuItem(R.id.menuKeamanan, SecurityActivity.class, "Keamanan", "Ubah kata sandi", android.R.drawable.ic_lock_idle_lock);
    }

    private void setupMenuItem(int includeId, final Class<?> targetActivity, String title, String subtitle, int iconRes) {
        View view = findViewById(includeId);
        if (view == null) return;

        TextView tvTitle = view.findViewById(R.id.menuTitle);
        TextView tvSubtitle = view.findViewById(R.id.menuSubtitle);
        ImageView ivIcon = view.findViewById(R.id.menuIcon);

        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
        ivIcon.setImageResource(iconRes);

        view.setOnClickListener(v -> {
            if (targetActivity != null) {
                startActivity(new Intent(this, targetActivity));
            } else {
                Toast.makeText(this, "Fitur " + title + " akan segera hadir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLogout() {
        findViewById(R.id.btnKeluar).setOnClickListener(v -> {
            startActivity(new Intent(this, LogoutConfirmActivity.class));
        });
    }

    private void setupNavigation(String role) {
        if (role.equals("Admin")) {
            findViewById(R.id.bottomNav).setVisibility(View.GONE);
            findViewById(R.id.adminBottomNav).setVisibility(View.VISIBLE);
            AdminNavigationHelper.setupNavigation(this, R.id.navAdminAkun);
        } else {
            findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);
            findViewById(R.id.adminBottomNav).setVisibility(View.GONE);
            
            findViewById(R.id.navBeranda).setOnClickListener(v -> {
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            });

            findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
                startActivity(new Intent(this, ExplorationMapActivity.class));
                finish();
            });

            findViewById(R.id.navLapak).setOnClickListener(v -> {
                startActivity(new Intent(this, MyStallsActivity.class));
                finish();
            });

            findViewById(R.id.navNotif).setOnClickListener(v -> {
                startActivity(new Intent(this, NotificationsActivity.class));
                finish();
            });
        }
    }
}
