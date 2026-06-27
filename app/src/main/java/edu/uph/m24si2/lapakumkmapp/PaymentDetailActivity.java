package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        TextView tvMethodLabel = findViewById(R.id.tvMethodLabel);
        TextView tvVANumber = findViewById(R.id.tvVANumber);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        ImageView btnBack = findViewById(R.id.btnBackDetail);
        Button btnKonfirmasi = findViewById(R.id.btnKonfirmasiSelesai);

        String method = getIntent().getStringExtra("PAYMENT_METHOD");
        String bank = getIntent().getStringExtra("BANK_NAME");

        if ("TRANSFER".equals(method)) {
            tvMethodLabel.setText("Virtual Account " + bank);
            tvVANumber.setText("8830" + (int)(Math.random() * 9000 + 1000) + " 0812 3456 7890");
            tvInstructions.setText("1. Masukkan kartu ATM dan PIN\n2. Pilih menu Transfer -> Virtual Account\n3. Masukkan nomor Virtual Account di atas\n4. Konfirmasi data dan klik Bayar");
        } else {
            tvMethodLabel.setText("Pembayaran via " + method);
            tvVANumber.setText("0812-3456-7890");
            tvInstructions.setText("1. Buka aplikasi " + method + " Anda\n2. Pilih menu Bayar/Scan QRIS\n3. Masukkan nominal atau konfirmasi tagihan\n4. Masukkan PIN " + method + " Anda");
        }

        btnBack.setOnClickListener(v -> finish());
        btnKonfirmasi.setOnClickListener(v -> {
            Toast.makeText(this, "Pembayaran Sedang Diverifikasi!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
