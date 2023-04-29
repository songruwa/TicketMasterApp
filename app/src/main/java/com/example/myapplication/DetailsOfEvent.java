package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.fragments.artist_card;
import com.example.myapplication.fragments.details_card;
import com.example.myapplication.fragments.venue_card;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsOfEvent extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private vpAdapter_detail adapter;
    private String eventId;

    private String localDate;
    private String localTime;
    private String artist;
    private String venueName;
    private String genreNames;
    private String priceRange;
    private String status;
    private String buyTicketUrl;
    private String seatMapUrl;

    private EventDetailsViewModel eventDetailsViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_event);

        // Get the event name from the intent and set it as the activity title
        String eventName = getIntent().getStringExtra("eventName");
        setTitle(eventName);


        // https://www.youtube.com/watch?v=FcPUFp8Qrps
        // Set up the back button in the top bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the UI elements

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
        eventId = getIntent().getStringExtra("eventId");
        Log.d("DetailsOfEvent", "Received eventId: " + eventId);

        // Initialize the ViewModel
//        eventDetailsViewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);



        // Call the event details API
        searchDetails(eventId);

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


//    https://mobikul.com/pass-data-activity-fragment-android/
    // passing data from DetailsOfEvent to details_card




    // Initialize the ViewModel



    //calling event details api
    private void searchDetails(String eventId) {
        String url = "https://ticketmasterhw6.nn.r.appspot.com/event-detail?eventId=" + eventId;
        Log.d("DetailsOfEvent", "URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("DetailsOfEvent", "Response: " + response.toString());
                        // TODO: Parse the response and update UI

                        try {

                            // Get the date and time
                            JSONObject dates = response.getJSONObject("dates").getJSONObject("start");
                            localDate = dates.getString("localDate");
                            localTime = dates.getString("localTime");
                            Log.d("DetailsOfEvent", "localDate: "+localDate);

                            // Get the artist/team names
                            artist = response.getString("name");
                            Log.d("DetailsOfEvent", "artist/team: " + artist);


                            // Get the venue name
                            JSONObject venue = response.getJSONObject("_embedded");
                            venueName = venue.getJSONArray("venues").getJSONObject(0).optString("name","");
                            Log.d("DetailsOfEvent", "venueName: "+ venueName);

                            // Get the genre
                            StringBuilder sb = new StringBuilder();
                            
                            String subGenre = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").optString("name","");
                            String genre = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").optString("name","");
                            String segment = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").optString("name", "");
                            String subType = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").optString("name","");
                            String type = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").optString("name","");
                            if (!subGenre.isEmpty()) {
                                sb.append(subGenre).append(" | ");
                            }
                            if (!genre.isEmpty()) {
                                sb.append(genre).append(" | ");
                            }
                            if (!segment.isEmpty()) {
                                sb.append(segment).append(" | ");
                            }
                            if (!subType.isEmpty()) {
                                sb.append(subType).append(" | ");
                            }
                            if (!type.isEmpty()) {
                                sb.append(type).append(" | ");
                            }

                            genreNames = sb.toString();
                            Log.d("DetailsOfEvent", "genre: " + genreNames);

                            // Get the price range
                            JSONArray priceRanges = response.optJSONArray("priceRanges");
                            priceRange = "";
                            if (priceRanges != null && priceRanges.length() > 0) {
                                JSONObject priceRangeObj = priceRanges.getJSONObject(0);
                                String min = priceRangeObj.getString("min");
                                String max = priceRangeObj.getString("max");
                                priceRange = min + " - " + max + "(USD)";
                                Log.d("DetailsOfEvent", "price range: "+ priceRange);
                            }

                            // Get the ticket status
                            status = response.getJSONObject("dates").getJSONObject("status").optString("code","");
                            Log.d("DetailsOfEvent", "status: "+status);

                            // Get the URL to buy tickets
                            buyTicketUrl = response.optString("url");
                            Log.d("DetailsOfEvent", "url to buy ticket: "+ buyTicketUrl);

                            // Get the URL to the seat map
                            seatMapUrl = "";
                            JSONObject seatMap = response.optJSONObject("seatmap");
                            if (seatMap != null) {
                                seatMapUrl = seatMap.optString("staticUrl");
                                Log.d("DetailsOfEvent", "seatmap url: "+seatMapUrl);
                            }

                            // Update the UI with the extracted information
                            details_card detailsCardFragment = new details_card();

                            Bundle args = new Bundle();
                            args.putString("local_Date", localDate);
                            args.putString("local_Time",localTime);
                            args.putString("artistsss", artist);
                            args.putString("venue_Name", venueName);
                            args.putString("genre_Names", genreNames);
                            args.putString("price_Range", priceRange);
                            args.putString("statussss", status);
                            args.putString("buyTicket_Url",buyTicketUrl);
                            args.putString("seat_MapUrl",seatMapUrl);
                            if (args == null) {
                                Log.d("DetailsOfEvent", "Passing args is null");
                            } else {
                                Log.d("DetailsOfEvent", "passing args: " + args);
                            }
                            detailsCardFragment.setArguments(args);

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.detailFragment, detailsCardFragment).commit();

                            // TODO: Update the UI with the extracted information
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}