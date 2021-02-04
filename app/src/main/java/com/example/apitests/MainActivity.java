package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Api api;
    ArrayAdapter adapter;
    List<Campeonato> campeonatos;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        api = retrofit.create(Api.class);

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        getCampeonatos();

        /*for(int pos = 0; pos < campeonatos.size(); pos++){
            if(!campeonatos.get(pos).isPlano()){
                Log.d("Camp", campeonatos.get(pos).getNome());
                View item = (View) adapter.getView(pos, null, listView);
                item.setAlpha(0.1f);
            }
        }
        */

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if(campeonatos.get(position).isPlano()){
                Toast.makeText(getApplicationContext(), "Incluído no Plano", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "NÃO incluído no Plano", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getCampeonatos(){
        Call<List<Campeonato>> call = api.getCampeonatos();

        call.enqueue(new Callback<List<Campeonato>>() {
            @Override
            public void onResponse(Call<List<Campeonato>> call, Response<List<Campeonato>> response) {

                List<Campeonato> campeonatoes = response.body();
                campeonatos = campeonatoes;
                Log.d("response", response.body().toString());
                for(Campeonato c: campeonatoes){
                    adapter.add(c);
                    getCampeonato(String.valueOf(c.getCampeonato_id()));
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