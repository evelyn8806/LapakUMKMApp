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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        prefs = getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE);
        tvCountdown = findViewById(R.id.tvCountdown);
        TextView tvMethodLabel = findViewById(R.id.tvMethodLabel);
        TextView tvVANumber = findViewById(R.id.tvVANumber);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        ImageView btnBack = findViewById(R.id.btnBackDetail);
        Button btnKonfirmasi = findViewById(R.id.btnKonfirmasiSelesai);
        TextView tvUbahMetode = findViewById(R.id.tvUbahMetode);

        setupTimer();

        String method = getIntent().getStringExtra("PAYMENT_METHOD");
        String bank = getIntent().getStringExtra("BANK_NAME");

        // Default values if opened from Dashboard
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

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        tvUbahMetode.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
            finish();
        });
        
        btnKonfirmasi.setOnClickListener(v -> {
            // Simulasi Verifikasi Pembayaran
            btnKonfirmasi.setEnabled(false);
            btnKonfirmasi.setText("Memverifikasi Pembayaran...");
            
            new Handler().postDelayed(() -> {
                Toast.makeText(this, "Pembayaran Berhasil Diverifikasi!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PaymentSuccessActivity.class);
                intent.putExtra("PAYMENT_METHOD", finalMethod.equals("TRANSFER") ? finalBank : finalMethod);
                startActivity(intent);
                finish();
            }, 3000); // Delay 3 detik seolah-olah cek ke server bank
        });
    }

    private void setupTimer() {
        long expiryTime = prefs.getLong("expiry_time", 0);
        
        // Jika belum ada waktu expiry, set 24 jam dari sekarang
        if (expiryTime == 0) {
            expiryTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
            prefs.edit().putLong("expiry_time", expiryTime).apply();
        }

        long currentTime = System.currentTimeMillis();
        long remainingTime = expiryTime - currentTime;

        if (remainingTime > 0) {
            startTimer(remainingTime);
        } else {
            tvCountdown.setText("00:00:00");
            Toast.makeText(this, "Waktu Pembayaran Habis", Toast.LENGTH_LONG).show();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
