package com.example.apitests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FaseDAO {

    private SQLiteDatabase banco;
    private Conexao conexao;
    private Context context;
    public FaseDAO(Context context) {
        conexao = new Conexao(context);
        this.context = context;
        banco = conexao.getWritableDatabase();
    }

    public void adicionar(Fase fase) {
        ContentValues values = new ContentValues();
        values.put("fase_id", fase.getFase_id());
        for (Fase.Chave c : fase.getChaves()) {
            ContentValues valuesChaves = new ContentValues();
            valuesChaves.put("campeonato_id", fase.getCampeonato().getCampeonato_id());
            valuesChaves.put("fase_id", fase.getFase_id());
            valuesChaves.put("nome", c.getNome());
            ContentValues valuesPartidaIda = new ContentValues();
            valuesPartidaIda.put("partida_id", c.getPartida_ida().getPartida_id());
            valuesPartidaIda.put("time_mandante", c.getPartida_ida().getTime_mandante().getNome_popular());
            valuesPartidaIda.put("time_visitante", c.getPartida_ida().getTime_visitante().getNome_popular());
            valuesPartidaIda.put("placar_mandante", c.getPartida_ida().getPlacar_mandante());
            valuesPartidaIda.put("placar_visitante", c.getPartida_ida().getPlacar_visitante());
            valuesPartidaIda.put("status", c.getPartida_ida().getStatus());
            valuesPartidaIda.put("data_realizacao", c.getPartida_ida().getData_realizacao());
            valuesPartidaIda.put("hora_realizacao", c.getPartida_ida().getHora_realizacao());
            valuesPartidaIda.put("estadio", c.getPartida_ida().getEstadio().getNome_popular());
            valuesChaves.put("partida_ida", banco.insert("partidas", null, valuesPartidaIda));

            ContentValues valuesPartidaVolta = new ContentValues();
            valuesPartidaVolta.put("partida_id", c.getPartida_volta().getPartida_id());
            valuesPartidaVolta.put("time_mandante", c.getPartida_volta().getTime_mandante().getNome_popular());
            valuesPartidaVolta.put("time_visitante", c.getPartida_volta().getTime_visitante().getNome_popular());
            valuesPartidaVolta.put("placar_mandante", c.getPartida_volta().getPlacar_mandante());
            valuesPartidaVolta.put("placar_visitante", c.getPartida_volta().getPlacar_visitante());
            valuesPartidaVolta.put("status", c.getPartida_volta().getStatus());
            valuesPartidaVolta.put("data_realizacao", c.getPartida_volta().getData_realizacao());
            valuesPartidaVolta.put("hora_realizacao", c.getPartida_volta().getHora_realizacao());
            valuesPartidaVolta.put("estadio", c.getPartida_volta().getEstadio().getNome_popular());
            valuesChaves.put("partida_volta", banco.insert("partidas", null, valuesPartidaVolta));
            banco.insert("chave", null, valuesChaves);
        }
        values.put("campeonato", fase.getCampeonato().getCampeonato_id());
        if(fase.getFase_anterior() != null) {
            values.put("fase_anterior", fase.getFase_anterior().getFase_id());
        }
        if(fase.getProxima_fase() != null) {
            values.put("proxima_fase", fase.getProxima_fase().getFase_id());
        }
        if(fase.isIda_e_volta()) {
            values.put("ida_e_volta", 1);
        }else{
            values.put("ida_e_volta", 0);
        }
        banco.insert("fase", null, values);
    }

    public void adicionarSemVolta(FaseSemVolta fase) {
        ContentValues values = new ContentValues();
        values.put("fase_id", fase.getFase_id());
        for (FaseSemVolta.Chave c : fase.getChaves()) {
            ContentValues valuesChaves = new ContentValues();
            valuesChaves.put("campeonato_id", fase.getCampeonato().getCampeonato_id());
            valuesChaves.put("fase_id", fase.getFase_id());
            valuesChaves.put("nome", c.getNome());
            ContentValues valuesPartidaIda = new ContentValues();
            valuesPartidaIda.put("partida_id", c.getPartida_ida().getPartida_id());
            valuesPartidaIda.put("time_mandante", c.getPartida_ida().getTime_mandante().getNome_popular());
            valuesPartidaIda.put("time_visitante", c.getPartida_ida().getTime_visitante().getNome_popular());
            valuesPartidaIda.put("placar_mandante", c.getPartida_ida().getPlacar_mandante());
            valuesPartidaIda.put("placar_visitante", c.getPartida_ida().getPlacar_visitante());
            valuesPartidaIda.put("status", c.getPartida_ida().getStatus());
            valuesPartidaIda.put("data_realizacao", c.getPartida_ida().getData_realizacao());
            valuesPartidaIda.put("hora_realizacao", c.getPartida_ida().getHora_realizacao());
            valuesPartidaIda.put("estadio", c.getPartida_ida().getEstadio().getNome_popular());
            valuesChaves.put("partida_ida", banco.insert("partidas", null, valuesPartidaIda));

            banco.insert("chave", null, valuesChaves);
        }
        values.put("campeonato", fase.getCampeonato().getCampeonato_id());
        values.put("fase_anterior", fase.getFase_anterior().getFase_id());
        values.put("proxima_fase", fase.getProxima_fase().getFase_id());
        if(fase.isIda_e_volta()) {
            values.put("ida_e_volta", 1);
        }else{
            values.put("ida_e_volta", 0);
        }
        banco.insert("fase", null, values);
    }

    public List<Fase> obterFase(String id){
        List<Fase> retorno = new ArrayList<>();
        Fase fase = new Fase();
        Cursor cursor = banco.rawQuery("select * from fase where fase_id = " + id, null);
        while (cursor.moveToNext()){
            fase.setFase_id(cursor.getInt(1));
            Cursor cursorCampeonato = banco.rawQuery("select * from campeonato where campeonato_id = " +  cursor.getInt(2), null);
            while (cursorCampeonato.moveToNext()){
                fase.setCampeonato(obterCampeonato(cursorCampeonato));
            }
            cursorCampeonato.close();
            Cursor cursorChave = banco.rawQuery("select * from chave where campeonato_id = " + fase.getCampeonato().getCampeonato_id() + " and fase_id = " +  fase.getFase_id(), null);
            List<Fase.Chave> chaves = new ArrayList<>();
            while(cursorChave.moveToNext()){
                Fase.Chave chave = new Fase.Chave();
                chave.setNome(cursor.getString(2));
                chave.setPartida_ida(obterPartida(String.valueOf(cursorChave.getInt(3))));
                chave.setPartida_volta(obterPartida(String.valueOf(cursorChave.getInt(4))));
                chaves.add(chave);
            }
            cursorChave.close();
            fase.setChaves(chaves);
            Fase faseAnterior = new Fase();
            faseAnterior.setFase_id(cursor.getInt(3));
            fase.setFase_anterior(faseAnterior);
            Fase proximaFase = new Fase();
            proximaFase.setFase_id(cursor.getInt(4));
            if(cursor.getInt(5) == 0) {
                fase.setIda_e_volta(false);
            }else{
                fase.setIda_e_volta(true);
            }
            break;
        }
        cursor.close();
        if(!fase.isIda_e_volta()){
            fase.setFase_id(-1);
            retorno.add(fase);
            return retorno;
        }
        if(fase.getCampeonato().getNome() == null){
            return null;
        }
        retorno.add(fase);
        return retorno;
    }

    public List<FaseSemVolta> obterFaseSemVolta(String id){
        List<FaseSemVolta> retorno = new ArrayList<>();
        FaseSemVolta fase = new FaseSemVolta();
        Cursor cursor = banco.rawQuery("select * from fase where fase_id = " + id, null);
        while (cursor.moveToNext()){
            fase.setFase_id(cursor.getInt(1));
            Cursor cursorCampeonato = banco.rawQuery("select * from campeonato where campeonato_id = " +  cursor.getInt(2), null);
            while (cursorCampeonato.moveToNext()){
                CampeonatoDAO campeonatoDAO = new CampeonatoDAO(context);
                fase.setCampeonato(campeonatoDAO.obterCampeonato(cursorCampeonato));
            }
            cursorCampeonato.close();
            Cursor cursorChave = banco.rawQuery("select * from chave where campeonato_id = " + fase.getCampeonato().getCampeonato_id() + " and fase_id = " +  fase.getFase_id(), null);
            List<FaseSemVolta.Chave> chaves = new ArrayList<>();
            while(cursorChave.moveToNext()){
                FaseSemVolta.Chave chave = new FaseSemVolta.Chave();
                chave.setNome(cursorChave.getString(2));
                chave.setPartida_ida(obterPartida(String.valueOf(cursorChave.getInt(3))));
                chaves.add(chave);
            }
            cursorChave.close();
            fase.setChaves(chaves);
            Fase faseAnterior = new Fase();
            faseAnterior.setFase_id(cursor.getInt(3));
            fase.setFase_anterior(faseAnterior);
            Fase proximaFase = new Fase();
            proximaFase.setFase_id(cursor.getInt(4));
            fase.setIda_e_volta(false);
        }
        cursor.close();
        if(fase.getCampeonato().getNome() == null){
            return null;
        }
        retorno.add(fase);
        return retorno;
    }

    public Rodada.Partida obterPartida(String id){
        Rodada.Partida partida = new Rodada.Partida();
        Cursor cursor = banco.rawQuery("select * from partidas where id = " + id, null);
        while (cursor.moveToNext()){
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
        }
        cursor.close();
        return partida;
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

    public void atualizar(){
        banco.beginTransaction();
        banco.execSQL("create table fase(id integer primary key autoincrement, " +
                "fase_id integer," +
                "campeonato integer," +
                "fase_anterior integer," +
                "proxima_fase integer," +
                "ida_e_volta bool)");
        banco.execSQL("create table chave(campeonato_id integer," +
                "fase_id integer," +
                "nome varchar(50)," +
                "partida_ida integer," +
                "partida_volta integer)");
        banco.delete("fase", null, null);
        banco.delete("chave", null, null);
        banco.setTransactionSuccessful();
        banco.endTransaction();
    }
}
