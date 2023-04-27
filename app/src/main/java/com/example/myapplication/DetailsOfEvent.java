package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.fragments.artist_card;
import com.example.myapplication.fragments.details_card;
import com.example.myapplication.fragments.venue_card;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailsOfEvent extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private vpAdapter_detail adapter;


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

// Tab Design
        tabLayout = findViewById(R.id.eventDetailTabLayout);
        viewPager = findViewById(R.id.view_pager_eventDetails);
        adapter = new vpAdapter_detail(getSupportFragmentManager(), getLifecycle());

        adapter.addFragment(new details_card(), "Details");
        adapter.addFragment(new artist_card(), "Artist(s)");
        adapter.addFragment(new venue_card(), "Venue");

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getPageTitle(position));
            viewPager.setCurrentItem(tab.getPosition(), true);
        }).attach();

        // Get the event id, use this event id to get event details
        String eventId = getIntent().getStringExtra("eventId");
        Log.d("DetailsOfEvent", "Received eventId: " + eventId);



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
    // Do it later


}