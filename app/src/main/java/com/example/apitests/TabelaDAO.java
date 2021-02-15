package com.example.apitests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabelaDAO {

    private SQLiteDatabase banco;
    private Conexao conexao;

    public TabelaDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public void adicionar(Fase tabela){
        for(Fase.Tabela t: tabela.getTabela()){
            ContentValues valuesLinha = new ContentValues();
            valuesLinha.put("tabela_id", tabela.getCampeonato().getCampeonato_id());
            valuesLinha.put("posicao", t.getPosicao());
            valuesLinha.put("nome_popular", t.getTime().getNome_popular());
            valuesLinha.put("pontos", t.getPontos());
            valuesLinha.put("jogos", t.getJogos());
            valuesLinha.put("vitorias", t.getVitorias());
            valuesLinha.put("empates", t.getEmpates());
            valuesLinha.put("derrotas", t.getDerrotas());
            valuesLinha.put("gols_pro", t.getGols_pro());
            valuesLinha.put("gols_contra", t.getGols_contra());
            valuesLinha.put("saldo_gols", t.getSaldo_gols());
            valuesLinha.put("aproveitamento", t.getVariacao_posicao());
            valuesLinha.put("ultimos_jogos", Arrays.toString(t.getUltimos_jogos()));
            banco.insert("tabela", null, valuesLinha);
        }
    }


    public List<Fase.Tabela> obterTabela(String id){
        List<Fase.Tabela> tabela = new ArrayList<>();
        Cursor cursor = banco.query("tabela", new String[]{"tabela_id","posicao","nome_popular","pontos","jogos","vitorias","empates","derrotas","gols_pro","gols_contra","saldo_gols","aproveitamento","ultimos_jogos"},null,null,null,null,null);
        while (cursor.moveToNext()){
            if(cursor.getInt(0) == Integer.parseInt(id)) {
                Fase.Tabela selectTabela = new Fase.Tabela();
                selectTabela.setPosicao(cursor.getInt(1));
                Fase.Tabela.Time time = new Fase.Tabela.Time();
                time.setNome_popular(cursor.getString(2));
                selectTabela.setTime(time);
                selectTabela.setPontos(cursor.getInt(3));
                selectTabela.setJogos(cursor.getInt(4));
                selectTabela.setVitorias(cursor.getInt(5));
                selectTabela.setEmpates(cursor.getInt(6));
                selectTabela.setDerrotas(cursor.getInt(7));
                selectTabela.setGols_pro(cursor.getInt(8));
                selectTabela.setGols_contra(cursor.getInt(9));
                selectTabela.setSaldo_gols(cursor.getInt(10));
                selectTabela.setAproveitamento(cursor.getInt(11));
                selectTabela.setV_ultimos_jogos(cursor.getString(12));
                tabela.add(selectTabela);
            }
        }
        return tabela;
    }

    public void atualizar(){
        banco.beginTransaction();
        banco.delete("tabela", null,null);
        banco.execSQL("drop table tabela");
        banco.execSQL("create table tabela(tabela_id integer," +
                "posicao integer," +
                "nome_popular varchar(50)," +
                "pontos integer," +
                "jogos integer," +
                "vitorias integer," +
                "empates integer," +
                "derrotas integer," +
                "gols_pro integer," +
                "gols_contra integer," +
                "saldo_gols integer," +
                "aproveitamento float," +
                "variacar_posicao integer," +
                "ultimos_jogos varchar(50))");
        banco.setTransactionSuccessful();
        banco.endTransaction();
    }

}
