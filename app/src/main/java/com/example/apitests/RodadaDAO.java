package com.example.apitests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RodadaDAO {

    private SQLiteDatabase banco;
    private Conexao conexao;

    public RodadaDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public void adicionar(Rodada rodada){
        ContentValues values = new ContentValues();
        values.put("rodada", rodada.getRodada());
        values.put("nome", rodada.getNome());
        values.put("rodada_anterior", rodada.getRodada_anterior().getRodada());
        values.put("proxima_rodada", rodada.getProxima_rodada().getRodada());
        values.put("partidas", rodada.getRodada());
        for(Rodada.Partida p: rodada.getPartidas()){
            ContentValues valuesPartida = new ContentValues();
            valuesPartida.put("partida_id", p.getPartida_id());
            valuesPartida.put("time_mandante", p.getTime_mandante().getNome_popular());
            valuesPartida.put("time_visitante", p.getTime_visitante().getNome_popular());
            valuesPartida.put("placar_mandante", p.getPlacar_mandante());
            valuesPartida.put("placar_visitante", p.getPlacar_visitante());
            valuesPartida.put("status", p.getStatus());
            valuesPartida.put("data_realizacao", p.getData_realizacao());
            valuesPartida.put("hora_realizacao", p.getHora_realizacao());
            valuesPartida.put("estadio", p.getEstadio().getNome_popular());
            valuesPartida.put("rodada_id", rodada.getRodada());
            Log.d("Rodada", rodada.getRodada() + "");
            banco.insert("partidas", null, valuesPartida);
        }
        banco.insert("rodada", null, values);
    }

    public List<Rodada> obterRodada(String id){
        List<Rodada> retorno = new ArrayList<>();
        Rodada rodada = new Rodada();
        Cursor cursor = banco.rawQuery("select * from rodada where rodada = " + id, null);
        while (cursor.moveToNext()){
            rodada.setRodada(cursor.getInt(1));
            rodada.setNome(cursor.getString(2));
            Rodada anterior = new Rodada();
            anterior.setRodada(cursor.getInt(3));
            rodada.setRodada_anterior(anterior);
            Rodada proxima = new Rodada();
            proxima.setRodada(cursor.getInt(4));
            rodada.setProxima_rodada(proxima);
            rodada.setPartidas(obterPartidas(id));
        }
        if(rodada.getNome() == null){
            return null;
        }
        retorno.add(rodada);
        return retorno;
    }

    public List<Rodada.Partida> obterPartidas(String id){
        List<Rodada.Partida> partidas = new ArrayList<>();
        Cursor cursor = banco.rawQuery("select * from partidas where rodada_id = " + id, null);
        while (cursor.moveToNext()){
            Rodada.Partida partida = new Rodada.Partida();
            partida.setPartida_id(cursor.getInt(0));
            Rodada.Partida.Time mandante = new Rodada.Partida.Time();
            mandante.setNome_popular(cursor.getString(1));
            partida.setTime_mandante(mandante);
            Rodada.Partida.Time visitante = new Rodada.Partida.Time();
            visitante.setNome_popular(cursor.getString(2));
            partida.setTime_visitante(visitante);
            partida.setPlacar_mandante(cursor.getInt(3));
            partida.setPlacar_visitante(cursor.getInt(4));
            partida.setStatus(cursor.getString(5));
            partida.setData_realizacao(cursor.getString(6));
            partida.setHora_realizacao(cursor.getString(7));
            Rodada.Partida.Estadio estadio = new Rodada.Partida.Estadio();
            estadio.setNome_popular(cursor.getString(8));
            partida.setEstadio(estadio);
            partidas.add(partida);
        }
        return partidas;
    }

    public void atualizarRodada(String rodada){
        banco.beginTransaction();
        banco.setTransactionSuccessful();
        banco.endTransaction();
    }

    public void atualizarPartida(){
        banco.beginTransaction();
        banco.delete("partidas", null,null);
        banco.execSQL("drop table partidas");
        banco.execSQL("create table partidas(id integer primary key autoincrement," +
                "partida_id integer," +
                "time_mandante varchar(50)," +
                "time_visitante varchar(50)," +
                "placar_mandante integer," +
                "placar_visitante integer," +
                "status varchar(50)," +
                "data_realizacao varchar(50)," +
                "hora_realizacao varchar(50)," +
                "estadio varchar(50)," +
                "rodada_id integer)");
        banco.setTransactionSuccessful();
        banco.endTransaction();
    }

}
