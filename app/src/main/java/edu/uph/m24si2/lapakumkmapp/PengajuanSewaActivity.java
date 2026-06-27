package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PengajuanSewaActivity extends AppCompatActivity {

    private LinearLayout layoutStep1, layoutStep2, layoutStep3;
    private TextView tvStep1Circle, tvStep2Circle, tvStep3Circle;
    private EditText etNamaUsaha, etNIB, etAlamatUsaha, etDeskripsiUsaha;
    private Spinner spinnerJenisUsaha;
    private Button btnNextStep2, btnNextStep3, btnSubmit;
    private TextView tvReviewNama, tvReviewJenis, tvReviewNIB, tvReviewAlamat;

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
        
        tvReviewNama = findViewById(R.id.tvReviewNama);
        tvReviewJenis = findViewById(R.id.tvReviewJenis);
        tvReviewNIB = findViewById(R.id.tvReviewNIB);
        tvReviewAlamat = findViewById(R.id.tvReviewAlamat);

        // Setup Spinner
        String[] jenisUsaha = {"Makanan", "Minuman", "Pakaian", "Kerajinan", "Lainnya"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisUsaha);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisUsaha.setAdapter(adapter);

        findViewById(R.id.btnBackPengajuan).setOnClickListener(v -> finish());

        // Navigation
        btnNextStep2.setOnClickListener(v -> {
            if (etNamaUsaha.getText().toString().isEmpty()) {
                Toast.makeText(this, "Nama Usaha harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            showStep(2);
        });

        btnNextStep3.setOnClickListener(v -> {
            // Fill review data
            tvReviewNama.setText(etNamaUsaha.getText().toString());
            tvReviewJenis.setText(spinnerJenisUsaha.getSelectedItem().toString());
            tvReviewNIB.setText(etNIB.getText().toString());
            tvReviewAlamat.setText(etAlamatUsaha.getText().toString());
            
            showStep(3);
        });

        btnSubmit.setOnClickListener(v -> {
            Toast.makeText(this, "Pengajuan Berhasil Dikirim!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PengajuanSewaActivity.this, PaymentActivity.class);
            startActivity(intent);
            finish();
        });
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
