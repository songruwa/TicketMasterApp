package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ViewPager2 viewPager2;
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

    private String musicOrNot;

    private artist_card artistFragment;
    private details_card detailsFragment;
    private venue_card venueFragment;

    private  eventViewModel eventDetailsViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_event);

        // Get the event name from the intent and set it as the activity title
        String eventName = getIntent().getStringExtra("eventName");
        setTitle(eventName);


        // https://www.youtube.com/watch?v=FcPUFp8Qrps
        // Set up the back button in the top bar
//        https://stackoverflow.com/questions/34110565/how-to-add-back-button-on-actionbar-in-android-studio
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // Tab Design
        tabLayout = findViewById(R.id.eventDetailTabLayout);
        viewPager2 = findViewById(R.id.view_pager_eventDetails);
        adapter = new vpAdapter_detail(this);
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });



        // Get the event id, use this event id to get event details
        eventId = getIntent().getStringExtra("eventId");
        Log.d("DetailsOfEvent", "Received eventId: " + eventId);


        searchDetails(eventId);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_of_event, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_favorite) {
            // Handle favorite button click
            Toast.makeText(this, "Favorite button clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_facebook) {
            // Handle Facebook button click
            shareOnFacebook();
            return true;
        } else if (id == R.id.action_twitter) {
            // Handle Twitter button click
            shareOnTwitter();
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
                            localTime = dates.optString("localTime","None");
                            Log.d("DetailsOfEvent", "localDate: "+localDate);

                            // Get the artist/team names
//                            artist = response.getString("name");
//                            Log.d("DetailsOfEvent", "artist/team: " + artist);

                            JSONObject aarrtt = response.getJSONObject("_embedded");
                            artist = aarrtt.getJSONArray("attractions").getJSONObject(0).optString("name");
                            Log.d("DetailsOfEvent", "artist/team: " + artist);




                            // Get the venue name
                            JSONObject venue = response.getJSONObject("_embedded");
                            venueName = venue.getJSONArray("venues").getJSONObject(0).optString("name","");
                            Log.d("DetailsOfEvent", "venueName: "+ venueName);

                            // Get the genre
                            StringBuilder sb = new StringBuilder();

                            String subGenre = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").optString("name","");
                            String genre = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").optString("name","");
                            musicOrNot = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").optString("name", "");
                            String subType = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subType").optString("name","");
                            String type = response.getJSONArray("classifications").getJSONObject(0).getJSONObject("type").optString("name","");
                            if (!subGenre.isEmpty()) {
                                sb.append(subGenre).append(" | ");
                            }
                            if (!genre.isEmpty()) {
                                sb.append(genre).append(" | ");
                            }
                            if (!musicOrNot.isEmpty()) {
                                sb.append(musicOrNot).append(" | ");
                            }
                            if (!subType.isEmpty()) {
                                sb.append(subType).append(" | ");
                            }
                            if (!type.isEmpty()) {
                                sb.append(type).append(" | ");
                            }

                            genreNames = sb.toString();
                            Log.d("DetailsOfEvent", "genre: " + genreNames);

                            Log.d("DetailsOfEvent", "musicOrNot: " + musicOrNot);






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


                            eventDetailsViewModel = new ViewModelProvider(DetailsOfEvent.this).get(eventViewModel.class);
                            eventDetailsViewModel.setLocalDate(localDate);
                            eventDetailsViewModel.setLocalTime(localTime);
                            eventDetailsViewModel.setArtist(artist);
                            eventDetailsViewModel.setVenueName(venueName);
                            eventDetailsViewModel.setGenreNames(genreNames);
                            eventDetailsViewModel.setPriceRange(priceRange);
                            eventDetailsViewModel.setStatus(status);
                            eventDetailsViewModel.setBuyTicketUrl(buyTicketUrl);
                            eventDetailsViewModel.setSeatMapUrl(seatMapUrl);
                            eventDetailsViewModel.setmusicOrNot(musicOrNot);


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

    private void shareOnFacebook() {
        String url = "https://www.facebook.com/sharer/sharer.php?u=" + buyTicketUrl + "&src=sdkpreparse";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void shareOnTwitter() {
        String tweetText = "Check " + getTitle() + " on Ticketmaster " + buyTicketUrl;
        String hashtags = "hashtag1,hashtag2";
        String twitterUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(tweetText) + "&hashtags=" + hashtags;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
        startActivity(browserIntent);
    }



}