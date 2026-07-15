package edu.uph.m24si2.lapakumkmapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RentalRequestAdapter extends RecyclerView.Adapter<RentalRequestAdapter.ViewHolder> {

    private List<RentalRequest> requests;
    private Context context;

    public RentalRequestAdapter(List<RentalRequest> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rental_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentalRequest request = requests.get(position);
        holder.tvName.setText(request.getEventName());
        holder.tvLocation.setText(request.getEventLocation());
        holder.ivImage.setImageResource(request.getEventImage());
        holder.tvStatus.setText(request.getStatus().getLabel());

        // Status coloring
        switch (request.getStatus()) {
            case MENUNGGU_PEMBAYARAN:
                holder.tvStatus.setTextColor(Color.parseColor("#E65100"));
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
                break;
            case MENUNGGU_PERSETUJUAN:
                holder.tvStatus.setTextColor(Color.parseColor("#FF8F00"));
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF8E1"));
                break;
            case AKTIF:
                holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
                break;
            case SELESAI:
                holder.tvStatus.setTextColor(Color.parseColor("#616161"));
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F5F5F5"));
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (request.getStatus() == RentalRequest.Status.AKTIF || request.getStatus() == RentalRequest.Status.MENUNGGU_PERSETUJUAN || request.getStatus() == RentalRequest.Status.SELESAI) {
                Intent intent = new Intent(context, StatusPengajuanActivity.class);
                intent.putExtra("rental_request", request);
                intent.putExtra("IS_DETAIL", true);
                context.startActivity(intent);
            } else if (request.getStatus() == RentalRequest.Status.MENUNGGU_PEMBAYARAN) {
                Intent intent = new Intent(context, PaymentDetailActivity.class);
                intent.putExtra("rental_request", request);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvLocation, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivRequestImage);
            tvName = itemView.findViewById(R.id.tvRequestName);
            tvLocation = itemView.findViewById(R.id.tvRequestLocation);
            tvStatus = itemView.findViewById(R.id.tvRequestStatus);
        }
    }
}
