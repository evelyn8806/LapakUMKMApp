package edu.uph.m24si2.lapakumkmapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventModel> eventList;
    private Context context;

    public EventAdapter(List<EventModel> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel event = eventList.get(position);
        holder.tvName.setText(event.getNama());
        holder.tvTag.setText(event.getKategori());
        holder.tvLocation.setText(event.getLokasi());
        holder.tvPrice.setText(event.getHarga());
        holder.ivImage.setImageResource(event.getGambar());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LapakDetailActivity.class);
                intent.putExtra("nama_lapak", event.getNama());
                intent.putExtra("kategori_lapak", event.getKategori());
                intent.putExtra("deskripsi_lapak", event.getDeskripsi());
                intent.putExtra("lokasi_lapak", event.getLokasi());
                intent.putExtra("gambar_lapak", event.getGambar());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateList(List<EventModel> newList) {
        this.eventList = newList;
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTag, tvName, tvLocation, tvPrice;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivEventImage);
            tvTag = itemView.findViewById(R.id.tvEventTag);
            tvName = itemView.findViewById(R.id.tvEventName);
            tvLocation = itemView.findViewById(R.id.tvEventLocation);
            tvPrice = itemView.findViewById(R.id.tvEventPrice);
        }
    }
}