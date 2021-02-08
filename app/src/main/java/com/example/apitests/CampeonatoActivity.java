package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CampeonatoActivity extends AppCompatActivity {

    Api api;
    ArrayAdapter adapter;
    List<Campeonato> campeonatos = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        api = retrofit.create(Api.class);

        listView = findViewById(R.id.listView);
        getCampeonatos();
        CampeonatoAdapter adapter = new CampeonatoAdapter(this, campeonatos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if(campeonatos.get(position).isPlano()){
                Toast.makeText(getApplicationContext(), "Incluído no Plano", Toast.LENGTH_SHORT).show();
                if(campeonatos.get(position).getTipo().equals("Pontos Corridos")){
                    Intent intent = new Intent(this, TabelaActivity.class);
                    intent.putExtra("nome", campeonatos.get(position).getNome());
                    intent.putExtra("rodada", String.valueOf(campeonatos.get(position).getRodada_atual().getRodada()));
                    intent.putExtra("id", String.valueOf(campeonatos.get(position).getCampeonato_id()));
                    intent.putExtra("fase", String.valueOf(campeonatos.get(position).getFase_atual().getFase_id()));
                    startActivity(intent);
                }
            }else{
                Toast.makeText(getApplicationContext(), "NÃO incluído no Plano", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "Carregando Campeonatos...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> {
            listView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            listView.setVisibility(View.VISIBLE);
            listView.invalidateViews();
        }, 3000);
    }

    public void getCampeonatos(){
        Call<List<Campeonato>> call = api.getCampeonatos();

        call.enqueue(new Callback<List<Campeonato>>() {
            @Override
            public void onResponse(Call<List<Campeonato>> call, Response<List<Campeonato>> response) {
                List<Campeonato> campeonatoes = response.body();
                campeonatos.addAll(campeonatoes);
                Log.d("response", response.body().toString());
                int pos = 0;
                listView.setVisibility(View.INVISIBLE);
                for(Campeonato c: campeonatoes){
                    getCampeonato(String.valueOf(c.getCampeonato_id()), pos);
                    pos++;
                }
            }

            @Override
            public void onFailure(Call<List<Campeonato>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCampeonato(String id, int pos){
        Call<Campeonato> call = api.getCampeonato(id);

        call.enqueue(new Callback<Campeonato>() {
            @Override
            public void onResponse(Call<Campeonato> call, Response<Campeonato> response) {
                if(response.code() != 401) {
                    Campeonato compare = response.body();
                    Log.d("Retorno", response.toString());
                    for (Campeonato c : campeonatos) {
                        if ((compare != null ? compare.getCampeonato_id() : -1) == c.getCampeonato_id()) {
                            c.setPlano(true);
                            Log.d("Retorno", c.getNome_popular() + "== true");
                        }
                    }
                }else{
                    for(Campeonato c: campeonatos){
                        if(Integer.parseInt(id) == c.getCampeonato_id()){
                            c.setPlano(false);
                            Log.d("Retorno", c.getNome_popular() + "== false");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Campeonato> call, Throwable t) {

            }
        });
    }

}