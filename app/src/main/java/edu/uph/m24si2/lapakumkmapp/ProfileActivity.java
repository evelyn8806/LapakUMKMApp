package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        displayUserData();
        setupNavigation();
    }

    private void displayUserData() {
        android.content.SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPref.getString("nama", "User");
        String email = sharedPref.getString("email", "email@example.com");
        String role = sharedPref.getString("role", "UMKM");
        String phone = sharedPref.getString("phone", "+62 812 3456 7890");

        ((TextView) findViewById(R.id.tvProfileName)).setText(name);
        ((TextView) findViewById(R.id.tvProfileEmail)).setText(email);
        ((TextView) findViewById(R.id.tvProfileRole)).setText(role.equals("UMKM") ? "Pelaku UMKM" : "Admin");

        setupInfoItem(R.id.infoName, "Nama Lengkap", name, android.R.drawable.ic_menu_edit);
        setupInfoItem(R.id.infoEmail, "Email", email, android.R.drawable.ic_dialog_email);
        setupInfoItem(R.id.infoPhone, "No. Telepon", phone, android.R.drawable.ic_menu_call);

        updateStatistics();
    }

    private void updateStatistics() {
        java.util.List<PengajuanModel> list = PengajuanManager.getInstance().getListPengajuan();
        int total = list.size();
        int disetujui = 0;
        for (PengajuanModel p : list) {
            if (p.getStatus().equalsIgnoreCase("Aktif") || p.getStatus().equalsIgnoreCase("Disetujui")) {
                disetujui++;
            }
        }

        ((TextView) findViewById(R.id.tvTotalPengajuan)).setText(String.valueOf(total));
        ((TextView) findViewById(R.id.tvPengajuanDisetujui)).setText(String.valueOf(disetujui));
    }

    private void setupInfoItem(int viewId, String label, String value, int iconRes) {
        View view = findViewById(viewId);
        ((TextView) view.findViewById(R.id.infoLabel)).setText(label);
        ((TextView) view.findViewById(R.id.infoValue)).setText(value);
        ((ImageView) view.findViewById(R.id.infoIcon)).setImageResource(iconRes);
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
