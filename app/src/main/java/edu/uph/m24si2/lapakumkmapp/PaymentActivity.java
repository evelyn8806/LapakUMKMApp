package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnBayar = findViewById(R.id.btnBayar);
        RadioGroup radioGroup = findViewById(R.id.radioPayment);
        LinearLayout layoutBankOptions = findViewById(R.id.layoutBankOptions);
        Spinner spinnerBank = findViewById(R.id.spinnerBank);

        // Setup Spinner Bank
        String[] banks = {"BCA", "BNI", "BRI", "Mandiri"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, banks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(adapter);

        // Show/Hide Bank Options based on selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbTransfer) {
                layoutBankOptions.setVisibility(View.VISIBLE);
            } else {
                layoutBankOptions.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnBayar.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, PaymentDetailActivity.class);
            
            if (selectedId == R.id.rbTransfer) {
                intent.putExtra("PAYMENT_METHOD", "TRANSFER");
                intent.putExtra("BANK_NAME", spinnerBank.getSelectedItem().toString());
            } else {
                RadioButton rb = findViewById(selectedId);
                intent.putExtra("PAYMENT_METHOD", rb.getText().toString());
            }
            
            startActivity(intent);
        });
    }
}
