package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TempoRVAdapter extends RecyclerView.Adapter<TempoRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TempoRVModal> tempoRVModalArrayList;

    public TempoRVAdapter(Context context, ArrayList<TempoRVModal> tempoRVModalArrayList) {
        this.context = context;
        this.tempoRVModalArrayList = tempoRVModalArrayList;
    }

    @NonNull
    @Override
    public TempoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tempo_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempoRVAdapter.ViewHolder holder, int position) {

        TempoRVModal modal = tempoRVModalArrayList.get(position);
        holder.temperaturaTV.setText(modal.getTemperatura()+"ºC");
        Picasso.get().load("http:".concat(modal.getIcone())).into(holder.condicaoIV);
        holder.ventoTV.setText(modal.getVelocidadeDoVento()+"km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try
        {
            Date t = input.parse(modal.getHora());
            holder.horaTV.setText(output.format(t));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return tempoRVModalArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ventoTV, temperaturaTV, horaTV;
        private ImageView condicaoIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ventoTV = itemView.findViewById(R.id.idTVVelocidadeDoVento);
            temperaturaTV = itemView.findViewById(R.id.idTVTemperatura);
            horaTV = itemView.findViewById(R.id.idTVHora);
            condicaoIV = itemView.findViewById(R.id.idTVCondicao);


        }
    }
}
