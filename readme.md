# RentalWheels - Android Car Rental App

![Project Status](https://img.shields.io/badge/status-in%20progress-yellow)
![Kotlin](https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase)

## 🚗 Project Overview

RentalWheels is an Android application that revolutionizes the car rental experience. Built with Kotlin and leveraging Firebase, this app allows users to easily browse, book, and manage car rentals from their mobile devices.

**⚠️ Note: This project is currently in active development. Features and implementations are subject to change.**

## 🌟 Key Features

- User authentication and profile management
- Interactive map view of available cars
- Detailed car listings with high-quality images
- Seamless booking process
- In-app messaging between renters and car owners
- Review and rating system
- Push notifications for booking updates

## 🛠 Technologies Used

- Kotlin
- Android Jetpack components
- Google Maps API
- Firebase (Authentication, Realtime Database, Cloud Messaging)
- MVVM Architecture
- Coroutines for asynchronous operations
- Material Design 3 components

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK with minimum API level 21

### Installation

1. Clone the repository:

git clone https://github.com/muchaisam/RentalWheels.git


2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Set up a Firebase project and add the `google-services.json` file to the app directory.
5. Build and run the app on an emulator or physical device.

## 📱 App Structure

- `HomeFragment`: Displays the map view and available cars
- `BrowseFragment`: Lists all available cars for rental
- `BookingsFragment`: Shows user's current and past bookings
- `ProfileFragment`: User profile management and settings

## 🧪 Testing

The project includes both unit tests and instrumentation tests:

- Unit tests for ViewModels and Repositories
- UI tests using Espresso
- Integration tests for Firebase interactions

Run tests using Android Studio or via Gradle:

./gradlew test
./gradlew connectedAndroidTest


## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## 📞 Contact

Your Name - [@smuchai10](https://twitter.com/smuchai10) 

Project Link: [https://github.com/muchaisam/RentalWheels](https://github.com/muchaisam/RentalWheels)