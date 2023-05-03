package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<Artists> artistList;

    public ArtistAdapter(List<Artists> artistList) {
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);
        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Log.d("ArtistAdapter", "onBindViewHolder() called, position: " + position);

        Artists currentArtist = artistList.get(position);

        holder.artistName.setText(currentArtist.getName());
        holder.followers.setText(currentArtist.getFollowers());
        holder.popularity.setText(currentArtist.getPopularity());
        holder.SpotifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArtist.getSpotifyLink()));
                v.getContext().startActivity(browserIntent);
            }
        });

        // Load the album covers and Spotify link here

        // Load artist image using Glide
//        Glide.with(holder.artistImage.getContext())
//                .load(currentArtist.getImageUrl())
//                .into(holder.artistImage);
    }

    @Override
    public int getItemCount() {
        Log.d("ArtistAdapter", "getItemCount() called, list size: " + artistList.size());

        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView followers;
        TextView popularity;
        ImageView albumCover1;
        ImageView albumCover2;
        ImageView albumCover3;
        TextView SpotifyLink;

        // Add other views as needed

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artist_name);
            followers = itemView.findViewById(R.id.followers);
            popularity = itemView.findViewById(R.id.popularity);
            albumCover1 = itemView.findViewById(R.id.album_cover_1);
            albumCover2 = itemView.findViewById(R.id.album_cover_2);
            albumCover3 = itemView.findViewById(R.id.album_cover_3);
            SpotifyLink = itemView.findViewById(R.id.spotify_link);

        }
    }

    // Add this method to update the artistList and notify the adapter of the changes
    public void setArtists(List<Artists> artists) {
        this.artistList = artists;
    }
}

