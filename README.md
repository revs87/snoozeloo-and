[![Static Badge](https://img.shields.io/badge/release%20-%20v1.0%20-%20%231082C3)](https://github.com/revs87/snoozeloo-and/releases/tag/v1.0)

![Static Badge](https://img.shields.io/badge/License%20-%20Apache%202.0%20-%20%231082C3)

![image](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)



# Snoozeloo - A modern and simple Alarm application.


## 📜 Summary

This is a modern Alarm clock Android Native application called ⏰ Snoozeloo.
The development of this app was motivated and was part of the [Mobile Dev Campus Monthly Challenge](https://pl-coding.com/campus/) by Philipp Lackner for November of 2024.

### 📌 Features

- Creation and editing multiple alarm entries.
- Killing the app does not stop alarm triggering.
- Rebooting the Android device does not stop alarm triggering.
- Dark mode supported.

### 🖧 Project structure

This project is structured in a single module divided by the following 5 packages:

common/

data/

di/

domain/

ui/

### 🚀 Technical Highlights

- Android Native
- 100% Kotlin
- 100% Jetpack Compose
- Kotlin Coroutines and Flows
- Push Notifications
- Broadcast Receivers
- Boot Receivers
- Dynamic Permission requests for POST_NOTIFICATIONS and USE_FULL_SCREEN_INTENT

#### 🏛️ Architecture & Design Patterns

- Single module architecture
- Single Activity architecture
- MVVM presentation pattern with UI States when editing an Alarm
- MVI unidirectional data flow from Actions to State
- Sealed interfaces for Actions and Destinations files
- Dependency Injection applied with Koin
- Dependency Inversion Principle (DIP) from data to presentation layers
- Single Source of Truth in a Room database
- Reactive Navigation (Flow listening)

#### 📲 UI

- Dark mode supported
- Jetpack-Compose No-type Args navigation
- Jetpack-Compose Deep Linking navigation
- Bespoke TimeCard (copied from Material3 TimePicker's source)

#### 🗄️ Local Persistence

- SavedStateHandle persistency for the Alarm editing
- Room DB (on KSP)


## 🎨 Design

### 📸 Screenshots



## 🕹️ Usage

### ☑ Project Requirements

Java 17+

The latest Android Studio Hedgehog or above (for easy install use JetBrains Toolbox)

### ⚙️ Configuration

Run ./gradlew build


## 🤝 Contributing

Check CONTRIBUTING.md from the project.
