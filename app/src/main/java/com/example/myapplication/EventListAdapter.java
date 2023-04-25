package com.example.myapplication;

import com.example.myapplication.Event;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private List<Event> eventList;

    public EventListAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
        Log.d("onBindViewHolder called for", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount called, size is", String.valueOf(eventList.size()));
        return eventList.size();
    }
}
