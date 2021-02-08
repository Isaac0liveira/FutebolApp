package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabelaActivity extends AppCompatActivity {

    List<Fase.Tabela> tabela = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);


        Intent intent = getIntent();
        String nome = (String) intent.getSerializableExtra("nome");
        String rodada = (String) intent.getSerializableExtra("rodada");
        String id = (String) intent.getSerializableExtra("id");
        String fase = (String) intent.getSerializableExtra("fase");


        GridView gridView = findViewById(R.id.gridView);
        TextView rodadaTxt = findViewById(R.id.rodadaTxt);
        rodadaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TabelaAdapter adapter = new TabelaAdapter(this, tabela);
        gridView.setAdapter(adapter);


        this.setTitle(nome);
        getSupportActionBar().setSubtitle("Tabela");


        Call<Fase> call = api.getFase(id, fase);
        Log.d("request", call.request().toString());
        call.enqueue(new Callback<Fase>() {
            @Override
            public void onResponse(Call<Fase> call, Response<Fase> response) {

                Log.d("Response", String.valueOf(call.request()));
                Fase fase = response.body();
                List<Fase.Tabela> tabelaRes = fase.getTabela();
                tabela.addAll(tabelaRes);
                gridView.invalidateViews();

            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {
                Log.d("Erro", t.getMessage());
            }
        });

    }
}