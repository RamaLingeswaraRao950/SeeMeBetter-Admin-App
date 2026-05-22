# SeeMeBetter Admin (Android)

Project root: `SeeMeBetter/app`

## Setup
1. Create / select your Firebase project.
2. Add an Android app in Firebase Console with package name `com.seemebetter.admin`.
3. Download `google-services.json` and place it at `app/app/google-services.json`.
4. Enable Firebase Authentication (Email/Password).
5. Create an Auth user and bootstrap an admin role document:
   - `admins/{uid}` with `role: "admin"`.

## Run
- Open this folder in Android Studio.
- Sync Gradle, then Run.

## Gradle wrapper
This project pins Gradle **8.6** via `gradle/wrapper/gradle-wrapper.properties` and includes the wrapper JARs under `gradle/wrapper/`.

CLI build:
- `./gradlew :app:assembleDebug`

If Android Studio prompts to upgrade Gradle, keep it on 8.6 for compatibility with the Android Gradle Plugin used here.

## Code structure
- `app/app/src/main/java/com/seemebetter/admin/repository/` — Firebase repositories (Auth/Questions/Responses/Settings)
- `app/app/src/main/java/com/seemebetter/admin/ui/` — Compose screens + ViewModels (MVVM + StateFlow)
- `app/app/src/main/java/com/seemebetter/admin/navigation/` — Navigation Compose graph
- `app/app/src/main/java/com/seemebetter/admin/di/` — Hilt modules
