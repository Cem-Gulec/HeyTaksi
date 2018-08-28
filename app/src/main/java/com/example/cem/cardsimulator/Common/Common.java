package com.example.cem.cardsimulator.Common;

import com.example.cem.cardsimulator.Path.iGoogleAPI;
import com.example.cem.cardsimulator.Path.RetrofitClient;

public class Common {

    public static final String baseURL = "https://maps.googleapis.com";

    private static int basePrice = 4;
    private static double perkm = 2.5;
    private static int minPrice = 10;

    public static double getPrice(double km){

        if((basePrice + km*perkm)< minPrice)
            return minPrice;
        else
            return basePrice + km*perkm;
    }

    public static iGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getClient(baseURL).create(iGoogleAPI.class);
    }
}
