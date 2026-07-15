package edu.uph.m24si2.lapakumkmapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

public class UmkmNavigationHelper {

    public static void setupNavigation(final Activity activity, int activeId) {
        View bottomNav = activity.findViewById(R.id.umkmBottomNavCard);
        if (bottomNav == null) return;

        setupNavItem(activity, bottomNav.findViewById(R.id.navBeranda), DashboardActivity.class, activeId == R.id.navBeranda);
        setupNavItem(activity, bottomNav.findViewById(R.id.navEksplorasi), ExplorationMapActivity.class, activeId == R.id.navEksplorasi);
        setupNavItem(activity, bottomNav.findViewById(R.id.navAkun), AccountActivity.class, activeId == R.id.navAkun);
    }

    private static void setupNavItem(final Activity activity, View itemView, final Class<?> targetActivity, boolean isActive) {
        if (itemView == null) return;

        updateItemStyle(activity, itemView, isActive);

        itemView.setOnClickListener(v -> {
            if (activity.getClass() != targetActivity) {
                Intent intent = new Intent(activity, targetActivity);
                activity.startActivity(intent);
                // For main tabs, we might want to finish the current activity to avoid deep stacks, 
                // but usually it's better to manage task flags or just let it be.
            }
        });
    }

    private static void updateItemStyle(Activity activity, View itemView, boolean isActive) {
        ImageView icon = null;
        TextView text = null;

        if (itemView.getId() == R.id.navBeranda) {
            icon = itemView.findViewById(R.id.ivNavBeranda);
            text = itemView.findViewById(R.id.tvNavBeranda);
        } else if (itemView.getId() == R.id.navEksplorasi) {
            icon = itemView.findViewById(R.id.ivNavEksplorasi);
            text = itemView.findViewById(R.id.tvNavEksplorasi);
        } else if (itemView.getId() == R.id.navAkun) {
            icon = itemView.findViewById(R.id.ivNavAkun);
            text = itemView.findViewById(R.id.tvNavAkun);
        }

        if (icon != null && text != null) {
            int activeColor = ContextCompat.getColor(activity, R.color.blue_primary);
            int inactiveColor = ContextCompat.getColor(activity, R.color.text_gray);
            
            icon.setColorFilter(isActive ? activeColor : inactiveColor);
            text.setTextColor(isActive ? activeColor : inactiveColor);
        }
    }
}
