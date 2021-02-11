package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
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
            if(campeonatos.get(position).isPlano()){
                if(campeonatos.get(position).getFase_atual().getTipo().equals("pontos-corridos")){
                    Intent intent = new Intent(this, TabelaActivity.class);
                    intent.putExtra("nome", campeonatos.get(position).getNome());
                    intent.putExtra("rodada", String.valueOf(campeonatos.get(position).getRodada_atual().getRodada()));
                    intent.putExtra("id", String.valueOf(campeonatos.get(position).getCampeonato_id()));
                    intent.putExtra("fase", String.valueOf(campeonatos.get(position).getFase_atual().getFase_id()));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, FaseActivity.class);
                    intent.putExtra("nome", campeonatos.get(position).getNome());
                    intent.putExtra("campeonato", String.valueOf(campeonatos.get(position).getCampeonato_id()));
                    intent.putExtra("fase", String.valueOf(campeonatos.get(position).getFase_atual().getFase_id()));
                    startActivity(intent);
                }
            }else{
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

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            campeonatos.clear();
            getCampeonatos();
        });
    }

    public void init(){
        if(dao.obterLista().size() == 0) {
            getCampeonatos();
        }else{
            campeonatos.clear();
            List<Campeonato> campeonatoes = dao.obterLista();
            campeonatos.addAll(campeonatoes);
            listView.invalidateViews();
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void getCampeonatos(){
        dao.atualizar();
        Call<List<Campeonato>> call = api.getCampeonatos();
        call.enqueue(new Callback<List<Campeonato>>() {
            @Override
            public void onResponse(Call<List<Campeonato>> call, Response<List<Campeonato>> response) {
                if(response.code() == 200) {
                    List<Campeonato> campeonatoes = response.body();
                    int pos = 0;
                    listView.setVisibility(View.INVISIBLE);
                    for (Campeonato c : campeonatoes) {
                        getCampeonato(String.valueOf(c.getCampeonato_id()));
                        pos++;
                        if(pos == campeonatoes.size()){
                            if(swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                                listView.invalidateViews();
                            }
                            init();
                        }
                    }
                }else{
                    showMessage("Não foi possível obter a lista de campeonatos, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<List<Campeonato>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCampeonato(String id){
        Call<Campeonato> call = api.getCampeonato(id);

        call.enqueue(new Callback<Campeonato>() {
            @Override
            public void onResponse(Call<Campeonato> call, Response<Campeonato> response) {
                if(response.code() == 200) {
                    Campeonato compare = response.body();
                    Log.d("Retorno", response.body().toString());
                    int pos = 0;
                    for (Campeonato c : campeonatos) {
                        if ((compare != null ? compare.getCampeonato_id() : -1) == c.getCampeonato_id()) {
                            campeonatos.get(pos).setPlano(true);
                        }
                        pos++;
                    }
                }else{
                    for(Campeonato c: campeonatos){
                        if(Integer.parseInt(id) == c.getCampeonato_id()){
                            c.setPlano(false);
                            Log.d("Retorno", c.getNome_popular() + "== false");
                            Log.d("Response", String.valueOf(response.code()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Campeonato> call, Throwable t) {

            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}