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
                "CONSTRAINT fk_rodada FOREIGN KEY (rodada_id) REFERENCES rodada (rodada)," +
                "CONSTRAINT fk_edicao FOREIGN KEY (edicao_id) REFERENCES edicao(edicao_id)," +
                "CONSTRAINT fk_fase_atual FOREIGN KEY (fase_atual_id) REFERENCES fase_atual (fase_id))");

        db.execSQL("create table edicao(edicao_id integer primary key," +
                "temporada varchar(50)," +
                "nome varchar(50)," +
                "nome_popular varchar(50)," +
                "slug varchar(50))");

        db.execSQL("create table fase_atual(fase_id integer primary key," +
                "tipo varchar(50)," +
                "nome varchar(50))");


        db.execSQL("create table tabela(posicao integer," +
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
                "ultimos_jogos varchar(50)," +
                "id_fase integer)");

        db.execSQL("create table rodada(rodada integer primary key," +
                "nome varchar(50))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
