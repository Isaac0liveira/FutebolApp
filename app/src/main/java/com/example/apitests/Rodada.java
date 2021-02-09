package com.example.apitests;

import java.util.List;

public class Rodada {

    private String nome;
    private int rodada;
    private Rodada rodada_anterior;
    private Rodada proxima_rodada;
    private List<Partida> partidas;

    public class Partida{

        private int partida_id;
        private Time time_mandante;
        private Time time_visitante;
        private int placar_mandante;
        private int placar_visitante;
        private String status;
        private String data_realizacao;
        private String hora_realizacao;
        private Estadio estadio;


        public class Time{

            private String nome_popular;

            public String getNome_popular() {
                return nome_popular;
            }

            public void setNome_popular(String nome_popular) {
                this.nome_popular = nome_popular;
            }

        }

        public class Estadio{

            private String nome_popular;

            public String getNome_popular() {
                return nome_popular;
            }

            public void setNome_popular(String nome_popular) {
                this.nome_popular = nome_popular;
            }
        }

        public String getHora_realizacao() {
            return hora_realizacao;
        }

        public void setHora_realizacao(String hora_realizacao) {
            this.hora_realizacao = hora_realizacao;
        }

        public int getPartida_id() {
            return partida_id;
        }

        public void setPartida_id(int partida_id) {
            this.partida_id = partida_id;
        }

        public Time getTime_mandante() {
            return time_mandante;
        }

        public void setTime_mandante(Time time_mandante) {
            this.time_mandante = time_mandante;
        }

        public Time getTime_visitante() {
            return time_visitante;
        }

        public void setTime_visitante(Time time_visitante) {
            this.time_visitante = time_visitante;
        }

        public int getPlacar_mandante() {
            return placar_mandante;
        }

        public void setPlacar_mandante(int placar_mandante) {
            this.placar_mandante = placar_mandante;
        }

        public int getPlacar_visitante() {
            return placar_visitante;
        }

        public void setPlacar_visitante(int placar_visitante) {
            this.placar_visitante = placar_visitante;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getData_realizacao() {
            return data_realizacao;
        }

        public void setData_realizacao(String data_realizacao) {
            this.data_realizacao = data_realizacao;
        }

        public Estadio getEstadio() {
            return estadio;
        }

        public void setEstadio(Estadio estadio) {
            this.estadio = estadio;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRodada() {
        return rodada;
    }

    public void setRodada(int rodada) {
        this.rodada = rodada;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public Rodada getRodada_anterior() {
        return rodada_anterior;
    }

    public void setRodada_anterior(Rodada rodada_anterior) {
        this.rodada_anterior = rodada_anterior;
    }

    public Rodada getProxima_rodada() {
        return proxima_rodada;
    }

    public void setProxima_rodada(Rodada proxima_rodada) {
        this.proxima_rodada = proxima_rodada;
    }
}
