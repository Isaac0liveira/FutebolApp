package com.example.apitests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class TabelaAdapter extends BaseAdapter {

    List<Fase.Tabela> tabela;
    Activity activity;

    public TabelaAdapter(Activity activity, List<Fase.Tabela> tabela){
        this.tabela = tabela;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return tabela.size();
    }

    @Override
    public Object getItem(int position) {
        return tabela.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tabela.get(position).getPosicao();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = activity.getLayoutInflater().inflate(R.layout.tabela_item, parent, false);
        TextView posicao = view.findViewById(R.id.pos);
        TextView time = view.findViewById(R.id.time);
        TextView pts = view.findViewById(R.id.pts);
        TextView jogos = view.findViewById(R.id.jogos);
        TextView vitorias = view.findViewById(R.id.vitorias);
        TextView empates = view.findViewById(R.id.empates);
        TextView derrotas = view.findViewById(R.id.derrotas);
        TextView golPro = view.findViewById(R.id.golPro);
        TextView golContra = view.findViewById(R.id.golContra);
        TextView saldoGols = view.findViewById(R.id.saldoGols);
        TextView aproveitamento = view.findViewById(R.id.aproveitamento);
        TextView recentes = view.findViewById(R.id.recentes);

        Fase.Tabela t = tabela.get(position);

        posicao.setText(String.valueOf(t.getPosicao()));
        time.setText(t.getTime().getNome_popular());
        pts.setText(String.valueOf(t.getPontos()));
        jogos.setText(String.valueOf(t.getJogos()));
        vitorias.setText(String.valueOf(t.getVitorias()));
        empates.setText(String.valueOf(t.getEmpates()));
        derrotas.setText(String.valueOf(t.getDerrotas()));
        golPro.setText(String.valueOf(t.getGols_pro()));
        golContra.setText(String.valueOf(t.getGols_contra()));
        saldoGols.setText(String.valueOf(t.getSaldo_gols()));
        aproveitamento.setText(String.valueOf(t.getAproveitamento()));
        recentes.setText(Arrays.toString(t.getUltimos_jogos()));


        return view;
    }
}
