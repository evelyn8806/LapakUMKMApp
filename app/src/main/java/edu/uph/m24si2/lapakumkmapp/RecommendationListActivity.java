package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class RecommendationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_list);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Mendapatkan data filter dari Intent
        String jenis = getIntent().getStringExtra("jenis");
        float minPrice = getIntent().getFloatExtra("minPrice", 0);
        float maxPrice = getIntent().getFloatExtra("maxPrice", 2000000);
        String ukuran = getIntent().getStringExtra("ukuran");
        String sort = getIntent().getStringExtra("sort");

        // Di sini Anda bisa menambahkan logika untuk memfilter data UMKM berdasarkan nilai-nilai di atas
    }
}
