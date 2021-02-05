package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabelaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        Api api = retrofit.create(Api.class);
        Intent intent = getIntent();
        String path = (String) intent.getSerializableExtra("id");
        Call<Fase> call = api.getFase(path);
        call.enqueue(new Callback<Fase>() {
            @Override
            public void onResponse(Call<Fase> call, Response<Fase> response) {
                Log.d("Response", String.valueOf(call.request()));
                Fase.Tabela tabela = response.body().getTabela();
                Log.d("Tabela", Arrays.toString(tabela.getUltimos_jogos()));
            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {

            }
        });
    }
}