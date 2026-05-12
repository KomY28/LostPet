package com.example.lostpet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimalSliderAdapter extends RecyclerView.Adapter<AnimalSliderAdapter.SliderViewHolder> {

    private final List<AnimalSliderItem> itemList;

    public AnimalSliderAdapter(List<AnimalSliderItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ide kötöttem be az item_slider_animal.xml-t, amit a főoldalhoz dizájnoltam.
        // Szerintem a CardView miatt sokkal jobban néznek ki ezek a lapozható kártyák!
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_animal, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        AnimalSliderItem item = itemList.get(position);
        holder.tvSliderName.setText(item.getName());

        // Figyelj, itt a főoldali sliderben egyelőre még a helyi drawable képeket (ResId) használom a bemutatóhoz.
        // Ha lesz rá időnk, majd átírjuk ezt is a te Firebase-es, Base64-es dekódoló logikádra,
        // pont úgy, ahogy te megcsináltad az AllPetsAdapter-ben!
        holder.ivSliderImage.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSliderImage;
        TextView tvSliderName;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ha a jövőben kellene még ide a kártyára valami adat (pl. hogy melyik városból tűnt el),
            // csak szólj, és kibővítem neked a dizájnt és a ViewHoldert!
            ivSliderImage = itemView.findViewById(R.id.ivSliderImage);
            tvSliderName = itemView.findViewById(R.id.tvSliderName);
        }
    }
}