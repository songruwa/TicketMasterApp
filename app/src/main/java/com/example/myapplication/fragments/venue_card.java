package com.example.myapplication.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.eventViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import com.ms.square.android.expandabletextview.ExpandableTextView;






public class venue_card extends Fragment  implements OnMapReadyCallback{

    private eventViewModel mEventViewModel;
    private TextView venueNameTextView;
    private TextView venueAddressTextView;
    private TextView venueCityStateTextView;
    private TextView venueContactInfoTextView;
    private MapView mapView;
    private GoogleMap mMap;
    private LatLng venueLatLng;

    private ExpandableTextView openHoursExpandableTextView;
    private ExpandableTextView generalRulesExpandableTextView;
    private ExpandableTextView childRulesExpandableTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_venue_card, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        venueNameTextView = view.findViewById(R.id.venue_name);
        venueAddressTextView = view.findViewById(R.id.venue_address);
        venueCityStateTextView = view.findViewById(R.id.venue_city_state);
        venueContactInfoTextView = view.findViewById(R.id.venue_contact_info);

        // Inside onViewCreated method
        openHoursExpandableTextView = view.findViewById(R.id.open_hours_expandable_text_view);
        generalRulesExpandableTextView = view.findViewById(R.id.general_rule_expandable_text_view);
        childRulesExpandableTextView = view.findViewById(R.id.child_rule_expandable_text_view);


        mapView = view.findViewById(R.id.map_view);


        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);

        mEventViewModel = new ViewModelProvider(requireActivity()).get(eventViewModel.class);
        mEventViewModel.getVenueName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String venueName) {
                fetchVenueInfo(venueName);
            }
        });
    }

    private void fetchVenueInfo(String venueName) {
        String url = "https://ticketmasterhw6.nn.r.appspot.com/venue?venueName=" + Uri.encode(venueName);
        Log.d("venue_card", "URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject venueObj = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                            String name = venueObj.getString("name");
                            double lat = venueObj.getJSONObject("location").getDouble("latitude");
                            double lng = venueObj.getJSONObject("location").getDouble("longitude");
                            venueLatLng = new LatLng(lat, lng);
                            Log.d("venue_card", "venueLatLng: " + venueLatLng);

                            String city = venueObj.getJSONObject("city").getString("name");
                            String state = venueObj.getJSONObject("state").getString("name");
                            String phoneNumber = venueObj.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");

                            // Fetch open hours, general rules, and child rules
                            String noInformation = "No information available";
                            JSONObject boxOfficeInfo = venueObj.optJSONObject("boxOfficeInfo");
                            String openHours = boxOfficeInfo != null ? boxOfficeInfo.optString("openHoursDetail", noInformation) : noInformation;

                            JSONObject generalInfo = venueObj.optJSONObject("generalInfo");
                            String generalRule = generalInfo != null ? generalInfo.optString("generalRule", noInformation) : noInformation;
                            String childRule = generalInfo != null ? generalInfo.optString("childRule", noInformation) : noInformation;

                            updateExpandableTextViews(openHours, generalRule, childRule);


                            // Update UI with fetched data
                            venueNameTextView.setText(name);
                            venueAddressTextView.setText(venueObj.getJSONObject("address").getString("line1"));
                            venueCityStateTextView.setText(city + ", " + state);
                            venueContactInfoTextView.setText(phoneNumber);

                            // Update the map with the fetched LatLng
                            if (mMap != null) {
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(venueLatLng).title(name));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLatLng, 15));
                            }

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

        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void updateVenueInfo(String name, String address, String city, String state, String contactInfo) {
        venueNameTextView.setText(name);
        venueAddressTextView.setText(address);
        venueCityStateTextView.setText(city + ", " + state);
        venueContactInfoTextView.setText(contactInfo);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapsInitializer.initialize(requireActivity());

        if (venueLatLng != null) {
            updateMap();
        }
    }

    private void updateMap() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(venueLatLng).title(mEventViewModel.getVenueName().getValue()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLatLng, 15));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void updateExpandableTextViews(String openHours, String generalRule, String childRule) {
        openHoursExpandableTextView.setText(openHours);
        generalRulesExpandableTextView.setText(generalRule);
        childRulesExpandableTextView.setText(childRule);
    }



}