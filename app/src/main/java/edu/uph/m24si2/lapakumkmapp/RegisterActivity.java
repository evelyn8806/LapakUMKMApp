package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegNama, etRegEmail, etRegPassword, etRegConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegNama = findViewById(R.id.etRegNama);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etRegNama.getText().toString();
                String email = etRegEmail.getText().toString();
                String password = etRegPassword.getText().toString();
                String confirmPassword = etRegConfirmPassword.getText().toString();

                if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password tidak cocok!", Toast.LENGTH_SHORT).show();
                } else {
                    // Simpan data ke SharedPreferences agar bisa login
                    SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putString("nama", nama);
                    editor.apply();

                    Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke halaman Login
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke MainActivity
            }
        });
    }
}
