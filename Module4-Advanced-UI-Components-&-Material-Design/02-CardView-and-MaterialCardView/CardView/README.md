# CardView and MaterialCardView Android Demo

This project demonstrates the usage of CardView and MaterialCardView in Android development with comprehensive examples and interactive features.

## Features

### 1. Basic CardView
- Simple elevated card with rounded corners
- Demonstrates basic CardView implementation
- Click to see interaction feedback

### 2. MaterialCardView with Stroke
- Enhanced MaterialCardView with purple stroke
- Contains a button for interaction demonstration
- Shows advanced MaterialCardView features

### 3. Checkable MaterialCardView
- Demonstrates checkable state functionality
- Tap to select/deselect the card
- Shows check icon when selected

### 4. Profile Card (Hands-on Lab)
- Complete profile card implementation
- Profile picture with circular background
- Name, designation, and action button
- Demonstrates real-world card usage

### 5. Interactive Color Toggle Card
- Demonstrates dynamic card styling
- Tap to toggle between blue and purple backgrounds
- Shows programmatic card manipulation

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/cardview/
│   │   └── MainActivity.kt          # Main activity with all interactions
│   ├── res/
│   │   ├── drawable/
│   │   │   ├── circle_background.xml    # Circular background for profile
│   │   │   └── instruction_background.xml # Background for instructions
│   │   ├── layout/
│   │   │   └── activity_main.xml        # Main layout with all card examples
│   │   └── values/
│   │       └── colors.xml               # Color resources
│   └── AndroidManifest.xml
└── build.gradle.kts                    # Dependencies configuration
```

## Setup Instructions

1. **Clone or download the project**
2. **Open in Android Studio**
3. **Sync Gradle files** (if prompted)
4. **Run the app** on an emulator or device

## Dependencies

The project uses the following key dependencies:

```gradle
implementation "com.google.android.material:material:1.12.0"
implementation "androidx.cardview:cardview:1.0.0"
```

## Learning Objectives

After completing this demo, you should understand:

- ✅ How to implement basic CardView
- ✅ How to use MaterialCardView with advanced features
- ✅ How to make cards interactive with click listeners
- ✅ How to implement checkable card states
- ✅ How to create profile cards for real-world applications
- ✅ How to dynamically change card properties
- ✅ Best practices for card design and interaction

## Interactive Features

### Click Interactions
- **Basic CardView**: Shows toast message on click
- **MaterialCardView**: Demonstrates card-level interaction
- **Card Button**: Shows button-specific interaction
- **Profile Card**: Simulates profile navigation
- **Profile Button**: Shows button action within card
- **Interactive Card**: Toggles background color

### Checkable State
- **Checkable Card**: Tap to select/deselect
- Visual feedback with check icon
- State management demonstration

## Code Examples

### Basic CardView Implementation
```xml
<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="12dp"
    app:cardElevation="8dp">
    <!-- Card content -->
</androidx.cardview.widget.CardView>
```

### MaterialCardView with Stroke
```xml
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="16dp"
    app:strokeColor="@color/purple_200"
    app:strokeWidth="2dp">
    <!-- Card content -->
</com.google.android.material.card.MaterialCardView>
```

### Kotlin Click Handler
```kotlin
cardView.setOnClickListener {
    Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show()
}
```

### Checkable Card Setup
```kotlin
materialCardView.isCheckable = true
materialCardView.setOnCheckedChangeListener { card, isChecked ->
    // Handle checked state
}
```

## Best Practices Demonstrated

1. **Consistent Spacing**: All cards use consistent margins and padding
2. **Material Design**: Follows Material Design elevation and corner radius guidelines
3. **Accessibility**: Proper content descriptions and touch targets
4. **Performance**: Efficient view initialization and event handling
5. **User Feedback**: Toast messages and visual feedback for all interactions

## Exercises to Try

### Easy Level
1. Change the corner radius of the basic CardView to 20dp
2. Modify the elevation of the MaterialCardView to 12dp
3. Add a different stroke color to the profile card

### Intermediate Level
1. Add a new card with a different background color
2. Implement a card that changes elevation on click
3. Create a card with multiple buttons

### Advanced Level
1. Implement a RecyclerView with multiple cards
2. Add card animations using ObjectAnimator
3. Create a card with dynamic content loading

## Troubleshooting

### Common Issues

1. **Build Errors**: Ensure all dependencies are synced
2. **Layout Issues**: Check that all drawable resources exist
3. **Runtime Errors**: Verify that all view IDs match between layout and Kotlin code

### Performance Tips

1. Use `cardUseCompatPadding="false"` when possible
2. Avoid deep nested hierarchies in card layouts
3. Use appropriate elevation values (2dp-8dp for most cases)

## Additional Resources

- [Material Design Cards](https://material.io/components/cards)
- [Android CardView Documentation](https://developer.android.com/reference/androidx/cardview/widget/CardView)
- [MaterialCardView Documentation](https://material.io/components/cards/android)

## Contributing

Feel free to enhance this demo by:
- Adding more card examples
- Implementing additional interactions
- Improving the UI design
- Adding animations

---

**Happy Learning! 🚀**
