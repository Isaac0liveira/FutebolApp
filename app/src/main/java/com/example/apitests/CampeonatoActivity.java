package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CampeonatoActivity extends AppCompatActivity {
    CampeonatoDAO dao;
    List<Campeonato> campeonatos = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    Api api;
    ListView listView;
    CampeonatoAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.deleteDatabase("tabela");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);
        dao = new CampeonatoDAO(this);
        listView = findViewById(R.id.listView);
        adapter = new CampeonatoAdapter(this, campeonatos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (campeonatos.get(position).isPlano()) {
                if (campeonatos.get(position).getFase_atual().getTipo().equals("pontos-corridos")) {
                    Intent intent = new Intent(this, TabelaActivity.class);
                    intent.putExtra("nome", campeonatos.get(position).getNome());
                    intent.putExtra("rodada", String.valueOf(campeonatos.get(position).getRodada_atual().getRodada()));
                    intent.putExtra("id", String.valueOf(campeonatos.get(position).getCampeonato_id()));
                    intent.putExtra("fase", String.valueOf(campeonatos.get(position).getFase_atual().getFase_id()));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, FaseActivity.class);
                    intent.putExtra("nome", campeonatos.get(position).getNome());
                    intent.putExtra("campeonato", String.valueOf(campeonatos.get(position).getCampeonato_id()));
                    intent.putExtra("fase", String.valueOf(campeonatos.get(position).getFase_atual().getFase_id()));
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Campeonato Não Disponível", Toast.LENGTH_SHORT).show();
            }
        });
        init();
        Toast.makeText(getApplicationContext(), "Carregando Campeonatos...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> {
            listView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            listView.invalidateViews();
            listView.setVisibility(View.VISIBLE);
        }, 3000);

        swipeRefreshLayout = findViewById(R.id.swipeRodada);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            dao.atualizar();
            campeonatos.clear();
            getCampeonatos();
        });
    }

    public void init() {
        if(swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
        if(dao.obterLista().size() == 0){
            getCampeonatos();
        }else {
            campeonatos.addAll(dao.obterLista());
            listView.invalidateViews();
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void getCampeonatos() {
        Call<List<Campeonato>> call = api.getCampeonatos();
        call.enqueue(new Callback<List<Campeonato>>() {
            @Override
            public void onResponse(Call<List<Campeonato>> call, Response<List<Campeonato>> response) {
                Log.d("Requisição", "Uma requisição foi feita ");
                if (response.code() == 200) {
                    List<Campeonato> campeonatoes = response.body();
                    getCampeonato(campeonatoes);
                } else {
                    Toast.makeText(CampeonatoActivity.this, "Não foi possível obter a lista de campeonatos, retorne mais tarde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Campeonato>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCampeonato(List<Campeonato> campeonatoes) {
        for (Campeonato c : campeonatoes) {
            Call<Campeonato> call = api.getCampeonato(String.valueOf(c.getCampeonato_id()));
            call.enqueue(new Callback<Campeonato>() {
                @Override
                public void onResponse(Call<Campeonato> call, Response<Campeonato> response) {
                    Log.d("Requisição", "Uma requisição foi feita: " + c.getCampeonato_id());
                    if (response.code() == 200) {
                        c.setPlano(true);
                        dao.adicionar(c);
                    } else {
                        c.setPlano(false);
                        dao.adicionar(c);
                    }
                }

                @Override
                public void onFailure(Call<Campeonato> call, Throwable t) { }
            });
        }
        new Handler().postDelayed(this::init, 1000);
    }
}