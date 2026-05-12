package com.example.lostpet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPagerAnimals;
    private PetAdapter petAdapter;
    private List<AnimalItem> petList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bekötöttem a gombokat. Raktam be egy újat is az "Összes állat" listádhoz,
        // hogy a főoldalról egyből el lehessen érni a te nézetedet is!
        Button btnOpenForm = findViewById(R.id.btnOpenForm);
        Button btnOpenSearch = findViewById(R.id.btnOpenSearch);
        Button btnViewAll = findViewById(R.id.btnViewAll);

        viewPagerAnimals = findViewById(R.id.viewPagerAnimals);

        // 1. Ez visz az én FormActivity-mre (Új bejelentés)
        btnOpenForm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });

        // 2. Itt hívom meg a te okos, térképes keresődet!
        btnOpenSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // 3. És itt nyitom meg a te AllPetsActivity-det, ahol listázod az összes állatot.
        btnViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllPetsActivity.class);
            startActivity(intent);
        });

        // Előkészítem a Slidert. Figyelj, itt végül a te AnimalItem modelledet használom,
        // hogy kompatibilis legyen a Firebase-es letöltéssel!
        petList = new ArrayList<>();
        petAdapter = new PetAdapter(petList);
        viewPagerAnimals.setAdapter(petAdapter);

        // Ugyanarra a Firebase URL-re csatlakozom, amit te is beállítottál.
        databaseReference = FirebaseDatabase.getInstance("https://lostpetapp-6806d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("MissingPets");

        // Kicsit optimalizáltam a kezdőlapot: hogy ne töltsön be azonnal 100+ állatot és ne fagyjon le,
        // a te adatbázisodból egy Query-vel lekérdezem csak a legújabb 5 bejelentést a sliderhez.
        Query lastFiveQuery = databaseReference.limitToLast(5);

        lastFiveQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    AnimalItem pet = postSnapshot.getValue(AnimalItem.class);
                    if (pet != null) {
                        // A 0. indexre rakom (add(0, pet)), hogy a legújabb jelenjen meg legelöl a sliderben!
                        petList.add(0, pet);
                    }
                }
                petAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Hiba az adatok letöltésekor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}