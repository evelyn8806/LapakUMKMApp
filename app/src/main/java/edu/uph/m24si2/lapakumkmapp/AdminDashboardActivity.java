package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        View btnLogout = findViewById(R.id.btnLogoutAdmin);
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        loadApplications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApplications();
    }

    private void loadApplications() {
        LinearLayout container = findViewById(R.id.layoutAdminList);
        if (container == null) return;
        
        container.removeAllViews();
        List<PengajuanModel> list = DataRepository.getDaftarPengajuan();

        if (list.isEmpty()) {
            TextView emptyTv = new TextView(this);
            emptyTv.setText("Belum ada pengajuan masuk.");
            emptyTv.setGravity(android.view.Gravity.CENTER);
            emptyTv.setPadding(0, 50, 0, 0);
            container.addView(emptyTv);
            return;
        }

        for (PengajuanModel p : list) {
            View itemView = getLayoutInflater().inflate(R.layout.item_admin_request, container, false);
            
            TextView tvUsaha = itemView.findViewById(R.id.tvAdminUsahaName);
            TextView tvEvent = itemView.findViewById(R.id.tvAdminEventName);
            TextView tvStatus = itemView.findViewById(R.id.tvAdminStatus);
            android.widget.Button btnApprove = itemView.findViewById(R.id.btnApprove);
            android.widget.Button btnReject = itemView.findViewById(R.id.btnReject);

            tvUsaha.setText(p.getNamaUsaha());
            tvEvent.setText("Event: " + p.getNamaEvent());
            tvStatus.setText(p.getStatus());

            if (!p.getStatus().equals("PENDING")) {
                btnApprove.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }

            btnApprove.setOnClickListener(v -> {
                DataRepository.updateStatus(p.getId(), "APPROVED");
                Toast.makeText(this, "Disetujui!", Toast.LENGTH_SHORT).show();
                loadApplications();
            });

            btnReject.setOnClickListener(v -> {
                DataRepository.updateStatus(p.getId(), "REJECTED");
                Toast.makeText(this, "Ditolak", Toast.LENGTH_SHORT).show();
                loadApplications();
            });

            container.addView(itemView);
        }
    }
}