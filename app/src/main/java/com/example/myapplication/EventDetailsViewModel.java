package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventDetailsViewModel extends ViewModel {
    public MutableLiveData<String> localDate = new MutableLiveData<>();
    public MutableLiveData<String> localTime = new MutableLiveData<>();
    public MutableLiveData<String> artist = new MutableLiveData<>();
    public MutableLiveData<String> venueName = new MutableLiveData<>();
    public MutableLiveData<String> genreNames = new MutableLiveData<>();
    public MutableLiveData<String> priceRange = new MutableLiveData<>();
    public MutableLiveData<String> status = new MutableLiveData<>();
    public MutableLiveData<String> buyTicketUrl = new MutableLiveData<>();
    public MutableLiveData<String> seatMapUrl = new MutableLiveData<>();
}

