# RentalWheels - Android Car Rental App

![Project Status](https://img.shields.io/badge/status-active%20development-green)
![Kotlin](https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

## 🚗 Project Overview

RentalWheels is a modern Android car rental application built specifically for the Kenyan market. The app provides a seamless experience for users to browse, book, and manage car rentals with real-time data from Firebase Firestore. 

**🔐 Authentication Required**: This app requires user authentication to access all features and data.

## 🌟 Key Features

### 🔐 Authentication & Security
- Firebase Authentication with secure user management
- User profile management and preferences
- Personalized booking history and analytics

### 🚗 Car Management
- Real-time car availability from Firestore
- Detailed car listings with comprehensive information
- Recommended cars based on user preferences
- Advanced filtering and search capabilities
- High-quality car images and galleries

### 📅 Booking System
- Seamless booking process with real-time updates
- Multiple booking statuses (Pending, Confirmed, Active, Completed, Cancelled)
- Cart functionality for multiple car bookings
- Quick booking for immediate rentals
- Booking modification and cancellation
- Booking analytics and insights


### 📊 User Experience
- Modern Material Design 3 UI
- Intuitive navigation with bottom navigation
- Pull-to-refresh functionality
- Loading states and error handling
- Offline support for cached data

## 🛠 Technologies Used

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Android Jetpack** - Comprehensive suite of libraries
- **Material Design 3** - Latest Material Design components

### Architecture & Patterns
- **MVVM Architecture** - Model-View-ViewModel pattern
- **Hilt Dependency Injection** - Compile-time dependency injection
- **Repository Pattern** - Data layer abstraction
- **StateFlow & Flow** - Reactive programming with coroutines

### Backend & Data
- **Firebase Firestore** - Real-time NoSQL database
- **Firebase Authentication** - Secure user authentication
- **Firebase Storage** - Car image storage and management

### Development Tools
- **Coroutines** - Asynchronous programming
- **Timber** - Enhanced logging
- **Coil** - Image loading and caching
- **Navigation Compose** - Type-safe navigation

## 🚀 Getting Started

### Prerequisites

- **Android Studio** - Hedgehog | 2023.1.1 or later
- **JDK 17** or later
- **Android SDK** with minimum API level 24 (Android 7.0)
- **Firebase Project** - Set up with Firestore and Authentication

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/muchaisam/RentalWheels.git
   cd RentalWheels
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository

3. **Firebase Setup:**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication (Email/Password provider)
   - Create a Firestore database
   - Download the `google-services.json` file
   - Place it in the `app/` directory

4. **Build and Run:**
   ```bash
   ./gradlew assembleDebug
   ```
   Or use Android Studio's Run button

### 📱 APK Installation

**APK and screenshots will be available soon for easy installation and preview.**

## 📱 App Architecture

### Screen Structure
- **🏠 HomeScreen** - Dashboard with featured cars and categories
- **🔍 BrowseScreen** - Complete car catalog with filtering and search
- **📅 BookingsScreen** - User bookings management and recommended cars
- **🚗 DetailedCarScreen** - Comprehensive car details and booking
- **⚙️ SettingsScreen** - User preferences and app configuration
- **📊 AnalyticsScreen** - Booking insights and user statistics

### Key Components
- **🛒 Cart Management** - Multi-car booking with quantity control
- **💳 Booking System** - Complete rental lifecycle management
- **🔄 Real-time Sync** - Live data updates from Firestore
- **📱 Responsive UI** - Optimized for various screen sizes

### Data Flow
```
User Authentication → Firestore Data → Repository Layer → ViewModel → Compose UI
```

### ViewModels
- `BookingsViewModel` - Manages booking operations and cart
- `CarViewModel` - Handles car data and home screen state
- `BrowseViewModel` - Controls browsing and filtering
- `CarDetailsViewModel` - Manages individual car details
- `AnalyticsViewModel` - Processes booking analytics


## 📸 Screenshots

**Screenshots and APK download will be available soon!**

<table>
  <tr>
    <td><img src="https://github.com/muchaisam/Rentalwheels/blob/main/screenshots/Home.png" alt="Home Screen" width="300"/></td>
    <td><img src="https://github.com/muchaisam/Rentalwheels/blob/main/screenshots/CarDetailScreen.png" alt="Car Detail Screen" width="300"/></td>
  </tr>
  <tr>
    <td align="center"><strong>Home Screen</strong></td>
    <td align="center"><strong>Car Details</strong></td>
  </tr>
</table>

*More screenshots coming soon showcasing the complete user experience.*

## 🧪 Testing

The project includes comprehensive testing:

### Test Types
- **Unit Tests** - ViewModels and Repository logic
- **Integration Tests** - Firebase interactions and data flow
- **UI Tests** - Compose UI component testing
- **End-to-End Tests** - Complete user journey validation

### Running Tests
```bash
# Run all unit tests
./gradlew test

# Run instrumentation tests
./gradlew connectedAndroidTest

# Run specific test suites
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

### Test Coverage
- Repository layer testing with mocked Firebase
- ViewModel state management testing
- Compose UI interaction testing
- Currency formatting and calculation testing

## 💡 Key Implementation Highlights

### 🔄 Real-time Data Synchronization
- All car data synced from Firestore in real-time
- Booking status updates reflect immediately
- No mock data - completely authentication-driven


### 🛒 Advanced Cart System
- Multi-car booking capability
- Quantity management per car
- Persistent cart state across app sessions

### 📊 Analytics & Insights
- Comprehensive booking analytics
- User preference tracking
- Performance metrics and insights


## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### Development Guidelines
1. Follow Kotlin coding conventions
2. Write comprehensive tests for new features
3. Update documentation for significant changes
4. Ensure Firebase security rules are maintained

## 🚀 Future Enhancements

- **🗺️ Google Maps Integration** - Location-based car discovery
- **💬 In-app Messaging** - Communication between renters and owners
- **⭐ Review System** - Car and user rating system
- **� Push Notifications** - Booking updates and reminders
- **🌐 Multi-language Support** - Swahili and English
- **📱 Offline Mode** - Enhanced offline functionality

## �📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## 📞 Contact

**Muchai Sam** - [@smuchai10](https://twitter.com/smuchai10)

**Project Link:** [https://github.com/muchaisam/RentalWheels](https://github.com/muchaisam/RentalWheels)

---

**Built with ❤️ for the Kenyan car rental market**