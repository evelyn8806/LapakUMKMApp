package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        initMenuItems();
        setupNavigation();
        
        findViewById(R.id.btnKeluarSetting).setOnClickListener(v -> {
            startActivity(new Intent(this, LogoutConfirmActivity.class));
        });
    }

    private void initMenuItems() {
        setupMenuItem(R.id.settingProfil, "Profil Saya", "Ubah detail profil", android.R.drawable.ic_menu_edit, ProfileActivity.class);
        setupMenuItem(R.id.settingKeamanan, "Keamanan", "Ubah kata sandi", android.R.drawable.ic_lock_idle_lock, SecurityActivity.class);
        setupMenuItem(R.id.settingPrivasi, "Kebijakan Privasi", "Lihat kebijakan", android.R.drawable.ic_menu_info_details, null);
        setupMenuItem(R.id.settingTerms, "Syarat & Ketentuan", "Lihat aturan main", android.R.drawable.ic_menu_agenda, null);
        setupMenuItem(R.id.settingTentang, "Tentang Aplikasi", "Versi 1.0.0", android.R.drawable.ic_menu_help, null);
    }

    private void setupMenuItem(int includeId, String title, String subtitle, int iconRes, Class<?> targetActivity) {
        View view = findViewById(includeId);
        ((TextView) view.findViewById(R.id.menuTitle)).setText(title);
        ((TextView) view.findViewById(R.id.menuSubtitle)).setText(subtitle);
        ((ImageView) view.findViewById(R.id.menuIcon)).setImageResource(iconRes);

        view.setOnClickListener(v -> {
            if (targetActivity != null) {
                startActivity(new Intent(this, targetActivity));
            } else {
                Toast.makeText(this, "Halaman " + title + " segera hadir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigation() {
        findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(this, ExplorationMapActivity.class));
            finish();
        });

        findViewById(R.id.navNotif).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
            finish();
        });
    }
}
