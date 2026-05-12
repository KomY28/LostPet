package com.example.lostpet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<AnimalItem> petList;

    // Végül átírtam ezt az adaptert is, hogy a te AnimalItem modelledet használja!
    // Így tökéletesen kompatibilis a te Firebase lekérdezéseddel a főoldalon.
    public PetAdapter(List<AnimalItem> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Itt a főoldali slider kártyájának dizájnját (item_pet) húzom be.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        AnimalItem pet = petList.get(position);

        holder.tvName.setText(pet.getName());
        holder.tvGender.setText("Neme: " + pet.getGender());
        holder.tvLocation.setText("Helyszín: " + pet.getLocation());

        // Átemeltem ide is a Base64 dekódoló logikádat, amit az AllPetsAdapter-hez írtál!
        // Zseniális, hogy így a formról feltöltött képek egyből megjelennek a főoldalamon is.
        String base64Image = pet.getImageUrl();
        if (base64Image != null && !base64Image.equals("nincs_kep")) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.ivImage.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // --- TÉRKÉP GOMB LOGIKA ---
        // Kölcsönvettem a térképes Intent-edet is, remélem nem gond! :D
        // Így már innen, a kezdőlapi lapozóból is egyből a Google Maps-re tudnak ugrani a felhasználók.
        holder.btnShowMap.setOnClickListener(v -> {
            String location = pet.getLocation();
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            mapIntent.setPackage("com.google.android.apps.maps");

            v.getContext().startActivity(mapIntent);
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGender, tvLocation;
        ImageView ivImage;
        Button btnShowMap;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvGender = itemView.findViewById(R.id.tvItemGender);
            tvLocation = itemView.findViewById(R.id.tvItemLocation);
            ivImage = itemView.findViewById(R.id.ivItemImage);

            // A térkép gombot bekötöttem ide a holderbe. A hívás gombot ide a sliderbe nem raktam be,
            // azt meghagytam a te részletes listád (AllPetsActivity) extrájának!
            btnShowMap = itemView.findViewById(R.id.btnShowMap);
        }
    }
}