package com.example.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class EventViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageViewCategory;
    private TextView textViewEventName;
    private TextView textViewVenueName;
    private TextView textViewEventDateTime;
    private ImageButton imageButtonFavorite;
    private Event event;
    private selectListener listener;




    public EventViewHolder(@NonNull View itemView , selectListener listener) {
        super(itemView);
        this.listener = listener;


        imageViewCategory = itemView.findViewById(R.id.imageView_category);
        textViewEventName = itemView.findViewById(R.id.textView_eventName);
        textViewVenueName = itemView.findViewById(R.id.textView_venueName);
        textViewEventDateTime = itemView.findViewById(R.id.textView_dateTime);
        imageButtonFavorite = itemView.findViewById(R.id.imageButton_favorite);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EventViewHolder", "onClick method called for event: " + event.getName());
                if (listener != null) {
                    listener.onItemClicked(itemView.getContext(), event);
                }
            }
        });

    }

//    https://stackoverflow.com/questions/24471109/recyclerview-onclick
    public void bind(Event event) {
        Log.d("EventViewHolder", "bind method called for event: " + event.getName());

        // Bind the event data to the views
        Glide.with(itemView)
                .load(event.getImages().get(0))
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.error_image)
                .into(imageViewCategory);

        textViewEventName.setText(event.getName());

        Venue venue = event.getVenue();
        textViewVenueName.setText(venue.getName());

        String localDate = event.getLocalDate();
        String localTime = event.getLocalTime();
        textViewEventDateTime.setText(String.format("%s, %s", localDate, localTime));


        imageButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle favorite button click
            }
        });
        this.event = event;

        Log.d("EventViewHolder's event variable", "event is " + event);

        Log.d("Binding event at position", String.valueOf(getAdapterPosition()));

        Log.d("EventViewHolder", "Event bound: " + event.getName());


    }

}

