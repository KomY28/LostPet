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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rvCityList;
    private CityAdapter cityAdapter;
    private List<String> uniqueCities;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ide is bepattintottam a saját Vissza gombunkat, amit beszéltünk.
        // Így ebből a nézetből is simán vissza tudnak navigálni a te főoldaladra!
        android.widget.TextView tvBack = findViewById(R.id.tvBackBtn);
        tvBack.setOnClickListener(v -> finish());

        rvCityList = findViewById(R.id.rvCityList);
        rvCityList.setLayoutManager(new LinearLayoutManager(this));

        uniqueCities = new ArrayList<>();
        cityAdapter = new CityAdapter(uniqueCities);
        rvCityList.setAdapter(cityAdapter);

        // Itt is a közös MissingPets táblára csatlakozom, ahová te a FormActivity-ből mentesz.
        databaseReference = FirebaseDatabase.getInstance("https://lostpetapp-6806d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("MissingPets");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Figyelj, itt egy kis trükköt alkalmaztam: HashSet-et használok a szűréshez!
                // Mivel a Set alapból nem enged duplikált elemeket, hiába töltesz fel a Formodon
                // 5 darab állatot "Budapest" helyszínnel, a listában a város neve csak egyszer fog megjelenni.
                Set<String> citySet = new HashSet<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    // Itt használom az én kibővített AnimalItem modellemet a letöltéshez
                    AnimalItem pet = data.getValue(AnimalItem.class);
                    if (pet != null && pet.getLocation() != null) {
                        citySet.add(pet.getLocation());
                    }
                }

                // Miután a Set kiszűrte a duplikációkat, áttöltöm az adatokat a végleges listába,
                // és szólok az adapternek (CityAdapter), hogy frissítse a képernyőt.
                uniqueCities.clear();
                uniqueCities.addAll(citySet);
                cityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Hiba az adatok letöltésekor!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}