package com.example.lostpet;

public class AnimalItem {
    private String id;
    private String name;
    private String gender;
    private String location;
    private String imageUrl;

    // Beletettem a telefonszám mezőt is, ahogy megbeszéltük, így a hívás gomb az adapterben már működni fog.
    private String phone;

    // Ezt az üres konstruktort mindenképp benne kell hagynunk,
    // különben a Firebase nem tudja deszerializálni az általad feltöltött JSON adatokat.
    public AnimalItem() {
    }

    // Itt a teljes konstruktor. Kérlek, a FormActivity-ben is eszerint használd,
    // amikor példányosítod az új állatot a mentésnél!
    public AnimalItem(String id, String name, String gender, String location, String imageUrl, String phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.imageUrl = imageUrl;
        this.phone = phone;
    }

    // Getterek és Setterek (Ezek is kötelezőek a Firebase miatt, szóval kérlek ne töröld ki őket,
    // még ha nem is használjuk mindet közvetlenül a kódban!)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}