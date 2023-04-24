package com.example.myapplication.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONObject;

public class SearchResultsFragment extends Fragment {

    private RequestQueue requestQueue;
    private JSONObject tableData;


    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tableData = new JSONObject();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        // Retrieve the parameters passed from SearchFragment
        Bundle args = getArguments();
        int distanceInput = args.getInt("distance_input");
        String selectedEventName = args.getString("selectedEventName");
        String segmentId = args.getString("segmentId");
        String latitude = args.getString("latitude");
        String longitude = args.getString("longitude");

        searchEvents(selectedEventName, distanceInput, segmentId, latitude, longitude);
        Log.d("table-event", tableData.toString());

        ImageButton BackButton = view.findViewById(R.id.imageButton_backSearch);

        BackButton.setOnClickListener(new View.OnClickListener() {
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
                        tableData = response;
                        Log.d("EventsSearchResult", tableData.toString());                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}

