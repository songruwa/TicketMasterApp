package com.example.myapplication.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.myapplication.EventDetailsViewModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;


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
    private EventDetailsViewModel viewModel;


//    https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args == null) {
            // handle null case
            Log.d("details_card", "failed passing of args");
            return null;
        }
        Log.d("details_card", "Received args: "+ args);
        String localDate = args.getString("local_Date");
        String localTime = args.getString("local_Time");
        String artist = args.getString("artistsss");
        String venueName = args.getString("venue_Name");
        String genreNames = args.getString("genre_Names");
        String priceRange = args.getString("price_Range");
        String status = args.getString("statussss");
        String buyTicketUrl = args.getString("buyTicket_Url");
        String seatMapUrl = args.getString("seat_MapUrl");

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


        // Set the values of the views
        dateTextView.setText(localDate);
        timeTextView.setText(localTime);
        artistTextView.setText(artist);
        venueTextView.setText(venueName);
        genresTextView.setText(genreNames);
        priceRangeTextView.setText(priceRange);
        statusTextView.setText(status);
//        https://stackoverflow.com/questions/8090459/android-dynamically-change-textview-background-color
        if (status == "onsale") {
            statusTextView.setBackgroundColor(Color.GREEN);
        } else if (status == "offsale") {
            statusTextView.setBackgroundColor(Color.RED);
        } else if (status == "Canceled") {
            statusTextView.setBackgroundColor(Color.BLACK);
        } else if (status == "postponed") {
            statusTextView.setBackgroundColor(Color.parseColor("#FFA500"));
        } else {
            statusTextView.setBackgroundColor(Color.parseColor("#FFA500"));
        }
        buyTicketUrlTextView.setText(buyTicketUrl);

//        https://stackoverflow.com/questions/2734270/how-to-make-links-in-a-textview-clickable
        // Make the buy ticket URL clickable
        buyTicketUrlTextView.setClickable(true);
        buyTicketUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());
        String clickableText = "<a href='" + buyTicketUrl + "'>" + buyTicketUrl + "</a>";
        buyTicketUrlTextView.setText(Html.fromHtml(clickableText));

        if (!TextUtils.isEmpty(seatMapUrl)) {
            Picasso.get().load(seatMapUrl).into(seatMapImageView);
        } else {
            seatMapImageView.setImageResource(R.drawable.error_image);
        }

        // Set an onclick listener for the buy ticket URL
        buyTicketUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(buyTicketUrl));
                startActivity(browserIntent);
            }
        });


        return view;
    }
}