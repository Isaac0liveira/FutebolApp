package com.example.apitests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RodadaAdapter extends BaseAdapter {

    List<Rodada.Partida> partidas;
    Activity activity;

    public RodadaAdapter(Activity activity, List<Rodada.Partida> partidas){
        this.activity = activity;
        this.partidas = partidas;
    }

    @Override
    public int getCount() {
        return partidas.size();
    }

    @Override
    public Object getItem(int position) {
        return partidas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return partidas.get(position).getPartida_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = activity.getLayoutInflater().inflate(R.layout.rodada_item, parent, false);
        TextView mandante = v.findViewById(R.id.txtMandante);
        TextView visitante = v.findViewById(R.id.txtVisitante);
        TextView golMandante = v.findViewById(R.id.golMandante);
        TextView golVisitante = v.findViewById(R.id.golVisitante);

        TextView txtStatus = v.findViewById(R.id.txtStatus);
        TextView txtData = v.findViewById(R.id.txtData);
        TextView txtHora = v.findViewById(R.id.txtHora);
        TextView estadio = v.findViewById(R.id.estadioRodada);

        Rodada.Partida partida = partidas.get(position);

        mandante.setText(partida.getTime_mandante().getNome_popular());
        visitante.setText(partida.getTime_visitante().getNome_popular());
        golMandante.setText(String.valueOf(partida.getPlacar_mandante()));
        golVisitante.setText(String.valueOf(partida.getPlacar_visitante()));
        if(partida.getStatus().equals("finalizado")){
            txtStatus.setText("Finalizado");
            txtStatus.setTextColor(Color.BLUE);
        }else{
            txtStatus.setText("Agendado");
            txtStatus.setTextColor(Color.GREEN);
        }
        txtData.setText(partida.getData_realizacao());
        txtHora.setText(partida.getHora_realizacao());
        estadio.setText(partida.getEstadio().getNome_popular());

        return v;
    }
}
