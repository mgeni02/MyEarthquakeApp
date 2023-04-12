package com.example.mgeni_ingrid_s2034327;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> implements Filterable {

    private List<Earthquake> originalEarthquakes;
    List<Earthquake> filteredEarthquakes;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Earthquake earthquake);
    }

    public EarthquakeAdapter(List<Earthquake> earthquakes, OnItemClickListener listener) {
        this.originalEarthquakes = new ArrayList<>(earthquakes);
        this.filteredEarthquakes = new ArrayList<>(earthquakes);
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView linkTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_textview1);
            dateTextView = itemView.findViewById(R.id.date_textview);
            linkTextView = itemView.findViewById(R.id.link_textview);
        }

        public void bind(final Earthquake earthquake, final OnItemClickListener listener) {
            titleTextView.setText(earthquake.getTitle());
            dateTextView.setText(earthquake.getDate());
            linkTextView.setText("Learn More");
            linkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(earthquake);
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View earthquakeView = inflater.inflate(R.layout.item_earthquake, parent, false);

        ViewHolder viewHolder = new ViewHolder(earthquakeView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Earthquake earthquake = filteredEarthquakes.get(position);
        holder.bind(earthquake, listener);

        double magnitude = earthquake.getMagnitude();
        if (magnitude >= 7.0) {
            holder.itemView.setBackgroundColor(Color.RED);
        } else if (magnitude >= 6.0) {
            holder.itemView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.itemView.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return filteredEarthquakes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                List<Earthquake> filteredList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    filteredList.addAll(originalEarthquakes);
                } else {
                    for (Earthquake earthquake : originalEarthquakes) {
                        if (earthquake.getTitle().toLowerCase().contains(searchText) ||
                                earthquake.getDescription().toLowerCase().contains(searchText)) {
                            filteredList.add(earthquake);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEarthquakes.clear();
                filteredEarthquakes.addAll((List<Earthquake>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public void resetData() {
        filteredEarthquakes.clear();
        filteredEarthquakes.addAll(originalEarthquakes);
        notifyDataSetChanged();
    }

}
