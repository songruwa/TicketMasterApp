package com.example.myapplication.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;




public class Search extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    String[] categories = { "All", "Music", "Sports", "Arts & Theatre", "Films", "Miscellaneous" };
    private String getSegmentId(String selectedCategory) {
        switch (selectedCategory) {
            case "Music":
                return "KZFzniwnSyZfZ7v7nJ";
            case "Sports":
                return "KZFzniwnSyZfZ7v7nE";
            case "Arts & Theatre":
                return "KZFzniwnSyZfZ7v7na";
            case "Films":
                return "KZFzniwnSyZfZ7v7nn";
            case "Miscellaneous":
                return "KZFzniwnSyZfZ7v7n1";
            default:
                return "";
        }
    }
    private int distance_input;
    private String selectedEventName;
    private String segmentId;
    private String latitude;
    private String longitude;
    private JSONObject tableData;



    //    https://www.geeksforgeeks.org/spinner-in-android-using-java-with-example/
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> autoCompleteAdapter;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        View view = inflater.inflate(R.layout.fragment_search, container, false);

//        https://www.section.io/engineering-education/making-api-requests-using-volley-android/

        // Set up the AutoCompleteTextView
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(this);
        requestQueue = Volley.newRequestQueue(getContext());


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (keyword.length() > 3) {
                    fetchSuggestions(keyword);
                } else {
                    selectedEventName = "";
                }
            }
        });

        // Assign chosen autocomplete result to variable selectEventName
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedEventName = (String) parent.getItemAtPosition(position);
                Log.d("EventNameGot", selectedEventName);
            }
        });



        //Distance Setup
        EditText distanceInput = view.findViewById(R.id.DISTANCEINPUT);

        distanceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    distance_input = Integer.parseInt(s.toString());
                    Log.d("DistanceDD", String.valueOf(distance_input));
                } catch (NumberFormatException e) {
                    distance_input = 0;
                }
            }
        });

        // Spinner Setup
        Spinner categorySpinner = view.findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categories);

        // set simple layout resource file
        // for each item of spinner
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        categorySpinner.setAdapter(adapter);


        // https://stackoverflow.com/questions/19320843/show-hide-text-with-button
        EditText locationInput = view.findViewById(R.id.LOCATIONINPUT);


        // Switch Setup
        Switch autoDetectSwitch = view.findViewById(R.id.switch1);
        autoDetectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch", "onCheckedChanged triggered");
                if (isChecked) {
                    locationInput.setVisibility(View.GONE); // Hide the location input
                    autoDetectLocation();
                } else {
                    locationInput.setVisibility(View.VISIBLE); // Show the location input
                }
            }
        });

        //setup google geocoding
        EditText geocodingInfo = view.findViewById(R.id.LOCATIONINPUT);

        geocodingInfo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String lc_input = s.toString();
                if (lc_input.length() > 3) {
                    geocoding(lc_input);
                }

            }
        });


        // click search button, searchEvents will be called
        // Find the Submitbutton
        Button submitButton = view.findViewById(R.id.Submitbutton);

        // Set an OnClickListener on the Submitbutton
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of SearchResultsFragment and pass the parameters to it
                SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                Bundle args = new Bundle();
                args.putInt("distance_input", distance_input);
                args.putString("selectedEventName", selectedEventName);
                Log.d("Search", "selectedEventName is: "+selectedEventName);
                args.putString("segmentId", segmentId);
                args.putString("latitude", latitude);
                args.putString("longitude", longitude);
                searchResultsFragment.setArguments(args);

                NavHostFragment.findNavController(Search.this).navigate(R.id.action_searchFragment_to_searchResultsFragment, args);

            }
        });

        return view;
    }

    private void fetchSuggestions(String keyword) {
        String apiUrl = "https://ticketmasterhw6.nn.r.appspot.com/search?keyword=" + keyword;
//        https://google.github.io/volley/simple.html

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray attractions = response.getJSONObject("_embedded").getJSONArray("attractions");
                            ArrayList<String> suggestionList = new ArrayList<>();

                            for (int i = 0; i < attractions.length(); i++) {
                                JSONObject attraction = attractions.getJSONObject(i);
                                String eventName = attraction.getString("name");
                                suggestionList.add(eventName);
                            }

                            autoCompleteAdapter.clear();
                            autoCompleteAdapter.addAll(suggestionList);
                            autoCompleteAdapter.notifyDataSetChanged();
//                            https://developer.android.com/reference/android/widget/AutoCompleteTextView
                            autoCompleteTextView.showDropDown();

                            Log.d("Success", "API called");
                            Log.d("Suggestions", suggestionList.toString());

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

    private void autoDetectLocation() {
        String ipInfoUrl = "https://ipinfo.io/?token=e2b6ea16e5d682";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ipInfoUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String loc = response.getString("loc");
                            Log.d("Location", loc);
                            String[] locArray = loc.split(",");

                            latitude = String.valueOf(Double.parseDouble(locArray[0]));
                            longitude = String.valueOf(Double.parseDouble(locArray[1]));

                            Log.d("Latitude", latitude);
                            Log.d("Longitude", longitude);

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

    //calling googleMap api to get lat&lng based on user's location input
    String encodedLocation = "";
    String GoogleapiKey = "AIzaSyDoxPEsB40HkiNgo_ZdgpenEgKgzRcPQrQ";

    private void geocoding(String locationInput) {
        try {
            encodedLocation = URLEncoder.encode(locationInput, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String geocodingUrl = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", encodedLocation, GoogleapiKey);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, geocodingUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");
                            JSONObject firstResult = resultsArray.getJSONObject(0);
                            JSONObject geometry = firstResult.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latitude = String.valueOf(location.getDouble("lat"));
                            longitude = String.valueOf(location.getDouble("lng"));

                            Log.d("Latitude", latitude);
//                            Log.d("DataType of Latitude", latitude.getClass().getName());
                            Log.d("Longitude", longitude);

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



//    //tic-table api GET
//    private void searchEvents(String keywordInput, int distance, String segmentId, String lat, String lng) {
//
//
//        String apiUrl = String.format("https://ticketmasterhw6.nn.r.appspot.com/tic?keyword=%s&distance=%d&segmentId=%s&lat=%s&lng=%s",
//                Uri.encode(keywordInput), distance, segmentId, lat, lng);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        tableData = response;
//                        Log.d("EventsSearchResult", tableData.toString());                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//    }



    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getContext(), categories[position], Toast.LENGTH_LONG).show();
        String selectedCategory = adapterView.getItemAtPosition(position).toString();
        segmentId = getSegmentId(selectedCategory);
        Log.d("SegmentId", segmentId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // This method is called when nothing is selected in the Spinner
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}