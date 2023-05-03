package com.example.myapplication;

import java.util.List;

public class Artists {
    private String name;
    private String followers;
    private String popularity;
    private String spotifyLink;
    private List<String> albumCovers;


    public Artists(String name, String followers, String popularity, String spotifyLink, List<String> albumCovers) {
        this.name = name;
        this.followers = followers;
        this.popularity = popularity;
        this.spotifyLink = spotifyLink;
        this.albumCovers = albumCovers;
    }

    public String getName() {
        return name;
    }

    public String getFollowers() {
        return followers;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public List<String> getAlbumCovers() {
        return albumCovers;
    }

}
