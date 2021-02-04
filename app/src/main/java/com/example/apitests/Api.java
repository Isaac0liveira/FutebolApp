package com.example.apitests;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://api.api-futebol.com.br/v1/";

    @Headers({"Authorization: Bearer live_77101c44e30c908b394ae9198e49fe"})
    @GET("campeonatos")
    Call<List<Campeonato>> getCampeonatos();

    @Headers({"Authorization: Bearer live_77101c44e30c908b394ae9198e49fe"})
    @GET("campeonatos/{id}")
    Call<Campeonato> getCampeonato(@Path("id") String id);


}
