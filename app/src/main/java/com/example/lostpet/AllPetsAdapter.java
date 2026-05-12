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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllPetsAdapter extends RecyclerView.Adapter<AllPetsAdapter.AllPetViewHolder> {

    private List<AnimalItem> petList;

    public AllPetsAdapter(List<AnimalItem> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public AllPetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Köszi a dizájnt az item_all_pet.xml-hez! Itt fűzöm hozzá az adapterhez, a kártyák nagyon jól néznek ki.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_pet, parent, false);
        return new AllPetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPetViewHolder holder, int position) {
        AnimalItem pet = petList.get(position);

        holder.tvName.setText(pet.getName());
        holder.tvGender.setText("Neme: " + pet.getGender());
        holder.tvLocation.setText("Helyszín: " + pet.getLocation());

        // Na, itt dekódolom vissza a képet, amit a FormActivity-dben Base64-be alakítottál.
        // Tök jó, hogy így toljuk fel a Firebase-be, sokkal stabilabb, mintha külön Storage-ot kéne bekötni!
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

        // Térkép integráció. Teszteltem a telómon, egyből a Google Maps-be dob, és ráközelít arra a városra, amit a formon megadtak.
        holder.btnMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(pet.getLocation()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            v.getContext().startActivity(mapIntent);
        });

        // Hívás gomb. Még jó, hogy utólag beraktad a telefonszám mezőt az Új bejelentéshez,
        // így már egy kattintással át tudom dobni a számot a rendszer tárcsázójába!
        holder.btnCall.setOnClickListener(v -> {
            String phone = pet.getPhone();
            if (phone != null && !phone.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                v.getContext().startActivity(intent);
            } else {
                // Ha valaki valamiért mégis üresen hagyná, dobunk egy Toastot, hogy ne fagyjon ki.
                Toast.makeText(v.getContext(), "Nincs megadva telefonszám!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class AllPetViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGender, tvLocation;
        ImageView ivImage;
        Button btnMap, btnCall;

        public AllPetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvAllPetName);
            tvGender = itemView.findViewById(R.id.tvAllPetGender);
            tvLocation = itemView.findViewById(R.id.tvAllPetLocation);
            ivImage = itemView.findViewById(R.id.ivAllPetImage);
            btnMap = itemView.findViewById(R.id.btnAllMap);
            btnCall = itemView.findViewById(R.id.btnAllCall);
        }
    }
}