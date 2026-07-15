package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        rvNotifications = findViewById(R.id.rvNotifications);
        tvEmpty = findViewById(R.id.tvEmptyNotif);
        
        findViewById(R.id.btnBackNotif).setOnClickListener(v -> finish());

        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        loadNotifications();
        
        setupBottomNav();
    }

    private void loadNotifications() {
        List<NotificationModel> notifs = NotificationManager.getInstance().getNotifications();
        if (notifs.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
            rvNotifications.setAdapter(new NotificationAdapter(notifs));
        }
    }

    private void setupBottomNav() {
        String role = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("role", "UMKM");
        
        if ("ADMIN".equals(role)) {
            findViewById(R.id.umkmBottomNav).setVisibility(View.GONE);
            findViewById(R.id.adminBottomNav).setVisibility(View.VISIBLE);
            AdminNavigationHelper.setupNavigation(this, -1); // No active item in standard admin nav for notifications
        } else {
            findViewById(R.id.umkmBottomNav).setVisibility(View.VISIBLE);
            findViewById(R.id.adminBottomNav).setVisibility(View.GONE);
            UmkmNavigationHelper.setupNavigation(this, -1);
        }
    }
}
