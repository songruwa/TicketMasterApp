package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private List<Event> eventList;
    private selectListener listener;


    public EventListAdapter(List<Event> eventList, selectListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
        Log.d("EventListAdapter", "OnClickListener is set on EventViewHolder");
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount called, size is", String.valueOf(eventList.size()));
        return eventList.size();
    }
}
