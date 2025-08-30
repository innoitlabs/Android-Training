# Setup Guide - Material Design Theming Project

## Quick Start

This guide will help you set up and run the Material Design theming demo project.

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK API 24+ (Android 7.0+)
- Kotlin 1.9+
- Git (optional)

## Project Structure

```
MaterialDesign/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/materialdesign/
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── themes.xml
│   │   │   │   └── styles.xml
│   │   │   └── values-night/
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

## Installation Steps

### 1. Clone or Download the Project

If using Git:
```bash
git clone <repository-url>
cd MaterialDesign
```

Or download and extract the project files.

### 2. Open in Android Studio

1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the `MaterialDesign` folder and select it
4. Wait for the project to sync and index

### 3. Verify Dependencies

The project should automatically sync and download dependencies. Verify in `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.google.android.material:material:1.12.0")
    // ... other dependencies
}
```

### 4. Build the Project

1. Click `Build` → `Make Project` (or press Ctrl+F9 / Cmd+F9)
2. Wait for the build to complete
3. Check the Build tab for any errors

### 5. Run the App

1. Connect an Android device or start an emulator
2. Click the green "Run" button (or press Shift+F10 / Ctrl+R)
3. Select your target device
4. Wait for the app to install and launch

## Expected Output

When you run the app, you should see:

1. **Header Section**: Title and subtitle with Material Design typography
2. **Profile Card**: User information with styled text and buttons
3. **Settings Card**: Theme toggle button
4. **Info Card**: About section with description

## Testing Features

### Theme Toggle
- Tap the "Toggle Dark/Light Theme" button
- The app should switch between light and dark themes
- All colors and backgrounds should update appropriately

### Button Interactions
- **Primary Button**: Shows "Changes Saved!" when tapped
- **Outlined Button**: Shows "Editing..." when tapped
- Both buttons return to their original text after a delay

### Dark Mode Testing
1. Enable dark mode in your device settings
2. Restart the app
3. Verify that the app automatically adapts to dark theme
4. Test the theme toggle button

## Troubleshooting

### Build Errors

**Error**: "Cannot resolve symbol 'MaterialButton'"
- **Solution**: Ensure Material Components dependency is included
- Check that `implementation("com.google.android.material:material:1.12.0")` is in build.gradle.kts

**Error**: "Theme.Material3.DayNight.NoActionBar not found"
- **Solution**: Update Material Components to latest version
- Ensure you're using Material3 theme names

### Runtime Errors

**App crashes on startup**
- Check that all layout IDs match between XML and Kotlin code
- Verify that all referenced styles exist in styles.xml

**Theme not applying correctly**
- Ensure the theme is properly set in AndroidManifest.xml
- Check that colors.xml contains all referenced colors

### Performance Issues

**Slow build times**
- Clean and rebuild the project: `Build` → `Clean Project` → `Rebuild Project`
- Invalidate caches: `File` → `Invalidate Caches and Restart`

## Customization

### Changing Colors

1. Edit `app/src/main/res/values/colors.xml`
2. Update color values to match your brand
3. Rebuild and test the app

### Adding New Styles

1. Edit `app/src/main/res/values/styles.xml`
2. Add new style definitions
3. Apply styles in your layouts using `style="@style/YourStyle"`

### Modifying Layout

1. Edit `app/src/main/res/layout/activity_main.xml`
2. Add new Material Design components
3. Apply existing or new styles

## Next Steps

After successfully running the demo:

1. **Explore the Code**: Review the implementation in MainActivity.kt
2. **Modify Styles**: Try changing colors and styles
3. **Add Components**: Experiment with other Material Design components
4. **Study Documentation**: Read the main README.md for detailed explanations

## Support

If you encounter issues:

1. Check the troubleshooting section above
2. Verify your Android Studio and SDK versions
3. Ensure all dependencies are properly synced
4. Try cleaning and rebuilding the project

## Version Information

- **Android Studio**: 2023.1.1+ (Hedgehog)
- **Android SDK**: API 24+ (Android 7.0+)
- **Kotlin**: 1.9+
- **Material Components**: 1.12.0
- **Target SDK**: 36 (Android 14)

---

**Happy Coding! 🚀**
