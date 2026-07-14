package edu.uph.m24si2.lapakumkmapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class AdminNavigationHelper {

    public static void setupNavigation(final Activity activity, int activeId) {
        View bottomNav = activity.findViewById(R.id.adminBottomNav);
        if (bottomNav == null) return;

        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminDashboard), AdminDashboardActivity.class, activeId == R.id.navAdminDashboard);
        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminPengajuan), AdminPengajuanActivity.class, activeId == R.id.navAdminPengajuan);
        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminPengguna), AdminPenggunaActivity.class, activeId == R.id.navAdminPengguna);
        
        // Laporan - Placeholder
        bottomNav.findViewById(R.id.navAdminLaporan).setOnClickListener(v -> 
            Toast.makeText(activity, "Fitur Laporan akan segera hadir", Toast.LENGTH_SHORT).show()
        );
        if (activeId == R.id.navAdminLaporan) {
            updateItemStyle(activity, bottomNav.findViewById(R.id.navAdminLaporan), true);
        }

        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminAkun), AccountActivity.class, activeId == R.id.navAdminAkun);
    }

    private static void setupNavItem(final Activity activity, View itemView, final Class<?> targetActivity, boolean isActive) {
        if (itemView == null) return;

        updateItemStyle(activity, itemView, isActive);

        itemView.setOnClickListener(v -> {
            if (activity.getClass() != targetActivity) {
                Intent intent = new Intent(activity, targetActivity);
                activity.startActivity(intent);
                if (!(activity instanceof AccountActivity)) {
                    activity.finish();
                }
            }
        });
    }

    private static void updateItemStyle(Activity activity, View itemView, boolean isActive) {
        ImageView icon = null;
        TextView text = null;

        if (itemView.getId() == R.id.navAdminDashboard) {
            icon = itemView.findViewById(R.id.ivNavDashboard);
            text = itemView.findViewById(R.id.tvNavDashboard);
        } else if (itemView.getId() == R.id.navAdminPengajuan) {
            icon = itemView.findViewById(R.id.ivNavPengajuan);
            text = itemView.findViewById(R.id.tvNavPengajuan);
        } else if (itemView.getId() == R.id.navAdminPengguna) {
            icon = itemView.findViewById(R.id.ivNavPengguna);
            text = itemView.findViewById(R.id.tvNavPengguna);
        } else if (itemView.getId() == R.id.navAdminLaporan) {
            icon = itemView.findViewById(R.id.ivNavLaporan);
            text = itemView.findViewById(R.id.tvNavLaporan);
        } else if (itemView.getId() == R.id.navAdminAkun) {
            icon = itemView.findViewById(R.id.ivNavAkun);
            text = itemView.findViewById(R.id.tvNavAkun);
        }

        if (icon != null && text != null) {
            int color = isActive ? ContextCompat.getColor(activity, R.color.admin_primary) : ContextCompat.getColor(activity, R.color.text_gray);
            icon.setColorFilter(color);
            text.setTextColor(color);
        }
    }
}
