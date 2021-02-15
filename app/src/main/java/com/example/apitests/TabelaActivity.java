package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    Api api;
    TabelaDAO dao;
    Retrofit retrofit;
    ListView gridView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabela);
        retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);
        Intent intent = getIntent();
        dao = new TabelaDAO(this);
        nome = (String) intent.getSerializableExtra("nome");
        rodada = (String) intent.getSerializableExtra("rodada");
        id = (String) intent.getSerializableExtra("id");
        fase = (String) intent.getSerializableExtra("fase");
        gridView = findViewById(R.id.gridView);
        progressBar = findViewById(R.id.progressBar2);
        gridView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        TextView rodadaTxt = findViewById(R.id.rodadaTxt);
        rodadaTxt.setOnClickListener(v -> {
           if(rodada != null){
               Intent newIntent = new Intent(TabelaActivity.this, RodadaActivity.class);
               newIntent.putExtra("campeonato", id);
               newIntent.putExtra("rodada", rodada);
               newIntent.putExtra("nome", nome);
               startActivity(newIntent);
           }
        });
        TabelaAdapter adapter = new TabelaAdapter(this, tabela);
        gridView.setAdapter(adapter);
        swipeRefreshLayout = findViewById(R.id.swipeRodada);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            gridView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            dao.atualizar();
            tabela.clear();
            getTabela();
        });

        this.setTitle(nome);
        getSupportActionBar().setSubtitle("Tabela");
        init();
    }


    public void init(){
        if(swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
        if(dao.obterTabela(id).size() == 0){
            getTabela();
        }else {
            tabela.addAll(dao.obterTabela(id));
            gridView.invalidateViews();
            gridView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void getTabela(){
        Call<Fase> call = api.getFase(id, fase);
        call.enqueue(new Callback<Fase>() {
            @Override
            public void onResponse(Call<Fase> call, Response<Fase> response) {
                if(response.code() == 200) {
                    dao.adicionar(response.body());
                }else{
                    Toast.makeText(TabelaActivity.this, "Não foi possível obter a tabela, retorne mais tarde", Toast.LENGTH_SHORT).show();
                }
                init();
            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {
                Log.d("Erro", t.getMessage());
            }
        });
    }
}