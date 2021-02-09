package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
    String nome;
    String rodada;
    String id;
    String fase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);


        Intent intent = getIntent();
        nome = (String) intent.getSerializableExtra("nome");
        rodada = (String) intent.getSerializableExtra("rodada");
        id = (String) intent.getSerializableExtra("id");
        fase = (String) intent.getSerializableExtra("fase");


        GridView gridView = findViewById(R.id.gridView);
        TextView rodadaTxt = findViewById(R.id.rodadaTxt);
        rodadaTxt.setOnClickListener(v -> {
           if(rodada != null){
               changeActivity();
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
                if(response.code() == 200) {
                    Log.d("Response", String.valueOf(call.request()));
                    Fase fase = response.body();
                    List<Fase.Tabela> tabelaRes = fase.getTabela();
                    tabela.addAll(tabelaRes);
                    gridView.invalidateViews();
                }else{
                    showMessage("Não foi possível obter a tabela, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {
                Log.d("Erro", t.getMessage());
            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void changeActivity(){
        Intent newIntent = new Intent(this, RodadaActivity.class);
        newIntent.putExtra("campeonato", id);
        newIntent.putExtra("rodada", rodada);
        newIntent.putExtra("nome", nome);
        startActivity(newIntent);
    }
}