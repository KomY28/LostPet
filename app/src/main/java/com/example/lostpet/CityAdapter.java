package com.example.lostpet;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<String> cityList;

    // Itt kapja meg az adapter a SearchActivity-ből azt a városlistát,
    // amit a HashSet-tel már megtisztítottam a duplikációktól.
    public CityAdapter(List<String> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ide bekötöttem a város kártyák dizájnját (item_city).
        // Igyekeztem olyan stílusra formázni, mint amilyen a te főoldali dizájnod!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        String cityName = cityList.get(position);
        holder.tvCityName.setText(cityName);

        // Na, ez az a térkép hívás, amiről meséltem!
        // Nem kellett beépíteni a nehéz Google Maps SDK-t, hanem egy okos Intent-tel
        // átadjuk az operációs rendszernek a várost, és ő megnyitja a gyári térkép appot.
        holder.btnCityMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(cityName));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            v.getContext().startActivity(mapIntent);
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName;
        Button btnCityMap;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            btnCityMap = itemView.findViewById(R.id.btnCityMap);
        }
    }
}