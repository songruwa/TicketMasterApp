package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsOfEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_event);

        // Get the event name from the intent and set it as the activity title
        String eventName = getIntent().getStringExtra("eventName");
        setTitle(eventName);


//        https://www.youtube.com/watch?v=FcPUFp8Qrps
        // Set up the back button in the top bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    // Handle back button clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //        https://stackoverflow.com/questions/13680281/text-for-marquee-dynamically
//        https://stackoverflow.com/questions/41926557/textview-marquee-programmatically

    // Set the title to a scrolling marquee


}