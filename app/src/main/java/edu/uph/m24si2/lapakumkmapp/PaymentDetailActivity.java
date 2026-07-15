package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class PaymentDetailActivity extends AppCompatActivity {

    private TextView tvCountdown;
    private CountDownTimer countDownTimer;
    private SharedPreferences prefs;
    private RentalRequest currentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        prefs = getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE);
        tvCountdown = findViewById(R.id.tvCountdown);
        TextView tvMethodLabel = findViewById(R.id.tvMethodLabel);
        TextView tvVANumber = findViewById(R.id.tvVANumber);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        TextView tvCopyVA = findViewById(R.id.tvCopyVA);
        ImageView btnBack = findViewById(R.id.btnBackDetail);
        Button btnKonfirmasi = findViewById(R.id.btnKonfirmasiSelesai);
        TextView tvUbahMetode = findViewById(R.id.tvUbahMetode);

        currentRequest = (RentalRequest) getIntent().getSerializableExtra("rental_request");

        setupTimer();

        String method = getIntent().getStringExtra("PAYMENT_METHOD");
        String bank = getIntent().getStringExtra("BANK_NAME");

        if (method == null) method = "TRANSFER";
        if (bank == null) bank = "BCA";

        final String finalMethod = method;
        final String finalBank = bank;

        if ("TRANSFER".equals(method)) {
            tvMethodLabel.setText("Virtual Account " + bank);
            tvVANumber.setText("8830" + (int)(Math.random() * 9000 + 1000) + " 0812 3456 7890");
            tvInstructions.setText("1. Masukkan kartu ATM dan PIN\n2. Pilih menu Transfer -> Virtual Account\n3. Masukkan nomor Virtual Account di atas\n4. Konfirmasi data dan klik Bayar");
        } else {
            tvMethodLabel.setText("Pembayaran via " + method);
            tvVANumber.setText("0812-3456-7890");
            tvInstructions.setText("1. Buka aplikasi " + method + " Anda\n2. Pilih menu Bayar/Scan QRIS\n3. Masukkan nominal atau konfirmasi tagihan\n4. Masukkan PIN " + method + " Anda");
        }

        tvCopyVA.setOnClickListener(v -> {
            Toast.makeText(this, "nomor sudah disalin", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> handleBack());

        tvUbahMetode.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
            finish();
        });
        
        btnKonfirmasi.setOnClickListener(v -> {
            btnKonfirmasi.setEnabled(false);
            btnKonfirmasi.setText("Memverifikasi Pembayaran...");
            
            new Handler().postDelayed(() -> {
                Toast.makeText(this, "Pembayaran Berhasil Diverifikasi!", Toast.LENGTH_SHORT).show();
                
                // Update status to MENUNGGU_PERSETUJUAN (Case b)
                if (currentRequest != null) {
                    currentRequest.setStatus(RentalRequest.Status.MENUNGGU_PERSETUJUAN);
                }

                Intent intent = new Intent(this, PaymentSuccessActivity.class);
                intent.putExtra("PAYMENT_METHOD", finalMethod.equals("TRANSFER") ? finalBank : finalMethod);
                intent.putExtra("rental_request", currentRequest);
                startActivity(intent);
                finish();
            }, 3000);
        });
    }

    private void handleBack() {
        Toast.makeText(this, "pembayaran ditunda", Toast.LENGTH_SHORT).show();
        // Request remains MENUNGGU_PEMBAYARAN
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    private void setupTimer() {
        long currentTime = System.currentTimeMillis();
        long expiryTime = prefs.getLong("expiry_time", 0);
        
        if (expiryTime <= currentTime) {
            expiryTime = currentTime + (24L * 60 * 60 * 1000);
            prefs.edit().putLong("expiry_time", expiryTime).commit();
        }

        long remainingTime = expiryTime - currentTime;
        startTimer(remainingTime);
    }

    private void startTimer(long duration) {
        if (countDownTimer != null) countDownTimer.cancel();
        
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
                int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                String timeLeft = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                tvCountdown.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("00:00:00");
                Toast.makeText(PaymentDetailActivity.this, "Waktu Pembayaran Habis", Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTimer();
    }
}
