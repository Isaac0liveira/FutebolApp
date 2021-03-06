package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    String fase;
    TextView rodadaAtual;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodada);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);
        dao = new RodadaDAO(this);
        listRodadas = findViewById(R.id.listRodadas);
        adapter = new RodadaAdapter(this, partidas);
        listRodadas.setAdapter(adapter);
        listRodadas.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar4);
        progressBar.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_in));
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        campeonato = (String) intent.getSerializableExtra("campeonato");
        rodada = (String) intent.getSerializableExtra("rodada");
        nome = (String) intent.getSerializableExtra("nome");
        fase = (String) intent.getSerializableExtra("fase");
        swipeRefreshLayout = findViewById(R.id.swipeRodada);
        swipeRefreshLayout.setOnRefreshListener(() -> new CampeonatoDAO(RodadaActivity.this).atualizar());
        this.setTitle(nome);
        rodadaAtual = findViewById(R.id.rodada_atual);
        rodadaAtual.setText("Rodada " + rodada);
        init(rodada);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }


    @SuppressLint("ClickableViewAccessibility")
    public void init(String rodadaID){
        Rodada copiaRodada;
        if(dao.obterRodada(rodadaID) == null){
            call(campeonato, rodadaID);
        }else {
            if (dao.obterRodada(rodadaID).size() != 0 && dao.obterRodada(rodadaID).get(0).getPartidas().size() != 0) {
                rodada = rodadaID;
                copiaRodada = dao.obterRodada(rodadaID).get(0);
                if (copiaRodada.getPartidas() != null) {
                    partidas.addAll(copiaRodada.getPartidas());
                }
                progressBar.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_out));
                progressBar.setVisibility(View.GONE);
                listRodadas.invalidateViews();
                new Handler().postDelayed(() -> {
                    listRodadas.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_in));
                    rodadaAtual.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_in));
                    listRodadas.setVisibility(View.VISIBLE);
                    rodadaAtual.setVisibility(View.VISIBLE);
                    getSupportActionBar().setSubtitle("Rodada " + rodadaID);
                    rodadaAtual.setText("Rodada " + rodadaID);
                }, 500);
                listRodadas.setOnTouchListener(new OnSwipeTouchListener(RodadaActivity.this) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSwipeRight() {
                        if (copiaRodada.getRodada_anterior() != null) {
                            if(copiaRodada.getRodada_anterior().getRodada() != 0) {
                                listRodadas.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_out));
                                rodadaAtual.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_out));
                                listRodadas.setVisibility(View.INVISIBLE);
                                rodadaAtual.setVisibility(View.INVISIBLE);
                                getSupportActionBar().setSubtitle("");
                                new Handler().postDelayed(() -> {
                                    partidas.clear();
                                    progressBar.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_in));
                                    progressBar.setVisibility(View.VISIBLE);
                                    init(String.valueOf(copiaRodada.getRodada_anterior().getRodada()));
                                    rodadaAtual.setText("Rodada " + copiaRodada.getRodada_anterior().getRodada());
                                }, 500);
                            }
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSwipeLeft() {
                        if (copiaRodada.getProxima_rodada() != null) {
                            if(copiaRodada.getProxima_rodada().getRodada() != 0) {
                                listRodadas.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_out));
                                rodadaAtual.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_out));
                                listRodadas.setVisibility(View.INVISIBLE);
                                rodadaAtual.setVisibility(View.INVISIBLE);
                                getSupportActionBar().setSubtitle("");
                                new Handler().postDelayed(() -> {
                                    partidas.clear();
                                    progressBar.startAnimation(AnimationUtils.loadAnimation(RodadaActivity.this, android.R.anim.fade_in));
                                    progressBar.setVisibility(View.VISIBLE);
                                    init(String.valueOf(copiaRodada.getProxima_rodada().getRodada()));
                                    rodadaAtual.setText("Rodada " + copiaRodada.getProxima_rodada().getRodada());
                                }, 500);
                            }
                        }
                    }
                });
            } else {
                call(campeonato, rodadaID);
            }
        }
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void call(String novoCampeonato, String novaRodada){
        Call<Rodada> callRodada = api.getRodada(novoCampeonato, novaRodada);
        callRodada.enqueue(new Callback<Rodada>() {
            @Override
            public void onResponse(Call<Rodada> call, Response<Rodada> response) {
                if(response.code() == 200) {
                    dao.adicionar(response.body());
                    init(novaRodada);
                }else{
                    showMessage("Não foi possível obter a rodada, retorne mais tarde");
                    if(rodada == novaRodada){
                        finish();
                    }else {
                        init(rodada);
                    }
                }
            }

            @Override
            public void onFailure(Call<Rodada> call, Throwable t) {
                Log.d("Errors", t.getMessage());
            }
        });
    }
}