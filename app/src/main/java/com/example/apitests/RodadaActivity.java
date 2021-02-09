package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodada);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);

        ListView listRodadas = findViewById(R.id.listRodadas);
        RodadaAdapter adapter = new RodadaAdapter(this, partidas);
        listRodadas.setAdapter(adapter);

        Intent intent = getIntent();
        String campeonato = (String) intent.getSerializableExtra("campeonato");
        String rodada = (String) intent.getSerializableExtra("rodada");
        String nome = (String) intent.getSerializableExtra("nome");

        this.setTitle(nome);
        getSupportActionBar().setSubtitle("Rodada " + rodada);

        TextView rodadaAtual = findViewById(R.id.rodada_atual);
        rodadaAtual.setText("Rodada " + rodada);

        Call<Rodada> callRodada = api.getRodada(campeonato, rodada);
        callRodada.enqueue(new Callback<Rodada>() {
            @Override
            public void onResponse(Call<Rodada> call, Response<Rodada> response) {
                if(response.code() == 200) {
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

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}