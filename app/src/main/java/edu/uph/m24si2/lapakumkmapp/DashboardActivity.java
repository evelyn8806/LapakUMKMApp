package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private MaterialCardView cardPendingPayment;
    private Button btnExploreNow, btnBayarSekarang;
    private TextView tvRekomendasiHeader, tvNoResults, tvPendingEventName;
    private ImageView ivPendingEventImage;
    private NestedScrollView nestedScrollView;
    private EditText etSearch;
    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private List<EventModel> allEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardPendingPayment = findViewById(R.id.cardPendingPayment);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        btnBayarSekarang = findViewById(R.id.btnBayarSekarang);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etSearch = findViewById(R.id.etSearch);
        tvNoResults = findViewById(R.id.tvNoResults);
        rvEvents = findViewById(R.id.rvEvents);
        tvPendingEventName = findViewById(R.id.tvPendingEventName);
        ivPendingEventImage = findViewById(R.id.ivPendingEventImage);

        refreshPendingPaymentCard();

        btnBayarSekarang.setOnClickListener(v -> {
            List<RentalRequest> pending = RentalManager.getInstance().getRequestsByStatus(RentalRequest.Status.MENUNGGU_PEMBAYARAN);
            if (!pending.isEmpty()) {
                Intent intent = new Intent(DashboardActivity.this, PaymentDetailActivity.class);
                intent.putExtra("PAYMENT_METHOD", "TRANSFER");
                intent.putExtra("BANK_NAME", "BCA");
                intent.putExtra("rental_request", pending.get(0));
                startActivity(intent);
            }
        });

        allEventsList = EventManager.getAllEvents();
        eventAdapter = new EventAdapter(new ArrayList<>(allEventsList), this);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(eventAdapter);

        findViewById(R.id.menuMaps).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ExplorationMapActivity.class));
        });

        findViewById(R.id.menuFilter).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, FilterActivity.class));
        });

        findViewById(R.id.menuPengajuan).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RentalListActivity.class);
            intent.putExtra("IS_HISTORY", false);
            startActivity(intent);
        });

        findViewById(R.id.menuHistory).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RentalListActivity.class);
            intent.putExtra("IS_HISTORY", true);
            startActivity(intent);
        });

        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ExplorationMapActivity.class));
        });

        findViewById(R.id.navLapak).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, MyStallsActivity.class));
        });

        findViewById(R.id.navNotif).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, NotificationsActivity.class));
        });
        
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBySearch(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnExploreNow.setOnClickListener(v -> {
            int y = ((View) tvRekomendasiHeader.getParent()).getTop();
            nestedScrollView.smoothScrollTo(0, y);
        });
    }

    private void refreshPendingPaymentCard() {
        List<RentalRequest> pending = RentalManager.getInstance().getRequestsByStatus(RentalRequest.Status.MENUNGGU_PEMBAYARAN);
        if (!pending.isEmpty()) {
            RentalRequest r = pending.get(0);
            cardPendingPayment.setVisibility(View.VISIBLE);
            if (tvPendingEventName != null) tvPendingEventName.setText(r.getEventName());
            if (ivPendingEventImage != null) ivPendingEventImage.setImageResource(r.getEventImage());
        } else {
            cardPendingPayment.setVisibility(View.GONE);
        }
    }

    private void filterBySearch(String query) {
        List<EventModel> filteredList = new ArrayList<>();
        for (EventModel event : allEventsList) {
            if (event.getNama().toLowerCase().contains(query) || 
                event.getKategori().toLowerCase().contains(query) ||
                event.getLokasi().toLowerCase().contains(query)) {
                filteredList.add(event);
            }
        }
        eventAdapter.updateList(filteredList);
        tvNoResults.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        rvEvents.setVisibility(filteredList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPendingPaymentCard();
    }
}
