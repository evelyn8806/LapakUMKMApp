package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.slider.RangeSlider;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnReset;
    private RadioButton rbSemua, rbPermanen, rbEvent, rbPasarMalam, rbFestival, rbBazar;
    private RangeSlider priceSlider;
    private Button btnSize2x2, btnSize3x3, btnSize3x4, btnSize4x4, btnSize5x5;
    private Spinner spinnerSort;
    private Button btnFilter;
    
    private String selectedSize = "Semua";
    private Button[] sizeButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initViews();
        setupSizeButtons();
        setupSortSpinner();

        btnBack.setOnClickListener(v -> finish());
        
        btnReset.setOnClickListener(v -> resetFilters());

        btnFilter.setOnClickListener(v -> applyFilter());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnReset = findViewById(R.id.btnReset);
        
        rbSemua = findViewById(R.id.rbSemua);
        rbPermanen = findViewById(R.id.rbPermanen);
        rbEvent = findViewById(R.id.rbEvent);
        rbPasarMalam = findViewById(R.id.rbPasarMalam);
        rbFestival = findViewById(R.id.rbFestival);
        rbBazar = findViewById(R.id.rbBazar);
        
        priceSlider = findViewById(R.id.priceSlider);
        
        btnSize2x2 = findViewById(R.id.btnSize2x2);
        btnSize3x3 = findViewById(R.id.btnSize3x3);
        btnSize3x4 = findViewById(R.id.btnSize3x4);
        btnSize4x4 = findViewById(R.id.btnSize4x4);
        btnSize5x5 = findViewById(R.id.btnSize5x5);
        
        sizeButtons = new Button[]{btnSize2x2, btnSize3x3, btnSize3x4, btnSize4x4, btnSize5x5};
        
        spinnerSort = findViewById(R.id.spinnerSort);
        btnFilter = findViewById(R.id.btnFilter);
    }

    private void setupSizeButtons() {
        for (Button btn : sizeButtons) {
            btn.setOnClickListener(v -> {
                selectedSize = btn.getText().toString();
                updateSizeButtonsUI(btn);
            });
        }
    }

    private void updateSizeButtonsUI(Button selectedBtn) {
        int blue = ContextCompat.getColor(this, R.color.blue_primary);
        int gray = ContextCompat.getColor(this, R.color.text_gray);
        int white = ContextCompat.getColor(this, R.color.white);

        for (Button btn : sizeButtons) {
            if (btn == selectedBtn) {
                btn.setBackgroundTintList(ColorStateList.valueOf(blue));
                btn.setTextColor(white);
            } else {
                btn.setBackgroundTintList(ColorStateList.valueOf(white));
                btn.setTextColor(gray);
            }
        }
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
    }

    private void resetFilters() {
        rbSemua.setChecked(true);
        priceSlider.setValues(0f, 2000000f);
        selectedSize = "Semua";
        for (Button btn : sizeButtons) {
            btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            btn.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        }
        spinnerSort.setSelection(0);
    }

    private void applyFilter() {
        String jenis = "Semua";
        if (rbPermanen.isChecked()) jenis = "Permanen";
        else if (rbEvent.isChecked()) jenis = "Event";
        else if (rbPasarMalam.isChecked()) jenis = "Pasar Malam";
        else if (rbFestival.isChecked()) jenis = "Festival";
        else if (rbBazar.isChecked()) jenis = "Bazar";

        List<Float> priceValues = priceSlider.getValues();
        float minPrice = priceValues.get(0);
        float maxPrice = priceValues.get(1);

        String sort = spinnerSort.getSelectedItem().toString();

        Intent intent = new Intent(this, RecommendationListActivity.class);
        intent.putExtra("jenis", jenis);
        intent.putExtra("minPrice", minPrice);
        intent.putExtra("maxPrice", maxPrice);
        intent.putExtra("ukuran", selectedSize);
        intent.putExtra("sort", sort);
        startActivity(intent);
    }
}
