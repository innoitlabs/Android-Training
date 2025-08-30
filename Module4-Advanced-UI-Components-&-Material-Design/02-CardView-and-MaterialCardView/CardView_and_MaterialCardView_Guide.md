# CardView and MaterialCardView in Android - Complete Guide

## Table of Contents
1. [Introduction](#introduction)
2. [When to Use Cards](#when-to-use-cards)
3. [Dependencies](#dependencies)
4. [CardView Basics](#cardview-basics)
5. [MaterialCardView Advanced Features](#materialcardview-advanced-features)
6. [Kotlin Implementation](#kotlin-implementation)
7. [Best Practices](#best-practices)
8. [Hands-on Lab: Profile Card](#hands-on-lab-profile-card)
9. [Exercises](#exercises)
10. [Summary](#summary)

---

## Introduction

### What are CardView and MaterialCardView?

**CardView** is a UI component that provides:
- Elevation and shadows
- Rounded corners
- Consistent styling across different Android versions
- Material Design compliance

**MaterialCardView** is an enhanced version of CardView that offers:
- All CardView features plus:
- Stroke color and width customization
- Ripple effects on touch
- Checkable states
- Material theming support
- Better integration with Material Design components

### Key Differences

| Feature | CardView | MaterialCardView |
|---------|----------|------------------|
| Basic elevation | ✅ | ✅ |
| Rounded corners | ✅ | ✅ |
| Stroke customization | ❌ | ✅ |
| Ripple effects | ❌ | ✅ |
| Checkable state | ❌ | ✅ |
| Material theming | Limited | ✅ |

---

## When to Use Cards

Cards are perfect for:

1. **Grouping Related Information**
   - User profile cards
   - Product listings
   - Article snippets
   - Contact information

2. **RecyclerView Items**
   - Consistent styling across list items
   - Touch feedback
   - Visual separation between items

3. **Material Design Compliance**
   - Elevation and shadows
   - Consistent spacing
   - Modern UI appearance

4. **Interactive Elements**
   - Clickable content areas
   - Selection states
   - Visual feedback

---

## Dependencies

The Material Components library is already included in your project. If you need to add it manually:

```gradle
implementation "com.google.android.material:material:1.12.0"
```

---

## CardView Basics

### Basic CardView Example

```xml
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:card_view="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    card_view:cardCornerRadius="12dp"
    card_view:cardElevation="8dp"
    card_view:cardBackgroundColor="@color/white">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="16dp"
        android:text="This is a CardView"
        android:textSize="18sp"/>
</androidx.cardview.widget.CardView>
```

### CardView Attributes

| Attribute | Description | Example |
|-----------|-------------|---------|
| `cardCornerRadius` | Corner radius in dp | `12dp` |
| `cardElevation` | Shadow depth in dp | `8dp` |
| `cardBackgroundColor` | Background color | `@color/white` |
| `cardMaxElevation` | Maximum elevation | `12dp` |
| `cardPreventCornerOverlap` | Prevent content overlap | `true/false` |
| `cardUseCompatPadding` | Use compatibility padding | `true/false` |

---

## MaterialCardView Advanced Features

### Basic MaterialCardView

```xml
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    app:cardCornerRadius="16dp"
    app:cardElevation="6dp"
    app:strokeColor="@color/purple_200"
    app:strokeWidth="2dp"
    app:checkedIcon="@drawable/ic_check">

    <LinearLayout
        android:orientation="vertical"
        android:padding="16dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:id="@+id/cardTitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="MaterialCardView Example"
            android:textSize="20sp"
            android:textStyle="bold"/>

        <Button
            android:id="@+id/cardButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Click Me"/>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

### MaterialCardView Additional Attributes

| Attribute | Description | Example |
|-----------|-------------|---------|
| `strokeColor` | Border color | `@color/purple_200` |
| `strokeWidth` | Border width | `2dp` |
| `checkedIcon` | Icon for checked state | `@drawable/ic_check` |
| `checkedIconTint` | Icon tint color | `@color/primary` |
| `rippleColor` | Ripple effect color | `@color/ripple_color` |
| `cardBackgroundColor` | Background color | `@color/white` |

---

## Kotlin Implementation

### Basic Click Listener

```kotlin
package com.example.cardview

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button inside the card
        val button = findViewById<Button>(R.id.cardButton)
        button.setOnClickListener {
            Toast.makeText(this, "Card button clicked!", Toast.LENGTH_SHORT).show()
        }

        // Make the entire card clickable
        val materialCard = findViewById<MaterialCardView>(R.id.materialCard)
        materialCard.setOnClickListener {
            Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show()
        }
    }
}
```

### Checkable MaterialCardView

```kotlin
// Make card checkable
materialCard.isCheckable = true
materialCard.isChecked = false

materialCard.setOnCheckedChangeListener { card, isChecked ->
    if (isChecked) {
        Toast.makeText(this, "Card selected!", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(this, "Card deselected!", Toast.LENGTH_SHORT).show()
    }
}
```

---

## Best Practices

### 1. Layout Guidelines
- Keep card layouts lightweight
- Avoid deep nested hierarchies
- Use appropriate padding and margins
- Maintain consistent spacing

### 2. Material Design Principles
- Use elevation to show hierarchy
- Apply consistent corner radius
- Follow Material Design spacing guidelines
- Use appropriate colors and typography

### 3. Performance Considerations
- Use `cardUseCompatPadding="false"` when possible
- Avoid unnecessary background drawables
- Use `cardPreventCornerOverlap="false"` for better performance

### 4. Accessibility
- Provide meaningful content descriptions
- Ensure sufficient touch targets
- Use appropriate contrast ratios
- Support screen readers

---

## Hands-on Lab: Profile Card

### Objective
Create a profile card with:
- Profile picture (ImageView)
- Name and designation (TextView)
- Interactive button
- MaterialCardView styling

### Implementation Steps

1. **Layout Design**
   - Use MaterialCardView as container
   - Add ImageView for profile picture
   - Include TextViews for name and designation
   - Add a button for interaction

2. **Styling**
   - Apply rounded corners
   - Add elevation for depth
   - Use consistent padding
   - Implement proper spacing

3. **Interaction**
   - Handle button clicks
   - Add ripple effects
   - Provide user feedback

### Expected Result
A visually appealing profile card that demonstrates MaterialCardView capabilities with proper interaction handling.

---

## Exercises

### Easy Level
1. **Customize Card Appearance**
   - Change corner radius to 20dp
   - Modify elevation to 12dp
   - Add a custom background color

2. **Add Stroke**
   - Add a 3dp purple stroke to MaterialCardView
   - Experiment with different stroke colors

### Intermediate Level
1. **Interactive Card**
   - Create a clickable card that toggles background color
   - Add animation for color transition
   - Implement state management

2. **Dynamic Content**
   - Load profile data from a data class
   - Update card content programmatically
   - Handle different data states

### Advanced Level
1. **RecyclerView Integration**
   - Create a list of profile cards
   - Implement RecyclerView with CardView items
   - Add click handling for list items

2. **Custom Card States**
   - Implement checkable cards
   - Add selection indicators
   - Handle multiple card selection

---

## Summary

### Key Takeaways

1. **CardView vs MaterialCardView**
   - CardView: Basic elevated cards
   - MaterialCardView: Advanced features with Material Design

2. **When to Use Each**
   - CardView: Simple elevation needs
   - MaterialCardView: Modern apps with advanced features

3. **Best Practices**
   - Follow Material Design guidelines
   - Keep layouts lightweight
   - Ensure accessibility
   - Use appropriate elevation and spacing

4. **Common Use Cases**
   - Profile cards
   - Product listings
   - RecyclerView items
   - Interactive content areas

### Next Steps

1. Practice with different card layouts
2. Experiment with MaterialCardView features
3. Integrate cards into RecyclerView
4. Explore Material Design components
5. Build real-world applications using cards

---

## Additional Resources

- [Material Design Cards](https://material.io/components/cards)
- [Android CardView Documentation](https://developer.android.com/reference/androidx/cardview/widget/CardView)
- [MaterialCardView Documentation](https://material.io/components/cards/android)
- [Material Design Guidelines](https://material.io/design)

---

*This guide provides a comprehensive introduction to CardView and MaterialCardView in Android development. Practice the examples and exercises to master these essential UI components.*
