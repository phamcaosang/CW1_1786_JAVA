package com.example.attemp1.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attemp1.R;
import com.example.attemp1.model.Hike;

import java.util.List;

public class HikeListAdapter extends RecyclerView.Adapter<HikeListAdapter.HikeListViewHolder> {
    private List<Hike> hikes;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public HikeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_hike, parent, false);
        return new HikeListViewHolder(itemView, hikes, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeListViewHolder holder, int position) {
        if (hikes != null && position < hikes.size()) {
            Hike hike = hikes.get(position);
            holder.bind(hike);
        }
    }

    @Override
    public int getItemCount() {
        return hikes != null ? hikes.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int hikeId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HikeListAdapter(List<Hike> hikes) {
        this.hikes = hikes;
    }

    public static class HikeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nameTextView;
        private ImageView optionsImageView;
        private List<Hike> hikes;
        private OnItemClickListener listener;
        public HikeListViewHolder(@NonNull View itemView, List<Hike> hikes, OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            optionsImageView = itemView.findViewById(R.id.optionsImageView);

            this.listener = listener;
            this.hikes = hikes;

            optionsImageView.setOnClickListener(this);
        }
        public void bind(Hike hike) {
            nameTextView.setText("Name: " + hike.getName());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.optionsImageView) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    int hikeId = hikes.get(position).getId();
                    listener.onItemClick(hikeId); // Notify the listener with the hike ID
                }
            }
        }

    }
}
