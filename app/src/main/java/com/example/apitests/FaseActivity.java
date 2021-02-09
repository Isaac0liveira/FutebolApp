package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaseActivity extends AppCompatActivity {

    List<Fase.Chave> chaves= new ArrayList<Fase.Chave>();
    TextView faseAtual;
    Api api;
    Retrofit retrofit;
    ListView listChaves;
    FaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fase);

        retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);

        listChaves = findViewById(R.id.list_chaves);
        adapter = new FaseAdapter(this, chaves);
        listChaves.setAdapter(adapter);

        Intent intent = getIntent();
        String campeonato = (String) intent.getSerializableExtra("campeonato");
        String fase = (String) intent.getSerializableExtra("fase");
        String nome = (String) intent.getSerializableExtra("nome");

        this.setTitle(nome);
        getSupportActionBar().setSubtitle("Fase " + fase);

        faseAtual = findViewById(R.id.fase_atual);
        faseAtual.setText("Fase " + fase);

        call(campeonato, fase);
    }

    public void call(String novoCampeonato, String novaFase){
        getSupportActionBar().setSubtitle("Fase " + novaFase);
        faseAtual = findViewById(R.id.fase_atual);
        faseAtual.setText("Fase " + novaFase);
        Call<Fase> callFase = api.getFase(novoCampeonato, novaFase);
        callFase.enqueue(new Callback<Fase>() {
            @Override
            public void onResponse(Call<Fase> call, Response<Fase> response) {
                chaves.clear();
                if(response.code() == 200) {
                    Fase proxima = null;
                    Fase anterior = null;
                    if(response.body().getProxima_fase() != null){
                        proxima = response.body().getProxima_fase();
                    }
                    if(response.body().getFase_anterior() != null) {
                        anterior = response.body().getFase_anterior();
                    }
                    if(proxima != null){
                        TextView txtProxima = findViewById(R.id.txtProxFase);
                        txtProxima.setVisibility(View.VISIBLE);
                        Fase finalProxima = proxima;
                        txtProxima.setOnClickListener(v -> {
                            call(novoCampeonato, String.valueOf(finalProxima.getFase_id()));
                        });
                    }else{
                        TextView txtProxima = findViewById(R.id.txtProxFase);
                        txtProxima.setVisibility(View.GONE);
                    }
                    if(anterior != null){
                        TextView txtAnterior = findViewById(R.id.txtAntFase);
                        txtAnterior.setVisibility(View.VISIBLE);
                        Fase finalAnterior = anterior;
                        txtAnterior.setOnClickListener(v -> {
                            call(novoCampeonato, String.valueOf(finalAnterior.getFase_id()));
                        });
                    }else{
                        TextView txtAnterior = findViewById(R.id.txtAntFase);
                        txtAnterior.setVisibility(View.GONE);
                    }
                    chaves.addAll(response.body().getChaves());
                    Collections.sort(chaves, new sortChave());
                    listChaves.invalidateViews();
                }else{
                    showMessage("Não foi possível obter a fase, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {
                Log.d("Errors", t.getMessage());
            }
        });
    }

    class sortChave implements Comparator<Fase.Chave>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Fase.Chave a, Fase.Chave b)
        {
            return Integer.parseInt(a.getNome().replaceAll("\\D+","")) - Integer.parseInt(b.getNome().replaceAll("\\D+",""));
        }
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}