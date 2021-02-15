package com.example.apitests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    private static final String nome = "campeonato.db";
    private static final int version = 1;

    public Conexao(Context context) {
        super(context, nome, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table campeonato(campeonato_id integer primary key," +
                "nome varchar(50)," +
                "slug varchar(50)," +
                "nome_popular varchar(50)," +
                "logo varchar(500)," +
                "tipo varchar(50)," +
                "plano integer," +
                "rodada_id integer," +
                "edicao_id integer," +
                "fase_atual_id integer," +
                "FOREIGN KEY (rodada_id) REFERENCES rodada (id)," +
                "FOREIGN KEY (edicao_id) REFERENCES edicao(edicao_id)," +
                "FOREIGN KEY (fase_atual_id) REFERENCES fase_atual (fase_id))");

        db.execSQL("create table edicao(edicao_id integer primary key," +
                "temporada varchar(50)," +
                "nome varchar(50)," +
                "nome_popular varchar(50)," +
                "slug varchar(50))");

        db.execSQL("create table fase_atual(fase_id integer primary key," +
                "tipo varchar(50)," +
                "nome varchar(50))");


        db.execSQL("create table tabela(tabela_id integer," +
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

        db.execSQL("create table rodada(id integer primary key autoincrement," +
                "rodada integer," +
                "nome varchar(50)," +
                "rodada_anterior integer," +
                "proxima_rodada integer," +
                "partidas integer)");

        db.execSQL("create table partidas(id primary key, " +
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



        db.execSQL("create table fase (id integer primary key autoincrement, " +
                "fase_id integer," +
                "campeonato integer," +
                "fase_anterior integer," +
                "proxima_fase integer," +
                "ida_e_volta bool)");

        db.execSQL("create table chave (campeonato_id integer," +
                "fase_id integer," +
                "nome varchar(50)," +
                "partida_ida integer," +
                "partida_volta integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
