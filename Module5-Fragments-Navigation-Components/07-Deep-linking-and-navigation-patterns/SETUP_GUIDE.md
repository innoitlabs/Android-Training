# Setup Guide - Android Deep Linking Project

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9+ 
- Android SDK API 24+ (Android 7.0)
- Android Emulator or physical device

## Project Setup

### 1. Clone/Download the Project

```bash
git clone <repository-url>
cd 07-Deep-linking-and-navigation-patterns
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the `DeepLinking` folder
4. Click "OK"

### 3. Sync Project

1. Wait for Gradle sync to complete
2. If prompted, update Gradle version
3. Ensure all dependencies are downloaded

### 4. Build Configuration

The project uses the following configuration:

```kotlin
// app/build.gradle.kts
android {
    namespace = "com.example.deeplinking"
    compileSdk = 36
    minSdk = 24
    targetSdk = 36
}
```

## Dependencies

The project includes these key dependencies:

- **Navigation Component**: For navigation and deep linking
- **Fragment KTX**: For fragment operations
- **Safe Args**: For type-safe navigation arguments

## Running the App

### 1. Start Emulator/Device

1. Open AVD Manager in Android Studio
2. Create or start an emulator (API 24+)
3. Ensure the device is connected and recognized

### 2. Build and Run

1. Click the "Run" button (green play icon)
2. Select your target device
3. Wait for the app to install and launch

### 3. Verify Installation

- The app should launch to the Home screen
- You should see a "Go to Detail" button
- Clicking the button should navigate to the Detail screen

## Testing Deep Links

### Method 1: ADB Command

```bash
# Test deep link with item ID 42
adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking

# Test deep link with item ID 123
adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/123" com.example.deeplinking
```

### Method 2: Browser Testing

1. Open a browser on your device
2. Type: `myapp://detail/42`
3. The app should open to the Detail screen

### Method 3: Intent Testing

```kotlin
// In another app or test environment
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://detail/42"))
startActivity(intent)
```

## Troubleshooting

### Common Issues

1. **Build Errors**
   - Clean and rebuild project
   - Invalidate caches and restart
   - Check Gradle sync status

2. **Deep Links Not Working**
   - Verify manifest configuration
   - Check navigation graph setup
   - Ensure app is installed and running

3. **Navigation Issues**
   - Verify Navigation Component dependencies
   - Check fragment class names in navigation graph
   - Ensure Safe Args plugin is applied

### Debug Commands

```bash
# Check if app is installed
adb shell pm list packages | grep deeplinking

# Check app logs
adb logcat | grep deeplinking

# Force stop and restart app
adb shell am force-stop com.example.deeplinking
```

## Project Structure

```
DeepLinking/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/deeplinking/
│   │   │   ├── MainActivity.kt
│   │   │   ├── HomeFragment.kt
│   │   │   └── DetailFragment.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   └── fragment_detail.xml
│   │   │   └── navigation/
│   │   │       └── nav_graph.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

## Next Steps

After successful setup:

1. **Explore the Code**: Review the implementation in each file
2. **Modify Deep Links**: Try changing the URI patterns
3. **Add Features**: Implement the exercises from the main README
4. **Test Scenarios**: Try different deep link patterns and edge cases

## Support

If you encounter issues:

1. Check the Android Studio logs
2. Verify all prerequisites are met
3. Ensure you're using compatible versions
4. Review the troubleshooting section above

---

**Note**: This project is designed for educational purposes and demonstrates best practices for Android deep linking and navigation patterns.
