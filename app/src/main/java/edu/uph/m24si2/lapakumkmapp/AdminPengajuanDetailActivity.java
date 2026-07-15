package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPengajuanDetailActivity extends AppCompatActivity {

    private PengajuanModel pengajuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pengajuan_detail);

        pengajuan = (PengajuanModel) getIntent().getSerializableExtra("pengajuan");
        if (pengajuan == null) {
            finish();
            return;
        }

        setupViews();

        // Mark as Diproses if status is still Menunggu
        if (pengajuan.getStatus().equals("Menunggu")) {
            PengajuanManager.getInstance().updateStatus(this, pengajuan.getId(), "Diproses");
            NotificationManager.getInstance().addNotification("Aktivitas Admin", 
                "Anda sedang memproses pengajuan dari " + pengajuan.getNamaUmkm());
        }
    }

    private void setupViews() {
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

        ((TextView) findViewById(R.id.tvDetailNamaUmkm)).setText(pengajuan.getNamaUmkm());
        ((TextView) findViewById(R.id.tvDetailJenisUsaha)).setText(pengajuan.getJenisUsaha());
        
        setupInfo(R.id.infoPendaftar, "Nama Pendaftar", pengajuan.getUserName());
        setupInfo(R.id.infoEmail, "Email", pengajuan.getUserEmail());
        setupInfo(R.id.infoNIB, "NIB", pengajuan.getNib());

        ((TextView) findViewById(R.id.tvDetailNamaEvent)).setText(pengajuan.getNamaEvent());
        
        // Find RentalRequest for location and price
        RentalRequest rrDetail = null;
        for (RentalRequest r : RentalManager.getInstance().getRequests()) {
            if (r.getId().equals(pengajuan.getRentalRequestId())) {
                rrDetail = r;
                break;
            }
        }

        if (rrDetail != null) {
            ((TextView) findViewById(R.id.tvDetailLokasi)).setText(rrDetail.getEventLocation());
            ((TextView) findViewById(R.id.tvDetailHarga)).setText(rrDetail.getEventPrice());
        }

        ((TextView) findViewById(R.id.tvDetailDeskripsi)).setText(pengajuan.getDeskripsiUsaha());

        findViewById(R.id.btnViewKtp).setOnClickListener(v -> {
            Toast.makeText(this, "Membuka KTP: " + pengajuan.getKtpUri(), Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnViewNib).setOnClickListener(v -> {
            Toast.makeText(this, "Membuka NIB: " + pengajuan.getNibUri(), Toast.LENGTH_SHORT).show();
        });

        View bottomActions = findViewById(R.id.layoutBottomActions);
        if (!pengajuan.getStatus().equals("Menunggu") && !pengajuan.getStatus().equals("Diproses")) {
            bottomActions.setVisibility(View.GONE);
        }

        findViewById(R.id.btnRejectDetail).setOnClickListener(v -> {
            PengajuanManager.getInstance().updateStatus(this, pengajuan.getId(), "Ditolak");
            NotificationManager.getInstance().addNotification("Pengajuan Ditolak", 
                "Pengajuan dari " + pengajuan.getNamaUmkm() + " telah ditolak.");
            Toast.makeText(this, "Pengajuan Ditolak", Toast.LENGTH_SHORT).show();
            finish();
        });

        findViewById(R.id.btnApproveDetail).setOnClickListener(v -> {
            PengajuanManager.getInstance().updateStatus(this, pengajuan.getId(), "Disetujui");
            
            // Update RentalManager status
            String rentalId = pengajuan.getRentalRequestId();
            if (rentalId != null) {
                for (RentalRequest r : RentalManager.getInstance().getRequests()) {
                    if (r.getId().equals(rentalId)) {
                        // User side status update
                        r.setStatus(RentalRequest.Status.AKTIF);
                        RentalManager.getInstance().updateRequest(r);
                        
                        // Send Notification to User
                        NotificationManager.getInstance().addNotification("Pengajuan Disetujui", 
                            "Selamat! Pengajuan Anda untuk " + r.getEventName() + " telah disetujui.");
                        break;
                    }
                }
            }
            
            NotificationManager.getInstance().addNotification("Aktivitas Admin", 
                "Pengajuan dari " + pengajuan.getNamaUmkm() + " telah disetujui.");
            
            Toast.makeText(this, "Pengajuan Disetujui", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupInfo(int includeId, String label, String value) {
        View view = findViewById(includeId);
        ((TextView) view.findViewById(R.id.tvInfoLabel)).setText(label);
        ((TextView) view.findViewById(R.id.tvInfoValue)).setText(value != null ? value : "-");
    }
}
