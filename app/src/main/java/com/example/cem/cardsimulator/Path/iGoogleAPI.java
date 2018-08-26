package com.example.cem.cardsimulator.Path;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface iGoogleAPI {

    @GET
    Call<String> getPath(@Url String url);
}
