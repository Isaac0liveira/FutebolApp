package com.example.apitests;

import java.io.Serializable;

public class Fase implements Serializable {

    private Tabela tabela;

    class Tabela implements Serializable {
        private int posicao;
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
    }

    public Tabela getTabela() {
        return tabela;
    }

    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }
}
