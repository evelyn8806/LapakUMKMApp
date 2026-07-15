package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        TextView tvMethod = findViewById(R.id.tvSuccessMethod);
        TextView tvTime = findViewById(R.id.tvSuccessTime);
        TextView tvTrx = findViewById(R.id.tvSuccessTrx);
        Button btnLihatStatus = findViewById(R.id.btnLihatStatus);
        ImageView btnBack = findViewById(R.id.btnBackSuccess);

        String method = getIntent().getStringExtra("PAYMENT_METHOD");
        tvMethod.setText(method);

        tvTrx.setText("TRX-" + System.currentTimeMillis() / 10000);

        getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("is_paid", true)
                .remove("expiry_time")
                .apply();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        tvTime.setText(sdf.format(new Date()) + " WIB");

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnLihatStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatusPengajuanActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
