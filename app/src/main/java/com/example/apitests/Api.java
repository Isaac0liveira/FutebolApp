package com.example.apitests;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://api.api-futebol.com.br/v1/";
    //live_32731f223b23e3d6d70558a3e47a6f
    @Headers({"Authorization: Bearer live_32731f223b23e3d6d70558a3e47a6f"})
    @GET("campeonatos")
    Call<List<Campeonato>> getCampeonatos();

    @Headers({"Authorization: Bearer live_32731f223b23e3d6d70558a3e47a6f"})
    @GET("campeonatos/{id}")
    Call<Campeonato> getCampeonato(@Path("id") String id);

    @Headers({"Authorization: Bearer live_32731f223b23e3d6d70558a3e47a6f"})
    @GET("campeonatos/{campeonato}/fases/{fase}")
    Call<Fase> getFase(@Path("campeonato") String campeonato, @Path("fase") String fase);


}
