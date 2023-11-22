package com.example.attemp1.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.attemp1.R;
import com.example.attemp1.model.Observation;
import java.util.List;

public class ObservationListAdapter extends RecyclerView.Adapter<ObservationListAdapter.ObservationListViewHolder> {
    private List<Observation> observations;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ObservationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_observation, parent, false);
        return new ObservationListViewHolder(itemView, observations, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationListViewHolder holder, int position) {
        if (observations != null && position < observations.size()) {
            Observation observation = observations.get(position);
            holder.bind(observation);
        }
    }

    @Override
    public int getItemCount() {
        return observations != null ? observations.size() : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int observationId); // Define onItemClick method
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ObservationListAdapter(List<Observation> observations) {
        this.observations = observations;
    }

    public static class ObservationListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView observationTextView;
        private ImageView optionsImageView;
        private List<Observation> observations;
        private OnItemClickListener listener;

        public ObservationListViewHolder(@NonNull View itemView, List<Observation> observations, OnItemClickListener listener) {
            super(itemView);
            observationTextView = itemView.findViewById(R.id.observationTextView);
            optionsImageView = itemView.findViewById(R.id.optionsImageView);

            this.listener = listener;
            this.observations = observations;

            optionsImageView.setOnClickListener(this);
        }

        public void bind(Observation observation) {
            observationTextView.setText("Observation: " + observation.getObservation());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.optionsImageView) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    int observationId = observations.get(position).getId();
                    listener.onItemClick(observationId); // Notify the listener with the observation ID
                }
            }
        }
    }
}
