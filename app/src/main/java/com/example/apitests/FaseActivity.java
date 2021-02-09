package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaseActivity extends AppCompatActivity {

    List<Fase.Chave> chaves= new ArrayList<Fase.Chave>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fase);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Api api = retrofit.create(Api.class);

        ListView listChaves = findViewById(R.id.list_chaves);
        FaseAdapter adapter = new FaseAdapter(this, chaves);
        listChaves.setAdapter(adapter);

        Intent intent = getIntent();
        String campeonato = (String) intent.getSerializableExtra("campeonato");
        String fase = (String) intent.getSerializableExtra("fase");
        String nome = (String) intent.getSerializableExtra("nome");

        this.setTitle(nome);
        getSupportActionBar().setSubtitle("Fase " + fase);

        TextView faseAtual = findViewById(R.id.fase_atual);
        faseAtual.setText("Fase " + fase);

        Call<Fase> callRodada = api.getFase(campeonato, fase);
        callRodada.enqueue(new Callback<Fase>() {
            @Override
            public void onResponse(Call<Fase> call, Response<Fase> response) {
                if(response.code() == 200) {
                    chaves.addAll(response.body().getChaves());
                    listChaves.invalidateViews();
                }else{
                    showMessage("Não foi possível obter a fase, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {
                Log.d("Errors", t.getMessage());
            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}