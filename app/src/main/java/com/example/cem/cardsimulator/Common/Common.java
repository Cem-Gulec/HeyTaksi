package com.example.cem.cardsimulator.Common;

import com.example.cem.cardsimulator.Path.iGoogleAPI;
import com.example.cem.cardsimulator.Path.RetrofitClient;

public class Common {

    public static final String baseURL = "https://maps.googleapis.com";
    public static iGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getClient(baseURL).create(iGoogleAPI.class);
    }
}
