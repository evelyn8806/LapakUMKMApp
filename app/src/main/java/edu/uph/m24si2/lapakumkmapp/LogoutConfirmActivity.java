package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LogoutConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_confirm);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancelLogout).setOnClickListener(v -> finish());

        findViewById(R.id.btnConfirmLogout).setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            // Hanya hapus status login otomatis (Remember Me), jangan hapus data registrasi
            sharedPref.edit().putBoolean("isLoggedIn", false).apply();
            
            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LogoutConfirmActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
