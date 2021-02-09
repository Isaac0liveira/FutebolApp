package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RodadaActivity extends AppCompatActivity {

    List<Rodada.Partida> partidas = new ArrayList<Rodada.Partida>();
    Api api;
    Retrofit retrofit;
    ListView listRodadas;
    RodadaAdapter adapter;
    String campeonato;
    String rodada;
    String nome;
    TextView rodadaAtual;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodada);

        retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);

        listRodadas = findViewById(R.id.listRodadas);
        adapter = new RodadaAdapter(this, partidas);
        listRodadas.setAdapter(adapter);

        Intent intent = getIntent();
        campeonato = (String) intent.getSerializableExtra("campeonato");
        rodada = (String) intent.getSerializableExtra("rodada");
        nome = (String) intent.getSerializableExtra("nome");

        this.setTitle(nome);

        rodadaAtual = findViewById(R.id.rodada_atual);
        rodadaAtual.setText("Rodada " + rodada);

        call(campeonato, rodada);
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void call(String novoCampeonato, String novaRodada){
        getSupportActionBar().setSubtitle("Rodada " + novaRodada);
        rodadaAtual = findViewById(R.id.rodada_atual);
        rodadaAtual.setText("Rodada " + novaRodada);
        Call<Rodada> callRodada = api.getRodada(novoCampeonato, novaRodada);
        callRodada.enqueue(new Callback<Rodada>() {
            @Override
            public void onResponse(Call<Rodada> call, Response<Rodada> response) {
                partidas.clear();
                if(response.code() == 200) {
                    Rodada proxima = null;
                    Rodada anterior = null;
                    if(response.body().getProxima_rodada() != null){
                        proxima = response.body().getProxima_rodada();
                    }
                    if(response.body().getRodada_anterior() != null) {
                        anterior = response.body().getRodada_anterior();
                    }
                    if(proxima != null){
                        TextView txtProxima = findViewById(R.id.txtProx);
                        txtProxima.setVisibility(View.VISIBLE);
                        Rodada finalProxima = proxima;
                        txtProxima.setOnClickListener(v -> {
                                call(campeonato, String.valueOf(finalProxima.getRodada()));
                        });
                    }else{
                        TextView txtProxima = findViewById(R.id.txtProx);
                        txtProxima.setVisibility(View.GONE);
                    }
                    if(anterior != null){
                        TextView txtAnterior = findViewById(R.id.txtAnt);
                        txtAnterior.setVisibility(View.VISIBLE);
                        Rodada finalAnterior = anterior;
                        txtAnterior.setOnClickListener(v -> {
                            call(campeonato, String.valueOf(finalAnterior.getRodada()));
                        });
                    }else{
                        TextView txtAnterior = findViewById(R.id.txtAnt);
                        txtAnterior.setVisibility(View.GONE);
                    }
                    partidas.addAll(response.body().getPartidas());
                    listRodadas.invalidateViews();
                }else{
                    showMessage("Não foi possível obter a rodada, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<Rodada> call, Throwable t) {
                Log.d("Errors", t.getMessage());
            }
        });
    }
}