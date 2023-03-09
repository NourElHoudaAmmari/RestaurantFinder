package com.example.restaurantapp;

import com.example.restaurantapp.Model.MyPlaces;
import com.example.restaurantapp.Model.Results;
import com.example.restaurantapp.Remote.IGoogleAPIService;
import com.example.restaurantapp.Remote.RetrofitClient;

public class Common {
    public static Results currentResult;
    private static final String GOOGLE_API_URL ="https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
