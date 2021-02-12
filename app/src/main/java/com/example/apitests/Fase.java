package com.example.apitests;

import java.io.Serializable;
import java.util.List;

public class Fase implements Serializable {
    private String fase_id;
    private List<Tabela> tabela;
    private List<Chave> chaves;
    private Campeonato campeonato;
    private Fase fase_anterior;
    private Fase proxima_fase;
    private boolean ida_e_volta;

    public class Chave{
        private String nome;
        private Rodada.Partida partida_ida;
        private Rodada.Partida partida_volta;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Rodada.Partida getPartida_ida() {
            return partida_ida;
        }

        public void setPartida_ida(Rodada.Partida partida_ida) {
            this.partida_ida = partida_ida;
        }

        public Rodada.Partida getPartida_volta() {
            return partida_volta;
        }

        public void setPartida_volta(Rodada.Partida partida_volta) {
            this.partida_volta = partida_volta;
        }
    }

    public static class Tabela {
        private int posicao;
        private Time time;
        private int pontos;
        private int jogos;
        private int vitorias;
        private int empates;
        private int derrotas;
        private int gols_pro;
        private int gols_contra;
        private int saldo_gols;
        private float aproveitamento;
        private int variacao_posicao;
        private String[] ultimos_jogos;
        private String v_ultimos_jogos;

        public static class Time{
            private String nome_popular;

            public String getNome_popular() {
                return nome_popular;
            }

            public void setNome_popular(String nome_popular) {
                this.nome_popular = nome_popular;
            }
        }

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public int getPosicao() {
            return posicao;
        }

        public void setPosicao(int posicao) {
            this.posicao = posicao;
        }

        public int getPontos() {
            return pontos;
        }

        public void setPontos(int pontos) {
            this.pontos = pontos;
        }

        public int getJogos() {
            return jogos;
        }

        public void setJogos(int jogos) {
            this.jogos = jogos;
        }

        public int getVitorias() {
            return vitorias;
        }

        public void setVitorias(int vitorias) {
            this.vitorias = vitorias;
        }

        public int getEmpates() {
            return empates;
        }

        public void setEmpates(int empates) {
            this.empates = empates;
        }

        public int getDerrotas() {
            return derrotas;
        }

        public void setDerrotas(int derrotas) {
            this.derrotas = derrotas;
        }

        public int getGols_pro() {
            return gols_pro;
        }

        public void setGols_pro(int gols_pro) {
            this.gols_pro = gols_pro;
        }

        public int getGols_contra() {
            return gols_contra;
        }

        public void setGols_contra(int gols_contra) {
            this.gols_contra = gols_contra;
        }

        public int getSaldo_gols() {
            return saldo_gols;
        }

        public void setSaldo_gols(int saldo_gols) {
            this.saldo_gols = saldo_gols;
        }

        public float getAproveitamento() {
            return aproveitamento;
        }

        public void setAproveitamento(float aproveitamento) {
            this.aproveitamento = aproveitamento;
        }

        public int getVariacao_posicao() {
            return variacao_posicao;
        }

        public void setVariacao_posicao(int variacao_posicao) {
            this.variacao_posicao = variacao_posicao;
        }

        public String[] getUltimos_jogos() {
            return ultimos_jogos;
        }

        public void setUltimos_jogos(String[] ultimos_jogos) {
            this.ultimos_jogos = ultimos_jogos;
        }

        public String getV_ultimos_jogos() {
            return v_ultimos_jogos;
        }

        public void setV_ultimos_jogos(String v_ultimos_jogos) {
            this.v_ultimos_jogos = v_ultimos_jogos;
        }
    }

    public String getFase_id() {
        return fase_id;
    }

    public void setFase_id(String fase_id) {
        this.fase_id = fase_id;
    }

    public List<Tabela> getTabela() {
        return tabela;
    }

    public void setTabela(List<Tabela> tabela) {
        this.tabela = tabela;
    }

    public List<Chave> getChaves() {
        return chaves;
    }

    public void setChaves(List<Chave> chaves) {
        this.chaves = chaves;
    }

    public Fase getFase_anterior() {
        return fase_anterior;
    }

    public void setFase_anterior(Fase fase_anterior) {
        this.fase_anterior = fase_anterior;
    }

    public Fase getProxima_fase() {
        return proxima_fase;
    }

    public void setProxima_fase(Fase proxima_fase) {
        this.proxima_fase = proxima_fase;
    }

    public boolean isIda_e_volta() {
        return ida_e_volta;
    }

    public void setIda_e_volta(boolean ida_e_volta) {
        this.ida_e_volta = ida_e_volta;
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }
}
