package com.example.apitests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class FaseSemVoltaAdapter extends BaseAdapter {

    List<FaseSemVolta.Chave> chaves;
    Activity activity;

    public FaseSemVoltaAdapter(Activity activity, List<FaseSemVolta.Chave> fase){
        this.activity = activity;
        this.chaves = fase;
    }

    @Override
    public int getCount() {
        return chaves.size();
    }

    @Override
    public Object getItem(int position) {
        return chaves.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = activity.getLayoutInflater().inflate(R.layout.fase_item, parent, false);

        TextView chave = v.findViewById(R.id.txtChave);

        TextView mandante = v.findViewById(R.id.txtMandanteIda);
        TextView visitante = v.findViewById(R.id.txtVisitanteIda);
        TextView golMandante = v.findViewById(R.id.golMandanteIda);
        TextView golVisitante = v.findViewById(R.id.golVisitanteIda);



        TextView mandanteVolta = v.findViewById(R.id.txtMandanteVolta);
        TextView visitanteVolta = v.findViewById(R.id.txtVisitanteVolta);
        TextView golMandanteVolta = v.findViewById(R.id.golMandanteVolta);
        TextView golVisitanteVolta = v.findViewById(R.id.golVisitanteVolta);


        TextView txtStatus = v.findViewById(R.id.txtStatusIda);
        TextView txtData = v.findViewById(R.id.txtDataIda);
        TextView txtEstadio = v.findViewById(R.id.txtEstadioIda);
        TextView txtHora = v.findViewById(R.id.txtHoraIda);

        TextView txtStatusVolta = v.findViewById(R.id.txtStatusVolta);
        TextView txtDataVolta = v.findViewById(R.id.txtDataVolta);
        TextView txtEstadioVolta = v.findViewById(R.id.txtEstadioVolta);
        TextView txtHoraVolta = v.findViewById(R.id.txtHoraVolta);

        Rodada.Partida partidaIda = chaves.get(position).getPartida_ida();


        chave.setText(chaves.get(position).getNome());

        mandante.setText(partidaIda.getTime_mandante().getNome_popular());
        visitante.setText(partidaIda.getTime_visitante().getNome_popular());
        golMandante.setText(String.valueOf(partidaIda.getPlacar_mandante()));
        golVisitante.setText(String.valueOf(partidaIda.getPlacar_visitante()));
        if(partidaIda.getStatus().equals("finalizado")){
            txtStatus.setText("Finalizado");
            txtStatus.setTextColor(Color.BLUE);
        }else{
            txtStatus.setText("Agendado");
            txtStatus.setTextColor(Color.green(10));
        }
        txtData.setText(partidaIda.getData_realizacao());
        txtHora.setText(partidaIda.getHora_realizacao());
        txtEstadio.setText(partidaIda.getEstadio().getNome_popular());


        mandanteVolta.setVisibility(View.GONE);
        visitanteVolta.setVisibility(View.GONE);
        golMandanteVolta.setVisibility(View.GONE);
        golVisitanteVolta.setVisibility(View.GONE);
        txtStatusVolta.setVisibility(View.GONE);
        txtDataVolta.setVisibility(View.GONE);
        txtHoraVolta.setVisibility(View.GONE);
        txtEstadioVolta.setVisibility(View.GONE);

        return v;
    }
}

