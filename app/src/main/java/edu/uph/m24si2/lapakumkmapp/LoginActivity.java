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

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        role = getIntent().getStringExtra("ROLE");
        TextView tvSubtitle = findViewById(R.id.tvLoginSubtitle);
        if (role != null) {
            String roleName = role.equals("UMKM") ? "UMKM" : "Admin";
            tvSubtitle.setText(getString(R.string.masuk_sebagai, roleName));
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);
        final CheckBox cbRememberMe = findViewById(R.id.cbRememberMe);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = etEmail.getText().toString();
                String passwordInput = etPassword.getText().toString();

                if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Harap isi email dan password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cek kredensial di UserManager
                UserModel foundUser = UserManager.getInstance().login(emailInput, passwordInput);

                // Fallback ke SharedPreferences jika tidak ditemukan di UserManager (untuk mendukung persistence user terakhir yang mendaftar)
                if (foundUser == null) {
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String savedEmail = prefs.getString("email", "");
                    String savedPassword = prefs.getString("password", "");
                    
                    if (!savedEmail.isEmpty() && savedEmail.equalsIgnoreCase(emailInput) && savedPassword.equals(passwordInput)) {
                        foundUser = new UserModel(
                            prefs.getString("nama", ""),
                            savedEmail,
                            prefs.getString("phone", ""),
                            savedPassword,
                            prefs.getString("role", "UMKM")
                        );
                        // Tambahkan kembali ke UserManager agar tersedia selama aplikasi berjalan
                        UserManager.getInstance().addUser(foundUser);
                    }
                }

                if (foundUser != null) {
                    Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                    
                    SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", foundUser.getEmail());
                    editor.putString("nama", foundUser.getNama());
                    editor.putString("role", foundUser.getRole());
                    
                    // Update status "Ingat Saya"
                    editor.putBoolean("isLoggedIn", cbRememberMe.isChecked());
                    editor.apply();

                    if (foundUser.getRole().equals("ADMIN")) {
                        Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("ROLE", role);
                startActivity(intent);
            }
        });
    }
}
