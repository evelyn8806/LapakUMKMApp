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
import java.util.List;

public class AdminPenggunaActivity extends AppCompatActivity {

    private String currentFilter = "Semua";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pengguna);

        UserManager.getInstance().loadUsers(this);

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
        
        List<UserModel> users = UserManager.getInstance().getListUsers();
        boolean hasItems = false;

        for (UserModel user : users) {
            // Filter logic
            String roleTag = user.getRole().equals("ADMIN") ? "Admin" : "Pelaku UMKM";
            if (!currentFilter.equals("Semua") && !currentFilter.equals(roleTag)) {
                continue;
            }

            hasItems = true;
            addUserView(container, user);
        }

        if (!hasItems) {
            TextView emptyText = new TextView(this);
            emptyText.setText("Belum ada pengguna terdaftar");
            emptyText.setGravity(android.view.Gravity.CENTER);
            emptyText.setPadding(0, 100, 0, 0);
            emptyText.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            container.addView(emptyText);
        }
    }

    private void addUserView(LinearLayout container, UserModel user) {
        View itemView = getLayoutInflater().inflate(R.layout.item_admin_user, container, false);
        ((TextView) itemView.findViewById(R.id.tvUserName)).setText(user.getNama());
        ((TextView) itemView.findViewById(R.id.tvUserEmail)).setText(user.getEmail());
        
        TextView tvRole = itemView.findViewById(R.id.tvUserRoleTag);
        String roleTag = user.getRole().equals("ADMIN") ? "Admin" : "Pelaku UMKM";
        tvRole.setText(roleTag);
        
        if (user.getRole().equals("ADMIN")) {
            tvRole.setTextColor(Color.parseColor("#2196F3"));
            tvRole.setBackgroundResource(R.drawable.bg_tag_new);
            tvRole.getBackground().setTint(Color.parseColor("#E3F2FD"));
        } else {
            tvRole.setTextColor(ContextCompat.getColor(this, R.color.admin_primary));
            tvRole.setBackgroundResource(R.drawable.bg_tag_new);
        }
        
        itemView.setOnClickListener(v -> Toast.makeText(this, "Detail " + user.getNama(), Toast.LENGTH_SHORT).show());
        container.addView(itemView);
    }
}
