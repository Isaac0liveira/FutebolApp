package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
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
    RodadaDAO dao;
    Retrofit retrofit;
    ListView listRodadas;
    RodadaAdapter adapter;
    String campeonato;
    String rodada;
    String nome;
    TextView rodadaAtual;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodada);
        retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);
        dao = new RodadaDAO(this);
        dao.atualizarRodada();
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
        swipeRefreshLayout = findViewById(R.id.swipeRodada);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            dao.atualizarRodada();
            dao.atualizarPartida();
            partidas.clear();
            call(campeonato, rodada);
        });
        init(rodada);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(String rodadaID){
        Rodada copiaRodada = dao.obterRodada(rodadaID);
        partidas.addAll(copiaRodada.getPartidas());
        listRodadas.invalidateViews();
        listRodadas.setOnTouchListener(new OnSwipeTouchListener(RodadaActivity.this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSwipeRight() {
                if (copiaRodada.getRodada_anterior() != null) {
                    new Thread(() -> {
                        partidas.clear();
                        init(String.valueOf(copiaRodada.getRodada_anterior().getRodada()));
                        getSupportActionBar().setSubtitle("Rodada " + copiaRodada.getRodada_anterior().getRodada());
                        rodadaAtual.setText("Rodada " + copiaRodada.getRodada_anterior().getRodada());
                    }).start();
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSwipeLeft() {
                if ( copiaRodada.getProxima_rodada() != null) {
                    new Thread(() -> {
                        partidas.clear();
                        init(String.valueOf(copiaRodada.getProxima_rodada().getRodada()));
                        getSupportActionBar().setSubtitle("Rodada " + copiaRodada.getProxima_rodada().getRodada());
                        rodadaAtual.setText("Rodada " + copiaRodada.getProxima_rodada().getRodada());
                    }).start();
                }
            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void call(String novoCampeonato, String novaRodada){
        getSupportActionBar().setSubtitle("Rodada " + novaRodada);
        rodadaAtual.setText("Rodada " + novaRodada);
        Call<Rodada> callRodada = api.getRodada(novoCampeonato, novaRodada);
        callRodada.enqueue(new Callback<Rodada>() {
            @Override
            public void onResponse(Call<Rodada> call, Response<Rodada> response) {
                if(response.code() == 200) {
                    dao.adicionar(response.body());
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