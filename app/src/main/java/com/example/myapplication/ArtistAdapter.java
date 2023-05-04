package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
        Artists currentArtist = artistList.get(position);

        holder.artistName.setText(currentArtist.getName());
        holder.followers.setText(currentArtist.getFollowers());
//        holder.popularity.setText(currentArtist.getPopularity());
        int popularityValue = Integer.parseInt(currentArtist.getPopularity());
        holder.popularity.setProgress(popularityValue);
        holder.popularity_text.setText(String.valueOf(popularityValue));

        holder.SpotifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArtist.getSpotifyLink()));
                v.getContext().startActivity(browserIntent);
            }
        });


//        https://stackoverflow.com/questions/45232608/how-to-load-image-into-imageview-from-url-using-glide-v4-0-0rc1
        // Load artist image using Glide
        if (!"none".equals(currentArtist.getImageUrl())) {
            Glide.with(holder.artistImg.getContext())
                    .load(currentArtist.getImageUrl())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.error_image)
                    .override(200, 200)
                    .centerCrop()
                    .into(holder.artistImg);
        } else {
            holder.artistImg.setImageResource(R.drawable.error_image); // Set a default image when imageUrl is "none"
        }


        // Load album covers using Glide
        // Load album covers using Glide
        ImageView[] albumCoversViews = {holder.albumCover1, holder.albumCover2, holder.albumCover3};
        for (int i = 0; i < 3; i++) {
            if (i < currentArtist.getAlbumCovers().size()) {
                String coverUrl = currentArtist.getAlbumCovers().get(i);
                if (!"none".equals(coverUrl)) {
                    Glide.with(albumCoversViews[i].getContext())
                            .load(coverUrl)
                            .placeholder(R.drawable.ic_baseline_image_24)
                            .error(R.drawable.error_image)
                            .override(130, 130)
                            .centerCrop()
                            .into(albumCoversViews[i]);
                } else {
                    albumCoversViews[i].setImageResource(R.drawable.ic_baseline_image_24); // Set a default image when coverUrl is "none"
                }
            } else {
                albumCoversViews[i].setImageResource(R.drawable.ic_baseline_image_24); // Set a default image when there's no album cover
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d("ArtistAdapter", "getItemCount() called, list size: " + artistList.size());

        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView followers;
//        TextView popularity;
        ImageView albumCover1;
        ImageView albumCover2;
        ImageView albumCover3;
        TextView SpotifyLink;
        ImageView artistImg;
        ProgressBar popularity;
        TextView popularity_text;

        // Add other views as needed

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.artist_name);
            followers = itemView.findViewById(R.id.followers);
//            popularity = itemView.findViewById(R.id.popularity);
            albumCover1 = itemView.findViewById(R.id.album_cover_1);
            albumCover2 = itemView.findViewById(R.id.album_cover_2);
            albumCover3 = itemView.findViewById(R.id.album_cover_3);
            SpotifyLink = itemView.findViewById(R.id.spotify_link);
            artistImg = itemView.findViewById(R.id.artist_image);

            popularity = itemView.findViewById(R.id.popularity);
            popularity_text = itemView.findViewById(R.id.popularity_text);

        }
    }

    // Add this method to update the artistList and notify the adapter of the changes
    public void setArtists(List<Artists> artists) {
        this.artistList = artists;
    }
}

