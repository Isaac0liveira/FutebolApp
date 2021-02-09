package com.example.apitests;

import java.io.Serializable;

public class Campeonato implements Serializable {
    private int campeonato_id;
    private String nome;
    private String slug;
    private String nome_popular;
    private Edicao edicao_atual;
    private FaseAtual fase_atual;
    private Rodada rodada_atual;
    private String logo;
    private String tipo;
    private boolean plano;

    public class Rodada{
        private int rodada;

        public int getRodada() {
            return rodada;
        }

        public void setRodada(int rodada) {
            this.rodada = rodada;
        }
    }


    public class Edicao {
        private int edicao_id;
        private String temporada;
        private String nome;
        private String nome_popular;
        private String slug;

        public int getEdicao_id() {
            return edicao_id;
        }

        public void setEdicao_id(int edicao_id) {
            this.edicao_id = edicao_id;
        }

        public String getTemporada() {
            return temporada;
        }

        public void setTemporada(String temporada) {
            this.temporada = temporada;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getNome_popular() {
            return nome_popular;
        }

        public void setNome_popular(String nome_popular) {
            this.nome_popular = nome_popular;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }

    public class FaseAtual {
        private String fase_id;
        private String tipo;
        private String nome;

        public String getFase_id() {
            return fase_id;
        }

        public void setFase_id(String fase_id) {
            this.fase_id = fase_id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    }

    public Rodada getRodada_atual() {
        return rodada_atual;
    }

    public void setRodada_atual(Rodada rodada_atual) {
        this.rodada_atual = rodada_atual;
    }

    public FaseAtual getFase_atual() {
        return fase_atual;
    }

    public void setFase_atual(FaseAtual fase_atual) {
        this.fase_atual = fase_atual;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Edicao getEdicao_atual() {
        return edicao_atual;
    }

    public void setEdicao_atual(Edicao edicao_atual) {
        this.edicao_atual = edicao_atual;
    }

    public int getCampeonato_id() {
        return campeonato_id;
    }

    public void setCampeonato_id(int campeonato_id) {
        this.campeonato_id = campeonato_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNome_popular() {
        return nome_popular;
    }

    public void setNome_popular(String nome_popular) {
        this.nome_popular = nome_popular;
    }

    public boolean isPlano() {
        return plano;
    }

    public void setPlano(boolean plano) {
        this.plano = plano;
    }

    @Override
    public String toString() {
        return nome;
    }
}