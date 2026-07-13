package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PengajuanSewaActivity extends AppCompatActivity {

    private static final int PICK_KTP_REQUEST = 1;
    private static final int PICK_NIB_REQUEST = 2;

    private LinearLayout layoutStep1, layoutStep2, layoutStep3;
    private TextView tvStep1Circle, tvStep2Circle, tvStep3Circle;
    private EditText etNamaUsaha, etNIB, etAlamatUsaha, etDeskripsiUsaha;
    private Spinner spinnerJenisUsaha;
    private Button btnNextStep2, btnNextStep3, btnSubmit;
    private Button btnPilihKTP, btnPilihNIB;
    private ImageView ivDeleteKTP, ivDeleteNIB;
    private TextView tvFileNameKTP, tvFileNameNIB;
    private TextView tvReviewNamaEvent, tvReviewLokasi, tvReviewTotalBiaya;
    private CheckBox cbAgreement;

    private Uri ktpUri = null;
    private Uri nibUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_sewa);

        // Bind views
        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        layoutStep3 = findViewById(R.id.layoutStep3);
        
        tvStep1Circle = findViewById(R.id.tvStep1Circle);
        tvStep2Circle = findViewById(R.id.tvStep2Circle);
        tvStep3Circle = findViewById(R.id.tvStep3Circle);
        
        etNamaUsaha = findViewById(R.id.etNamaUsaha);
        etNIB = findViewById(R.id.etNIB);
        etAlamatUsaha = findViewById(R.id.etAlamatUsaha);
        etDeskripsiUsaha = findViewById(R.id.etDeskripsiUsaha);
        spinnerJenisUsaha = findViewById(R.id.spinnerJenisUsaha);
        
        btnNextStep2 = findViewById(R.id.btnNextToStep2);
        btnNextStep3 = findViewById(R.id.btnNextToStep3);
        btnSubmit = findViewById(R.id.btnSubmitPengajuan);

        btnPilihKTP = findViewById(R.id.btnPilihKTP);
        btnPilihNIB = findViewById(R.id.btnPilihNIB);
        ivDeleteKTP = findViewById(R.id.ivDeleteKTP);
        ivDeleteNIB = findViewById(R.id.ivDeleteNIB);
        tvFileNameKTP = findViewById(R.id.tvFileNameKTP);
        tvFileNameNIB = findViewById(R.id.tvFileNameNIB);
        
        tvReviewNamaEvent = findViewById(R.id.tvReviewNamaEvent);
        tvReviewLokasi = findViewById(R.id.tvReviewLokasi);
        tvReviewTotalBiaya = findViewById(R.id.tvReviewTotalBiaya);
        cbAgreement = findViewById(R.id.cbAgreement);

        // Setup Spinner
        String[] jenisUsaha = {"Pilih Jenis Usaha", "Makanan", "Minuman", "Pakaian", "Kerajinan", "Lainnya"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisUsaha);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisUsaha.setAdapter(adapter);

        findViewById(R.id.btnBackPengajuan).setOnClickListener(v -> finish());

        // File Selection
        btnPilihKTP.setOnClickListener(v -> openFilePicker(PICK_KTP_REQUEST));
        btnPilihNIB.setOnClickListener(v -> openFilePicker(PICK_NIB_REQUEST));

        ivDeleteKTP.setOnClickListener(v -> {
            ktpUri = null;
            tvFileNameKTP.setText("Tidak ada file");
            ivDeleteKTP.setVisibility(View.GONE);
        });

        ivDeleteNIB.setOnClickListener(v -> {
            nibUri = null;
            tvFileNameNIB.setText("Tidak ada file");
            ivDeleteNIB.setVisibility(View.GONE);
        });

        // Navigation
        btnNextStep2.setOnClickListener(v -> {
            if (etNamaUsaha.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Nama Usaha wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spinnerJenisUsaha.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Pilih Jenis Usaha terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etDeskripsiUsaha.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Deskripsi Usaha wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            showStep(2);
        });

        btnNextStep3.setOnClickListener(v -> {
            if (ktpUri == null) {
                Toast.makeText(this, "File KTP wajib diunggah", Toast.LENGTH_SHORT).show();
                return;
            }

            // Fill review data from Intent
            String eventName = getIntent().getStringExtra("nama_event");
            String eventLocation = getIntent().getStringExtra("lokasi_event");
            
            if (eventName != null) tvReviewNamaEvent.setText(eventName);
            if (eventLocation != null) tvReviewLokasi.setText(eventLocation);

            showStep(3);
        });

        cbAgreement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnSubmit.setEnabled(isChecked);
        });

        btnSubmit.setOnClickListener(v -> {
            // Simpan data pengajuan ke Manager (Database Lokal)
            String namaUmkm = etNamaUsaha.getText().toString();
            String namaEvent = tvReviewNamaEvent.getText().toString();
            String tanggal = "01 Jan 2025"; // Bisa ambil dari DatePicker

            PengajuanManager.getInstance().tambahPengajuan(namaUmkm, namaEvent, tanggal);

            // Reset timer pembayaran untuk pengajuan baru
            getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE)
                    .edit()
                    .remove("expiry_time")
                    .commit();

            Toast.makeText(this, "Pengajuan Berhasil Dikirim!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, PaymentDetailActivity.class);
            intent.putExtra("PAYMENT_METHOD", "TRANSFER");
            intent.putExtra("BANK_NAME", "BCA");
            startActivity(intent);
            finish();
        });
    }

    private void openFilePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedFileUri = data.getData();
            String fileName = "File terpilih";
            
            if (requestCode == PICK_KTP_REQUEST) {
                ktpUri = selectedFileUri;
                tvFileNameKTP.setText("KTP: " + fileName);
                ivDeleteKTP.setVisibility(View.VISIBLE);
            } else if (requestCode == PICK_NIB_REQUEST) {
                nibUri = selectedFileUri;
                tvFileNameNIB.setText("NIB: " + fileName);
                ivDeleteNIB.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showStep(int step) {
        layoutStep1.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        layoutStep2.setVisibility(step == 2 ? View.VISIBLE : View.GONE);
        layoutStep3.setVisibility(step == 3 ? View.VISIBLE : View.GONE);

        // Update indicator colors
        tvStep1Circle.setBackgroundResource(step >= 1 ? R.drawable.bg_circle_blue : R.drawable.bg_circle_gray);
        tvStep1Circle.setTextColor(step >= 1 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.text_gray));
        
        tvStep2Circle.setBackgroundResource(step >= 2 ? R.drawable.bg_circle_blue : R.drawable.bg_circle_gray);
        tvStep2Circle.setTextColor(step >= 2 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.text_gray));
        
        tvStep3Circle.setBackgroundResource(step >= 3 ? R.drawable.bg_circle_blue : R.drawable.bg_circle_gray);
        tvStep3Circle.setTextColor(step >= 3 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.text_gray));
    }
}
