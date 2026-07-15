package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageView btnBack = findViewById(R.id.btnBackHistory);
        rvHistory = findViewById(R.id.rvHistory);
        tvEmpty = findViewById(R.id.tvEmptyHistory);

        btnBack.setOnClickListener(v -> finish());

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        loadHistory();
    }

    private void loadHistory() {
        List<RentalRequest> historyRequests = RentalManager.getInstance().getRequestsByStatus(RentalRequest.Status.SELESAI);
        
        if (historyRequests.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            RentalRequestAdapter adapter = new RentalRequestAdapter(historyRequests, this);
            rvHistory.setAdapter(adapter);
        }
    }
}
