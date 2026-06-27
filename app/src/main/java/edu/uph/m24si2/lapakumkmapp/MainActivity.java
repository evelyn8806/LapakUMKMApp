package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek jika user memilih "Selalu Login" sebelumnya
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPref.getBoolean("isLoggedIn", false);
        if (isRemembered) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = etEmail.getText().toString();
                String passwordInput = etPassword.getText().toString();

                // Ambil data yang tersimpan di SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String savedEmail = sharedPref.getString("email", "admin@umkm.com");
                String savedPassword = sharedPref.getString("password", "password123");

                // Validasi Login (Menggunakan data register atau default)
                if (emailInput.equals(savedEmail) && passwordInput.equals(savedPassword)) {
                    Toast.makeText(MainActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                    
                    // Simpan status "Selalu Login" jika dicentang
                    if (cbRememberMe.isChecked()) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                    }

                    // Pindah ke Dashboard
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}