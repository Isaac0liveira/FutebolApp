package com.example.apitests;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://api.api-futebol.com.br/v1/";
    String Auth = "live_1377d15aa59339b8bdd3da9ce1c2a8"; //Coloque a API Key aqui
    //live_32731f223b23e3d6d70558a3e47a6f
    @Headers({"Authorization: Bearer " + Auth})
    @GET("campeonatos")
    Call<List<Campeonato>> getCampeonatos();

    @Headers({"Authorization: Bearer " + Auth})
    @GET("campeonatos/{id}")
    Call<Campeonato> getCampeonato(@Path("id") String id);

    @Headers({"Authorization: Bearer " + Auth})
    @GET("campeonatos/{campeonato}/fases/{fase}")
    Call<Fase> getFase(@Path("campeonato") String campeonato, @Path("fase") String fase);

    @Headers({"Authorization: Bearer " + Auth})
    @GET("campeonatos/{campeonato}/fases/{fase}")
    Call<FaseSemVolta> getFaseSemVolta(@Path("campeonato") String campeonato, @Path("fase") String fase);

    @Headers({"Authorization: Bearer " + Auth})
    @GET("campeonatos/{campeonato}/rodadas/{rodada}")
    Call<Rodada> getRodada(@Path("campeonato") String campeonato, @Path("rodada") String rodada);




}
