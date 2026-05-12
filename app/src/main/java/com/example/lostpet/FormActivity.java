package com.example.lostpet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FormActivity extends AppCompatActivity {

    private EditText etName, etGender, etLocation, etPhone;
    private ImageView ivPreview;
    private String base64Image = "nincs_kep";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Betettem a Vissza gombot ide is, ahogy kérted, így mentés nélkül is
        // vissza tudnak ugrani a felhasználók az én főoldalamra!
        android.widget.TextView tvBack = findViewById(R.id.tvBackBtn);
        if (tvBack != null) {
            tvBack.setOnClickListener(v -> finish());
        }

        // Ugyanazt a Firebase hivatkozást használom, amit te is a listázásnál,
        // szóval minden egyből a 'MissingPets' táblába megy.
        databaseReference = FirebaseDatabase.getInstance("https://lostpetapp-6806d-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("MissingPets");

        etName = findViewById(R.id.etPetName);
        etGender = findViewById(R.id.etPetGender);
        etLocation = findViewById(R.id.etPetLocation);
        etPhone = findViewById(R.id.etPetPhone);
        ivPreview = findViewById(R.id.ivPetPreview);
        Button btnSelect = findViewById(R.id.btnSelectImage);
        Button btnSave = findViewById(R.id.btnSavePet);

        // Ez az Intent nyitja meg a telefon beépített galériáját. Csekkoltam, jól működik!
        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        });

        btnSave.setOnClickListener(v -> saveToFirebase());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ivPreview.setImageBitmap(bitmap);
                ivPreview.setVisibility(View.VISIBLE);

                // Itt hívom meg az átalakítót, hogy a Firebase sima Stringként tudja menteni.
                base64Image = encodeImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bm) {
        // Figyelj, itt tömörítem le a képet 50%-ra és nyomom át Base64-be,
        // hogy a te dekódoló logikád az AllPetsAdapter-ben simán meg tudja enni letöltéskor.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void saveToFirebase() {
        String name = etName.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Ide raktam egy gyors validációt, hogy ne tudjanak üres vagy hiányos
        // adatokat feltölteni a te listádba.
        if (name.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Név, helyszín és telefon kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();

        // Itt használom a te frissített AnimalItem konstruktorodat a telefonszámmal együtt!
        AnimalItem pet = new AnimalItem(id, name, gender, location, base64Image, phone);

        if (id != null) {
            databaseReference.child(id).setValue(pet).addOnSuccessListener(aVoid -> {
                Toast.makeText(FormActivity.this, "Sikeres mentés!", Toast.LENGTH_SHORT).show();
                // Ha sikeres a mentés az adatbázisba, bezárom a formot és visszadobom a főoldalra
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(FormActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}