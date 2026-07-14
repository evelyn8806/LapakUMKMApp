package edu.uph.m24si2.lapakumkmapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;

public class AdminPenggunaActivity extends AppCompatActivity {

    private String currentFilter = "Semua";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pengguna);

        AdminNavigationHelper.setupNavigation(this, R.id.navAdminPengguna);
        setupTabs();
        loadUsers();
    }

    private void setupTabs() {
        LinearLayout tabsLayout = (LinearLayout) ((android.widget.HorizontalScrollView) findViewById(R.id.tabsAdminUser)).getChildAt(0);
        for (int i = 0; i < tabsLayout.getChildCount(); i++) {
            View view = tabsLayout.getChildAt(i);
            if (view instanceof MaterialButton) {
                MaterialButton btn = (MaterialButton) view;
                btn.setOnClickListener(v -> {
                    currentFilter = btn.getText().toString();
                    updateTabStyles(tabsLayout);
                    loadUsers();
                });
            }
        }
    }

    private void updateTabStyles(LinearLayout tabsLayout) {
        for (int i = 0; i < tabsLayout.getChildCount(); i++) {
            View view = tabsLayout.getChildAt(i);
            if (view instanceof MaterialButton) {
                MaterialButton btn = (MaterialButton) view;
                if (btn.getText().toString().equals(currentFilter)) {
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.admin_primary)));
                    btn.setTextColor(Color.WHITE);
                } else {
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F5F5F5")));
                    btn.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
                }
            }
        }
    }

    private void loadUsers() {
        LinearLayout container = findViewById(R.id.containerPenggunaFull);
        if (container == null) return;
        
        container.removeAllViews();
        
        // Dummy Users based on the image
        addUser(container, "Siti Nur Aisyah", "sitinuraisyah@email.com", "Pelaku UMKM");
        addUser(container, "Ahmad Rizki", "ahmadrizki@email.com", "Pelaku UMKM");
        addUser(container, "Budi Santoso", "budisantoso@email.com", "Pelaku UMKM");
        addUser(container, "Dewi Lestari", "dewilestari@email.com", "Pelaku UMKM");
        addUser(container, "Rina Handayani", "rina.handayani@email.com", "Pelaku UMKM");
        addUser(container, "Admin Utama", "admin@umkmapp.com", "Admin");
    }

    private void addUser(LinearLayout container, String name, String email, String role) {
        // Filter logic
        if (!currentFilter.equals("Semua") && !currentFilter.equals(role)) {
            return;
        }

        View itemView = getLayoutInflater().inflate(R.layout.item_admin_user, container, false);
        ((TextView) itemView.findViewById(R.id.tvUserName)).setText(name);
        ((TextView) itemView.findViewById(R.id.tvUserEmail)).setText(email);
        
        TextView tvRole = itemView.findViewById(R.id.tvUserRoleTag);
        tvRole.setText(role);
        
        if (role.equals("Admin")) {
            tvRole.setTextColor(Color.parseColor("#2196F3"));
            tvRole.setBackgroundResource(R.drawable.bg_tag_new); // Reuse or create new
            // Re-tint background if needed
            tvRole.getBackground().setTint(Color.parseColor("#E3F2FD"));
        } else {
            tvRole.setTextColor(ContextCompat.getColor(this, R.color.admin_primary));
            tvRole.setBackgroundResource(R.drawable.bg_tag_new);
        }
        
        itemView.setOnClickListener(v -> Toast.makeText(this, "Detail " + name, Toast.LENGTH_SHORT).show());
        container.addView(itemView);
    }
}
