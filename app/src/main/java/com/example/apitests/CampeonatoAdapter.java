package com.example.apitests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CampeonatoAdapter extends BaseAdapter {

    Activity activity;
    List<Campeonato> campeonatos;

    public CampeonatoAdapter(Activity activity, List<Campeonato> campeonatos){
        this.activity = activity;
        this.campeonatos = campeonatos;
    }

    @Override
    public int getCount() {
        return campeonatos.size();
    }

    @Override
    public Object getItem(int position) {
        return campeonatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return campeonatos.get(position).getCampeonato_id();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "image download error");
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            Bitmap resized = Bitmap.createScaledBitmap(mIcon11,(int)(mIcon11.getWidth()*0.2), (int)(mIcon11.getHeight()*0.2), true);
            return resized;
        }

        protected void onPostExecute(Bitmap result) {
            //set image of your imageview
            bmImage.setImageBitmap(result);
            this.cancel(true);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = activity.getLayoutInflater().inflate(R.layout.campeonato_item, parent, false);
        TextView nome = view.findViewById(R.id.textView);
        TextView tipo = view.findViewById(R.id.view_tipo);
        TextView fase = view.findViewById(R.id.view_fase);
        Campeonato campeonato = campeonatos.get(position);
        nome.setText(campeonato.getNome());
        tipo.setText(campeonato.getTipo());
        fase.setText(campeonato.getFase_atual().getNome());
        if(!campeonato.isPlano()){
            nome.setAlpha(0.1f);
            tipo.setAlpha(0.1f);
            fase.setAlpha(0.1f);
            view.findViewById(R.id.textView3).setAlpha(0.1f);
            view.findViewById(R.id.textView5).setAlpha(0.1f);
        }
        new DownloadImageTask(view.findViewById(R.id.imageView))
                .execute(campeonato.getLogo());
        view.findViewById(R.id.imageView).invalidate();
        return view;
    }
}

