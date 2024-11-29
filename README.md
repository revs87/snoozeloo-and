[![Static Badge](https://img.shields.io/badge/release%20-%20v1.0%20-%20%231082C3)](https://github.com/revs87/snoozeloo-and/releases/tag/v1.0)

![Static Badge](https://img.shields.io/badge/License%20-%20Apache%202.0%20-%20%231082C3)

![image](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)

# Snoozeloo - A modern and simple Alarm application.

## 📜 Summary

This is a modern Alarm clock Android Native application called ⏰ Snoozeloo.
The development of this app was motivated and was part of
the [Mobile Dev Campus Monthly Challenge](https://pl-coding.com/campus/) by Philipp Lackner for
November of 2024.

## 📌 Features

- Creation and editing multiple alarm entries.
- Killing the app does not stop alarm triggering.
- Rebooting the Android device does not stop alarm triggering.
- Dark mode supported.

### 🚀 Technical Highlights

- Android Native
- 100% Kotlin
- 100% Jetpack Compose
- Kotlin Coroutines and Flows
- Push Notifications
- Broadcast Receivers
- Boot Receivers
- Dynamic Permission requests for POST_NOTIFICATIONS and USE_FULL_SCREEN_INTENT
- Unit tests applied to TimeHelper

### 🏛️ Architecture & Design Patterns

- Single module architecture
- Single Activity architecture
- MVVM presentation pattern with UI States when editing an Alarm
- MVI unidirectional data flow from Actions to State
- Sealed interfaces for Actions and Destinations files
- Dependency Injection applied with Koin
- Dependency Inversion Principle (DIP) from data to presentation layers
- Single Source of Truth in a Room database
- Reactive Navigation (Flow listening)

### 📲 UI

- Dark mode supported
- Jetpack-Compose No-type Args navigation
- Jetpack-Compose Deep Linking navigation
- Bespoke TimeCard (copied from Material3 TimePicker's source)
- Delete Alarm entry with a side swipe from RTL

### 🗄️ Local Persistence

- SavedStateHandle persistency for the Alarm editing
- Room DB (on KSP)

## 🎨 Design

### 📸 Screenshots

| Splash                                                                                      | Alarm List                                                                                 |
|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| ![1_intro](https://github.com/user-attachments/assets/9ce9bffa-cf92-4ece-99e7-384f4a116707) | ![2_list](https://github.com/user-attachments/assets/65859df0-f78d-46f6-b57f-03a15903851f) |

| Existing Alarm Editing                                                                     | New Alarm Rditing                                                                         |
|--------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| ![3_edit](https://github.com/user-attachments/assets/939b189e-ee8d-4bc2-8cab-78b23fff8e9e) | ![4_new](https://github.com/user-attachments/assets/09ffcf37-f1ed-47a1-bb83-3b8781baacc3) |

| Alarm Time Setting                                                                         | Alarm Ringing                                                                                 |
|--------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| ![5_time](https://github.com/user-attachments/assets/4dcdf2f3-5dcf-4f40-a219-78d7558f4f7d) | ![6_trigger](https://github.com/user-attachments/assets/6b41b53e-4935-429e-b162-a6bf60a681a2) |

### 🖧 Project structure

This project is structured in a single module divided by the following 5 packages:

common/

data/

di/

domain/

ui/

## 🕹️ Usage

### ☑ Project Requirements

- Java 17+
- The latest Android Studio Hedgehog or above (for easy install use JetBrains Toolbox)

### ⚙️ Configuration

Run ./gradlew build

## 🧾 License

Check LICENSE.md from the project.

## 🤝 Contributing

Check CONTRIBUTING.md from the project.
