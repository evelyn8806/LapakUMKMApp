package edu.uph.m24si2.lapakumkmapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

public class AdminNavigationHelper {

    public static void setupNavigation(final Activity activity, int activeId) {
        View bottomNav = activity.findViewById(R.id.adminBottomNav);
        if (bottomNav == null) return;

        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminDashboard), AdminDashboardActivity.class, activeId == R.id.navAdminDashboard);
        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminPengajuan), AdminPengajuanActivity.class, activeId == R.id.navAdminPengajuan);
        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminPengguna), AdminPenggunaActivity.class, activeId == R.id.navAdminPengguna);
        setupNavItem(activity, bottomNav.findViewById(R.id.navAdminAkun), AccountActivity.class, activeId == R.id.navAdminAkun);
    }

    private static void setupNavItem(final Activity activity, View itemView, final Class<?> targetActivity, boolean isActive) {
        if (itemView == null) return;

        updateItemStyle(activity, itemView, isActive);

        itemView.setOnClickListener(v -> {
            if (activity.getClass() != targetActivity) {
                Intent intent = new Intent(activity, targetActivity);
                activity.startActivity(intent);
                if (!targetActivity.getSimpleName().equals("AccountActivity")) {
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
        } else if (itemView.getId() == R.id.navAdminAkun) {
            icon = itemView.findViewById(R.id.ivNavAkun);
            text = itemView.findViewById(R.id.tvNavAkun);
        }

        if (icon != null && text != null) {
            int activeColor = Color.parseColor("#7B61FF");
            int inactiveColor = ContextCompat.getColor(activity, R.color.text_gray);
            
            icon.setColorFilter(isActive ? activeColor : inactiveColor);
            text.setTextColor(isActive ? activeColor : inactiveColor);
        }
    }
}
