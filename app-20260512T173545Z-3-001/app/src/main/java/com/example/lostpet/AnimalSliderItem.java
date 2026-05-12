package com.example.lostpet;

// Ez az én kis lokális modellem a főoldali lapozóhoz (Slider).
// Tudom, hogy te a távoli adatbázishoz a rendes AnimalItem-et használod a sok adattal,
// de a bemutatóra a kezdőlapon egyelőre megtartom ezt az egyszerűsített verziót, hogy tuti gyorsan betöltsön.
public class AnimalSliderItem {

    private String name;

    // Figyelj, itt egyelőre a res/drawable mappából töltöm be a fix tesztképeket (ezért int a típus).
    // Ha majd a védés után fejlesztjük tovább az appot, ezt a részt is lecseréljük
    // a te Base64-es dekódoló logikádra, hogy ide is a Firebase-ből jöjjenek a fotók!
    private int imageResId;

    public AnimalSliderItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}