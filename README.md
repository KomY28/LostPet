# LostPet 🐾

An Android application designed to help communities find and report lost pets. Users can easily upload missing pet reports with photos, search by city, and directly contact the finders. The app is powered by Firebase Realtime Database for seamless, real-time synchronization.

## 🚀 Key Features

* **Real-time Feed:** View all missing pets instantly without refreshing the app.
* **Smart Search:** Filter lost pets by city. The app uses `HashSet` optimization to ensure duplicate cities are removed from the search list.
* **Interactive Sliders:** A modern, swipeable ViewPager2 slider on the home screen showing the latest reports.
* **Quick Contact:** Built-in Intent integration (`ACTION_DIAL`) to immediately call the person who found/lost the pet.
* **Map Integration:** One-click location viewing. Uses implicit intents (`geo:0,0?q=`) to open the city directly in Google Maps.
* **Image Handling:** Select images from the gallery, compress them, and store/retrieve them via Base64 encoding in Firebase.

## 🛠️ Tech Stack

* **Language:** Java
* **Platform:** Android SDK
* **Database:** Firebase Realtime Database
* **UI Components:** RecyclerView, ViewPager2, CardView

## 👨‍💻 Team & Roles

This project was built collaboratively by two developers:

* **Bálint:** * Backend data modeling (`AnimalItem`)
  * Data retrieval, decoding Base64 images, and real-time listing (`AllPetsActivity`, `AllPetsAdapter`)
  * Smart location filtering logic (`SearchActivity`)
  * Google Maps intent integration (`CityAdapter`)
* **Marci:** * Main UI and navigation (`MainActivity`)
  * ViewPager2 implementation for the home screen slider (`AnimalSliderAdapter`)
  * Form creation, input validation, and Firebase data writing (`FormActivity`)
  * Image compression and Base64 encoding logic.

## 📱 How to Run

1. Clone this repository to your local machine.
2. Open the project in **Android Studio**.
3. Let the Gradle sync finish completely.
4. Run the app on an emulator (API 34+ recommended) or a physical Android device.
