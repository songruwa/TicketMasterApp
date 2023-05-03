package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


//https://www.youtube.com/watch?time_continue=281&v=ACK67xU1Y3s&embeds_euri=https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Dpassing%2Bdata%2Bto%2Bviewmodel%2Bandroid%26rlz%3D1C5CHFA_enUS979US979%26sxsrf%3DAPwXEdfdZkOASlZjADIMEZGa9bDe6J7&source_ve_path=MTM5MTE3LDI4NjY2&feature=emb_logo
public class eventViewModel  extends ViewModel {
    private MutableLiveData<String> localDate = new MutableLiveData<>();
    private MutableLiveData<String> localTime = new MutableLiveData<>();
    private MutableLiveData<String> artist = new MutableLiveData<>();
    private MutableLiveData<String> venueName = new MutableLiveData<>();
    private MutableLiveData<String> genreNames = new MutableLiveData<>();
    private MutableLiveData<String> priceRange = new MutableLiveData<>();
    private MutableLiveData<String> status = new MutableLiveData<>();
    private MutableLiveData<String> buyTicketUrl = new MutableLiveData<>();
    private MutableLiveData<String> seatMapUrl = new MutableLiveData<>();
    private MutableLiveData<String> musicOrNot = new MutableLiveData<>();
    private MutableLiveData<List<Artists>> artistsList;


    public eventViewModel() {
        // Initialize MutableLiveData objects
        artistsList = new MutableLiveData<>();

    }



    public eventViewModel(String localDate, String localTime, String artist, String venueName, String genreNames, String priceRange, String status, String buyTicketUrl, String seatMapUrl, String musicOrNot) {
        this.localDate.setValue(localDate);
        this.localTime.setValue(localTime);
        this.artist.setValue(artist);
        this.venueName.setValue(venueName);
        this.genreNames.setValue(genreNames);
        this.priceRange.setValue(priceRange);
        this.status.setValue(status);
        this.buyTicketUrl.setValue(buyTicketUrl);
        this.seatMapUrl.setValue(seatMapUrl);
        this.musicOrNot.setValue(musicOrNot);

        Log.d("eventViewModel", "localDate: " + localDate);
        Log.d("eventViewModel", "localTime: " + localTime);
        Log.d("eventViewModel", "artist: " + artist);
        Log.d("eventViewModel", "venueName: " + venueName);
        Log.d("eventViewModel", "genreNames: " + genreNames);
        Log.d("eventViewModel", "priceRange: " + priceRange);
        Log.d("eventViewModel", "status: " + status);
        Log.d("eventViewModel", "buyTicketUrl: " + buyTicketUrl);
        Log.d("eventViewModel", "seatMapUrl: " + seatMapUrl);
        Log.d("eventViewModel", "musicOrNot: " + musicOrNot);

    }


    public LiveData<String> getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDateValue) {
        localDate.setValue(localDateValue);
    }

    public LiveData<String> getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTimeValue) {
        localTime.setValue(localTimeValue);
    }

    public LiveData<String> getArtist() {
        return artist;
    }

    public void setArtist(String artistValue) {
        artist.setValue(artistValue);
    }

    public LiveData<String> getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueNameValue) {
        venueName.setValue(venueNameValue);
    }

    public LiveData<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(String genreNamesValue) {
        genreNames.setValue(genreNamesValue);
    }

    public LiveData<String> getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRangeValue) {
        priceRange.setValue(priceRangeValue);
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public void setStatus(String statusValue) {
        status.setValue(statusValue);
    }

    public LiveData<String> getBuyTicketUrl() {
        return buyTicketUrl;
    }

    public void setBuyTicketUrl(String buyTicketUrlValue) {
        buyTicketUrl.setValue(buyTicketUrlValue);
    }

    public LiveData<String> getSeatMapUrl() {
        return seatMapUrl;
    }

    public void setSeatMapUrl(String seatMapUrlValue) {
        seatMapUrl.setValue(seatMapUrlValue);
    }

    public LiveData<String> getmusicOrNot() {
        return musicOrNot;
    }

    public void setmusicOrNot(String musicOrNotValue) {
        musicOrNot.setValue(musicOrNotValue);
    }

    public void setArtistsList(List<Artists> artists) {
        artistsList.postValue(artists);
    }

    public LiveData<List<Artists>> getArtistsList() {
        return artistsList;
    }


}

