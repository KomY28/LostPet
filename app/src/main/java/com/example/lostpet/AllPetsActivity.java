package com.example.lostpet;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllPetsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllPetsAdapter adapter;
    private List<AnimalItem> allPetList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pets);

        // Beraktam ide is azt a vissza gomb logikát, amit megbeszéltünk,
        // így már innen is szépen vissza tudunk ugrani a te főoldaladra.
        android.widget.TextView tvBack = findViewById(R.id.tvBackBtn);
        tvBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rvAllPets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Itt inicializálom a listát, ami várja az adatokat a Firebase-ből
        allPetList = new ArrayList<>();
        adapter = new AllPetsAdapter(allPetList);
        recyclerView.setAdapter(adapter);

        // Kérlek a DB linket ne írd át, ezt már bekötöttem a végleges MissingPets táblához!
        databaseReference = FirebaseDatabase.getInstance("https://lostpetapp-6806d-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("MissingPets");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allPetList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    AnimalItem pet = data.getValue(AnimalItem.class);
                    if (pet != null) {
                        allPetList.add(pet);
                    }
                }

                // Figyelj, ha a FormActivity-dből mentesz egy új állatot,
                // ez a notifyDataSetChanged azonnal frissíti a listát, nem kell újraindítani az appot!
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Ha gond lenne a nettel, dobunk egy Toastot, hogy ne fagyjon ki a felület.
                Toast.makeText(AllPetsActivity.this, "Hiba az adatok letöltésekor!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}