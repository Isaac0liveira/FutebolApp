package com.example.apitests;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaseActivity extends AppCompatActivity {

    TextView faseAtual;
    Api api;
    Retrofit retrofit;
    ListView listChaves;
    FaseDAO dao;
    String campeonato;
    List<Fase.Chave> chaves = new ArrayList<Fase.Chave>();
    List<FaseSemVolta.Chave> chavesSemVolta = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fase);

        retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(Api.class);
        dao = new FaseDAO(this);
        listChaves = findViewById(R.id.list_chaves);
        listChaves.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
        listChaves.setVisibility(View.INVISIBLE);
        FaseAdapter adapter = new FaseAdapter(this, chaves);
        listChaves.setAdapter(adapter);

        Intent intent = getIntent();
        campeonato = (String) intent.getSerializableExtra("campeonato");
        String fase = (String) intent.getSerializableExtra("fase");
        String nome = (String) intent.getSerializableExtra("nome");

        this.setTitle(nome);

        init(fase);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void init(String faseID){
        getSupportActionBar().setSubtitle("Fase " + faseID);
        faseAtual = findViewById(R.id.fase_atual);
        faseAtual.setText("Fase " + faseID);
        if(dao.obterFase(faseID) == null){
            call(campeonato, faseID);
        }else if(dao.obterFase(faseID).get(0).getFase_id() == -1){


            if(dao.obterFaseSemVolta(faseID).size() != 0 && dao.obterFaseSemVolta(faseID).get(0).getChaves().size() != 0){
                FaseSemVolta copiaFase = dao.obterFaseSemVolta(faseID).get(0);
                if(copiaFase.getChaves() != null){
                    chavesSemVolta.addAll(copiaFase.getChaves());
                }
                Collections.sort(chavesSemVolta, new sortChaveSemVolta());
                listChaves.invalidateViews();
                listChaves.setOnTouchListener(new OnSwipeTouchListener(FaseActivity.this) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSwipeRight() {
                        if (copiaFase.getFase_anterior() != null) {
                            new Thread(() -> {
                                listChaves.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                                faseAtual.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                                listChaves.setVisibility(View.INVISIBLE);
                                faseAtual.setVisibility(View.INVISIBLE);
                            }).start();
                            Toast.makeText(FaseActivity.this, "left", Toast.LENGTH_SHORT).show();
                            call(campeonato, String.valueOf(copiaFase.getFase_anterior().getFase_id()));
                        }
                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSwipeLeft() {
                        if (copiaFase.getProxima_fase() != null) {
                            listChaves.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                            faseAtual.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                            Toast.makeText(FaseActivity.this, "right", Toast.LENGTH_SHORT).show();
                            call(campeonato, String.valueOf(copiaFase.getProxima_fase().getFase_id()));
                        }
                    }
                });
            }else{
                call(campeonato, faseID);
            }


        }else{


            if(dao.obterFase(faseID).size() != 0 && dao.obterFase(faseID).get(0).getChaves().size() != 0){
                Fase copiaFase = dao.obterFase(faseID).get(0);
                if(copiaFase.getChaves() != null){
                    chaves.addAll(copiaFase.getChaves());
                }
                Collections.sort(chaves, new sortChave());
                listChaves.invalidateViews();
                listChaves.setOnTouchListener(new OnSwipeTouchListener(FaseActivity.this) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSwipeRight() {
                        if (copiaFase.getFase_anterior() != null) {
                            new Thread(() -> {
                                listChaves.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                                faseAtual.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                                listChaves.setVisibility(View.INVISIBLE);
                                faseAtual.setVisibility(View.INVISIBLE);
                            }).start();
                            Toast.makeText(FaseActivity.this, "left", Toast.LENGTH_SHORT).show();
                            call(campeonato, String.valueOf(copiaFase.getFase_anterior().getFase_id()));
                        }
                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSwipeLeft() {
                        if (copiaFase.getProxima_fase() != null) {
                            listChaves.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                            faseAtual.startAnimation(AnimationUtils.loadAnimation(FaseActivity.this, android.R.anim.fade_out));
                            Toast.makeText(FaseActivity.this, "right", Toast.LENGTH_SHORT).show();
                            call(campeonato, String.valueOf(copiaFase.getProxima_fase().getFase_id()));
                        }
                    }
                });
            }else{
                call(campeonato, faseID);
            }


        }
    }

    public void call(String novoCampeonato, String novaFase) {
        Call<Fase> callFase = api.getFase(novoCampeonato, novaFase);
        callFase.enqueue(new Callback<Fase>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResponse(Call<Fase> call, Response<Fase> response) {
                if (response.code() == 200) {
                    dao.adicionar(response.body());
                    init(novaFase);
                } else {
                    showMessage("Não foi possível obter a fase, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<Fase> call, Throwable t) {
                Log.d("Errors", t.getMessage());
                callSemVolta(novoCampeonato, novaFase);
            }
        });
    }

    public void callSemVolta(String novoCampeonato, String novaFase) {
        FaseSemVoltaAdapter adapter = new FaseSemVoltaAdapter(this, chavesSemVolta);
        listChaves.setAdapter(adapter);
        Call<FaseSemVolta> callFase = api.getFaseSemVolta(novoCampeonato, novaFase);
        callFase.enqueue(new Callback<FaseSemVolta>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResponse(Call<FaseSemVolta> call, Response<FaseSemVolta> response) {
                chavesSemVolta.clear();
                if (response.code() == 200) {
                    dao.adicionarSemVolta(response.body());
                    init(novaFase);
                } else {
                    showMessage("Não foi possível obter a fase, retorne mais tarde");
                }
            }

            @Override
            public void onFailure(Call<FaseSemVolta> call, Throwable t) {
                Log.d("Errorsss", t.getMessage());
            }
        });
    }

    class sortChave implements Comparator<Fase.Chave> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Fase.Chave a, Fase.Chave b) {
            return Integer.parseInt(a.getNome().replaceAll("\\D+", "")) - Integer.parseInt(b.getNome().replaceAll("\\D+", ""));
        }
    }

    class sortChaveSemVolta implements Comparator<FaseSemVolta.Chave> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(FaseSemVolta.Chave a, FaseSemVolta.Chave b) {
            return Integer.parseInt(a.getNome().replaceAll("\\D+", "")) - Integer.parseInt(b.getNome().replaceAll("\\D+", ""));
        }
    }



    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}