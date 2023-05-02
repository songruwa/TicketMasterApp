package com.example.myapplication.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.R;
import com.example.myapplication.eventViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class details_card extends Fragment {

    private TextView dateTextView;
    private TextView timeTextView;
    private TextView artistTextView;
    private TextView venueTextView;
    private TextView genresTextView;
    private TextView priceRangeTextView;
    private TextView statusTextView;
    private TextView buyTicketUrlTextView;
    private ImageView seatMapImageView;

    private String localDate;
    private String localTime;
    private String artist;
    private String venueName;
    private String genreNames;
    private String priceRange;
    private String status;
    private String buyTicketUrl;
    private String seatMapUrl;

    private eventViewModel eventDetailsViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_card, container, false);

        // Get references to the views
        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        artistTextView = view.findViewById(R.id.artistTextView);
        venueTextView = view.findViewById(R.id.venueTextView);
        genresTextView = view.findViewById(R.id.genresTextView);
        priceRangeTextView = view.findViewById(R.id.priceRangeTextView);
        statusTextView = view.findViewById(R.id.ticketStatusTextView);
        buyTicketUrlTextView = view.findViewById(R.id.buyTicketAtTextView);
        seatMapImageView = view.findViewById(R.id.seatMapImageView);

        // Get the values from the eventViewModel
        eventDetailsViewModel = new ViewModelProvider(requireActivity()).get(eventViewModel.class);



        // Observe the LiveData objects and update the UI
        eventDetailsViewModel.getLocalDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String localDate) {
                dateTextView.setText(localDate);
            }
        });

        eventDetailsViewModel.getLocalTime().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String localTime) {
                timeTextView.setText(localTime);
            }
        });

        eventDetailsViewModel.getArtist().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String artist) {
                artistTextView.setText(artist);
            }
        });

        eventDetailsViewModel.getVenueName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String venueName) {
                venueTextView.setText(venueName);
            }
        });

        eventDetailsViewModel.getGenreNames().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String genreNames) {
                genresTextView.setText(genreNames);
            }
        });

        eventDetailsViewModel.getPriceRange().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String priceRange) {
                priceRangeTextView.setText(priceRange);
            }
        });

        eventDetailsViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                statusTextView.setText(status);
                // Update the background color based on the status
                if ("onsale".equals(status)) {
                    statusTextView.setBackgroundColor(Color.GREEN);
                } else if ("offsale".equals(status)) {
                    statusTextView.setBackgroundColor(Color.RED);
                } else if ("Canceled".equals(status)) {
                    statusTextView.setBackgroundColor(Color.BLACK);
                } else if ("postponed".equals(status)) {
                    statusTextView.setBackgroundColor(Color.parseColor("#FFA500"));
                } else {
                    statusTextView.setBackgroundColor(Color.parseColor("#FFA500"));
                }
            }
        });


        eventDetailsViewModel.getBuyTicketUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String buyTicketUrl) {
                buyTicketUrlTextView.setClickable(true);
                buyTicketUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());
                String clickableText = "<a href='" + buyTicketUrl + "'>" + buyTicketUrl + "</a>";
                buyTicketUrlTextView.setText(Html.fromHtml(clickableText));
            }
        });

        eventDetailsViewModel.getSeatMapUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String seatMapUrl) {
                if (!TextUtils.isEmpty(seatMapUrl)) {
                    Picasso.get().load(seatMapUrl).into(seatMapImageView);
                } else {
                    seatMapImageView.setImageResource(R.drawable.error_image);
                }
            }
        });

        // Set an onclick listener for the buy ticket URL
        buyTicketUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buyTicketUrl = eventDetailsViewModel.getBuyTicketUrl().getValue();
                if (buyTicketUrl != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(buyTicketUrl));
                    startActivity(browserIntent);
                }
            }
        });


        return view;
    }


}