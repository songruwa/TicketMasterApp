package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DetailsOfEvent;
import com.example.myapplication.EventListAdapter;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.Event;
import com.example.myapplication.Venue;
import com.example.myapplication.selectListener;


public class SearchResultsFragment extends Fragment implements selectListener  {

    private RequestQueue requestQueue;
    private JSONObject tableData;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private selectListener mListener;
    private List<Event> eventList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof selectListener) {
            mListener = (selectListener) context;
            Log.d("SearchResultsFragment","Setting mListener"+ mListener);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement selectListener");
        }
    }

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tableData = new JSONObject();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        requestQueue = Volley.newRequestQueue(getContext());

        selectListener listener = mListener;

        EventListAdapter adapter = new EventListAdapter(eventList, listener);
        recyclerView.setAdapter(adapter);

        // Retrieve the parameters passed from SearchFragment
        Bundle args = getArguments();
        int distanceInput = args.getInt("distance_input");
        String selectedEventName = args.getString("selectedEventName");
        String segmentId = args.getString("segmentId");
        String latitude = args.getString("latitude");
        String longitude = args.getString("longitude");

        searchEvents(selectedEventName, distanceInput, segmentId, latitude, longitude);

        ImageButton backButton = view.findViewById(R.id.imageButton_backSearch);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SearchResultsFragment.this).navigate(R.id.action_searchResultsFragment_to_searchFragment);
            }
        });
        return view;
    }



    //tic-table api GET
    private void searchEvents(String keywordInput, int distance, String segmentId, String lat, String lng) {
        String apiUrl = String.format("https://ticketmasterhw6.nn.r.appspot.com/tic?keyword=%s&distance=%d&segmentId=%s&lat=%s&lng=%s",
                Uri.encode(keywordInput), distance, segmentId, lat, lng);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            tableData = response;
                            Log.d("Table Data is", tableData.toString());
                            JSONArray eventsArray = response.getJSONObject("_embedded").getJSONArray("events");
                            Log.d("EventsArray is", eventsArray.getString(0));
                            List<Event> eventList = new ArrayList<>();

                            for (int i = 0; i < eventsArray.length(); i++) {
                                JSONObject eventObject = eventsArray.getJSONObject(i);
                                Event event = new Event();
                                event.setName(eventObject.getString("name"));
                                event.setLocalDate(eventObject.getJSONObject("dates").getJSONObject("start").optString("localDate",""));
                                event.setLocalTime(eventObject.getJSONObject("dates").getJSONObject("start").optString("localTime", ""));
                                event.setSegment(eventObject.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").optString("name",""));

                                JSONArray imageArray = eventObject.getJSONArray("images");
                                List<String> imageList = new ArrayList<>();
                                for (int j = 0; j < imageArray.length(); j++) {
                                    imageList.add(imageArray.getJSONObject(j).getString("url"));
                                }
                                event.setImages(imageList);

                                JSONObject venueObject = eventObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                                Venue venue = new Venue();
                                venue.setName(venueObject.getString("name"));
                                event.setVenue(venue);

                                eventList.add(event);
                            }
                            Log.d("Event List size is", String.valueOf(eventList.size()));
                            EventListAdapter adapter = new EventListAdapter(eventList, mListener);



                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onItemClicked(@NonNull Context context, @NonNull Event event) {

    }
}

