package com.example.apitests;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CampeonatoDAO {

    private SQLiteDatabase banco;
    private Conexao conexao;
    private Context context;

    public CampeonatoDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        this.context = context;
    }

    public List<Campeonato> obterLista(){
        List<Campeonato> campeonatos = new ArrayList<>();
        Cursor cursor = banco.query("campeonato", new String[]{"campeonato_id", "nome", "slug", "nome_popular", "logo", "tipo", "plano", "rodada_id", "edicao_id", "fase_atual_id"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            campeonatos.add(obterCampeonato(cursor));
        }
        cursor.close();
        return campeonatos;
    }

    public Campeonato obterCampeonato(Cursor cursor){
        Campeonato campeonato = new Campeonato();
        campeonato.setCampeonato_id(cursor.getInt(0));
        campeonato.setNome(cursor.getString(1));
        campeonato.setSlug(cursor.getString(2));
        campeonato.setNome_popular(cursor.getString(3));
        campeonato.setLogo(cursor.getString(4));
        campeonato.setTipo(cursor.getString(5));
        if(cursor.getInt(6) == 0){
            campeonato.setPlano(false);
        }else{
            campeonato.setPlano(true);
        }
        Cursor cursorRodada = banco.query("rodada", new String[]{"id", "rodada"}, null, null, null, null, null);
        while(cursorRodada.moveToNext()){
            if(cursorRodada.getInt(0) == cursor.getInt(7)){
                Campeonato.Rodada rodada = new Campeonato.Rodada();
                rodada.setRodada(cursorRodada.getInt(1));
                campeonato.setRodada_atual(rodada);
            }
        }
        cursorRodada.close();
        Cursor cursorEdicao = banco.query("edicao", new String[]{"edicao_id", "temporada", "nome", "nome_popular", "slug"}, null, null, null, null, null);
        while(cursorEdicao.moveToNext()){
            if(cursorEdicao.getInt(0) == cursor.getInt(8)){
                Campeonato.Edicao edicao = new Campeonato.Edicao();
                edicao.setEdicao_id(cursorEdicao.getInt(0));
                edicao.setTemporada(cursorEdicao.getString(1));
                edicao.setNome(cursorEdicao.getString(2));
                edicao.setNome_popular(cursorEdicao.getString(3));
                edicao.setSlug(cursorEdicao.getString(4));
                campeonato.setEdicao_atual(edicao);
            }
        }
        cursorEdicao.close();
        Cursor cursorFaseAtual = banco.query("fase_atual", new String[]{"fase_id", "tipo", "nome"}, null, null, null, null, null);
        while(cursorFaseAtual.moveToNext()){
            if(cursorFaseAtual.getInt(0) == cursor.getInt(9)){
                Campeonato.FaseAtual faseAtual = new Campeonato.FaseAtual();
                faseAtual.setFase_id(cursorFaseAtual.getInt(0));
                faseAtual.setTipo(cursorFaseAtual.getString(1));
                faseAtual.setNome(cursorFaseAtual.getString(2));
                campeonato.setFase_atual(faseAtual);
            }
        }
        cursorFaseAtual.close();
        return campeonato;
    }

    public long adicionar(Campeonato campeonato){
        ContentValues values = new ContentValues();
        values.put("campeonato_id", campeonato.getCampeonato_id());
        values.put("nome", campeonato.getNome());
        values.put("slug", campeonato.getSlug());
        values.put("nome_popular", campeonato.getNome_popular());
        values.put("logo", campeonato.getLogo());
        values.put("tipo", campeonato.getTipo());
        values.put("edicao_id", campeonato.getEdicao_atual().getEdicao_id());
        values.put("fase_atual_id", campeonato.getFase_atual().getFase_id());
        if(campeonato.isPlano()){
            values.put("plano", 1);
        }else{
            values.put("plano", 0);
        }
        ContentValues valuesEdicao = new ContentValues();
        if (campeonato.getRodada_atual() != null) {
            ContentValues valuesRodada = new ContentValues();
            valuesRodada.put("rodada", campeonato.getRodada_atual().getRodada());
            long trying =  banco.insert("rodada", null, valuesRodada);
            Log.d("inserting", String.valueOf(trying));
            values.put("rodada_id", trying);
        }
        valuesEdicao.put("edicao_id", campeonato.getEdicao_atual().getEdicao_id());
        valuesEdicao.put("temporada", campeonato.getEdicao_atual().getTemporada());
        valuesEdicao.put("nome", campeonato.getEdicao_atual().getNome());
        valuesEdicao.put("nome_popular", campeonato.getEdicao_atual().getNome_popular());
        valuesEdicao.put("slug", campeonato.getEdicao_atual().getSlug());
        ContentValues valuesFase = new ContentValues();
        valuesFase.put("fase_id", campeonato.getFase_atual().getFase_id());
        valuesFase.put("tipo", campeonato.getFase_atual().getTipo());
        valuesFase.put("nome", campeonato.getFase_atual().getNome());
        banco.insert("edicao", null, valuesEdicao);
        banco.insert("fase_atual", null, valuesFase);
        return banco.insert("campeonato", null, values);
    }

    public void atualizar(){
        Intent intent = new Intent(context, CampeonatoActivity.class);
        context.startActivity(intent);
        Toast.makeText(context, "Atualizando dados...", Toast.LENGTH_SHORT).show();
        banco.beginTransaction();
        context.deleteDatabase("campeonato.db");
        new Handler().postDelayed(() -> new Conexao(context), 500);
        banco.setTransactionSuccessful();
        banco.endTransaction();
        Log.d("size", String.valueOf(this.obterLista().size()));
    }

}
