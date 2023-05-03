package com.example.myapplication.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ArtistAdapter;
import com.example.myapplication.Artists;
import com.example.myapplication.R;
import com.example.myapplication.eventViewModel;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;



public class artist_card extends Fragment {

    private eventViewModel eventDetailsViewModel;
    private boolean isMusic;
    private String musicianId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist_card, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.artist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ArtistAdapter artistAdapter = new ArtistAdapter(new ArrayList<>());
        recyclerView.setAdapter(artistAdapter);


        eventDetailsViewModel = new ViewModelProvider(requireActivity()).get(eventViewModel.class);

        eventDetailsViewModel.getmusicOrNot().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String musicOrNot) {
                if ("Music".equals(musicOrNot)) {
                    isMusic = true;
                    Log.d("artist_card", "isMusic: "+ isMusic);
                } else {
                    isMusic = false;
                }
            }
        });

        eventDetailsViewModel.getArtist().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String artist) {
                if (isMusic == true) {
                    fetchSpotifyData(artist);


                } else {

                }
            }
        });

        eventDetailsViewModel.getArtistsList().observe(getViewLifecycleOwner(), new Observer<List<Artists>>() {
            @Override
            public void onChanged(List<Artists> artists) {
                artistAdapter.setArtists(artists); // You'll need to create a setArtists method in the ArtistAdapter
                artistAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }


    private void fetchSpotifyData(String artist) {
        String url = "https://ticketmasterhw6.nn.r.appspot.com/SPOTIFY?musician_name=" + Uri.encode(artist);
        Log.d("artist_card", "URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONObject("artists").getJSONArray("items");
                            List<Artists> artists = new ArrayList<>();

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject artistObj = items.getJSONObject(i);
                                String name = artistObj.getString("name");
                                String followers = artistObj.getJSONObject("followers").getString("total");
                                String popularity = artistObj.getString("popularity");
                                String spotifyLink = artistObj.getJSONObject("external_urls").getString("spotify");
//                                Log.d("artist_card", "spotifyLink: " + spotifyLink);

                                String artistId = artistObj.getString("id");

                                List<String> albumCovers = fetchAlbumCovers(artistId); // Fetch album covers here
//                                String imageUrl = artistObj.getJSONArray("images").getJSONObject(2).getString("url");
//                                Log.d("artist_card", "image url: " + imageUrl);
                                String imageUrl;
                                try {
                                    imageUrl = artistObj.getJSONArray("images").getJSONObject(2).getString("url");
                                    Log.d("artist_card", "image url: " + imageUrl);
                                } catch (JSONException e) {
                                    imageUrl = "none";
                                    Log.d("artist_card", "image url not found, defaulting to: " + imageUrl);
                                }

                                Artists artist = new Artists(name, followers, popularity, spotifyLink, albumCovers, imageUrl);
                                artists.add(artist);
                            }

                            eventDetailsViewModel.setArtistsList(artists); // Update ViewModel's LiveData list

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

    private List<String> fetchAlbumCovers(String artistId) {
        String url = "https://ticketmasterhw6.nn.r.appspot.com/ALBUMS?artistId=" + Uri.encode(artistId) + "&limit=3";
        final List<String> albumCovers = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject albumObj = items.getJSONObject(i);
                                String coverUrl = albumObj.getJSONArray("images").getJSONObject(0).getString("url");
                                Log.d("artist_card", "coverUrl: "+ i + coverUrl);
                                albumCovers.add(coverUrl);
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

        return albumCovers;
    }


}