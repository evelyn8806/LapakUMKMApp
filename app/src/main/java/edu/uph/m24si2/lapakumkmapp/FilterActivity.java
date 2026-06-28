package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnReset;
    private RadioButton rbSemua, rbEvent, rbPasarMalam, rbBazar;
    private Spinner spinnerCity;
    private EditText etMinPrice, etMaxPrice;
    private Spinner spinnerSort;
    private Button btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initViews();
        setupCitySpinner();
        setupSortSpinner();
        setupPriceFormatting();

        btnBack.setOnClickListener(v -> finish());
        
        btnReset.setOnClickListener(v -> resetFilters());

        btnFilter.setOnClickListener(v -> applyFilter());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnReset = findViewById(R.id.btnReset);
        
        spinnerCity = findViewById(R.id.spinnerCity);

        rbSemua = findViewById(R.id.rbSemua);
        rbEvent = findViewById(R.id.rbEvent);
        rbPasarMalam = findViewById(R.id.rbPasarMalam);
        rbBazar = findViewById(R.id.rbBazar);
        
        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);
        
        spinnerSort = findViewById(R.id.spinnerSort);
        btnFilter = findViewById(R.id.btnFilter);
    }

    private void setupPriceFormatting() {
        etMinPrice.addTextChangedListener(new PriceTextWatcher(etMinPrice));
        etMaxPrice.addTextChangedListener(new PriceTextWatcher(etMaxPrice));
    }

    private class PriceTextWatcher implements TextWatcher {
        private final EditText editText;
        private String current = "";

        public PriceTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(current)) {
                editText.removeTextChangedListener(this);

                String cleanString = s.toString().replaceAll("[.]", "");
                if (!cleanString.isEmpty()) {
                    try {
                        double parsed = Double.parseDouble(cleanString);
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                        symbols.setGroupingSeparator('.');
                        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
                        String formatted = formatter.format(parsed);

                        current = formatted;
                        editText.setText(formatted);
                        editText.setSelection(formatted.length());
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                } else {
                    current = "";
                    editText.setText("");
                }

                editText.addTextChangedListener(this);
            }
        }
    }

    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
    }

    private void resetFilters() {
        spinnerCity.setSelection(0);
        rbSemua.setChecked(true);
        etMinPrice.setText("");
        etMaxPrice.setText("");
        spinnerSort.setSelection(0);
    }

    private void applyFilter() {
        String jenis = "Semua";
        if (rbEvent.isChecked()) jenis = "Event";
        else if (rbPasarMalam.isChecked()) jenis = "Pasar Malam";
        else if (rbBazar.isChecked()) jenis = "Bazar";

        float minPrice = 0;
        float maxPrice = 2000000;

        try {
            String minStr = etMinPrice.getText().toString().replace(".", "");
            if (!minStr.isEmpty()) minPrice = Float.parseFloat(minStr);
            
            String maxStr = etMaxPrice.getText().toString().replace(".", "");
            if (!maxStr.isEmpty()) maxPrice = Float.parseFloat(maxStr);
        } catch (NumberFormatException e) {
            // Use default values
        }

        String lokasi = spinnerCity.getSelectedItem().toString();
        String sort = spinnerSort.getSelectedItem().toString();

        Intent intent = new Intent(this, RecommendationListActivity.class);
        intent.putExtra("jenis", jenis);
        intent.putExtra("minPrice", minPrice);
        intent.putExtra("maxPrice", maxPrice);
        intent.putExtra("lokasi", lokasi);
        intent.putExtra("sort", sort);
        startActivity(intent);
    }
}
