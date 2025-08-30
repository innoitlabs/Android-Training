# Units of Measurement in Android

## Understanding Android Units

Android provides several units of measurement to handle different screen densities and user preferences. Choosing the right unit is crucial for responsive design.

## Available Units

### 1. Pixels (px)
**What it is**: Physical pixels on the screen
**When to use**: **NEVER** for UI dimensions
**Why avoid**: Different devices have different pixel densities

```xml
<!-- ❌ DON'T DO THIS -->
<TextView
    android:layout_width="100px"
    android:layout_height="50px"
    android:textSize="16px"/>
```

### 2. Density-Independent Pixels (dp)
**What it is**: Virtual pixels that scale with screen density
**When to use**: For all UI dimensions (width, height, margins, padding)
**Formula**: 1dp = 1px on 160dpi screen

```xml
<!-- ✅ DO THIS -->
<Button
    android:layout_width="100dp"
    android:layout_height="50dp"
    android:layout_margin="16dp"
    android:padding="8dp"/>
```

### 3. Scale-Independent Pixels (sp)
**What it is**: Similar to dp but respects user's font size preferences
**When to use**: For text sizes only
**Formula**: 1sp = 1dp × user's font scale

```xml
<!-- ✅ DO THIS -->
<TextView
    android:textSize="16sp"
    android:text="Hello World"/>
```

### 4. Match Parent / Wrap Content
**What it is**: Relative sizing based on parent or content
**When to use**: For flexible layouts

```xml
<!-- Takes full width of parent -->
<View
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## Density Categories

| Density | DPI Range | Scale Factor | Example Device |
|---------|-----------|--------------|----------------|
| ldpi    | 120-160   | 0.75x        | Old devices |
| mdpi    | 160-240   | 1.0x         | Baseline |
| hdpi    | 240-320   | 1.5x         | Common phones |
| xhdpi   | 320-480   | 2.0x         | High-end phones |
| xxhdpi  | 480-640   | 3.0x         | Flagship phones |
| xxxhdpi | 640+      | 4.0x         | Ultra-high density |

## Practical Examples

### Button Sizing
```xml
<!-- Good: Uses dp for consistent sizing -->
<Button
    android:layout_width="120dp"
    android:layout_height="48dp"
    android:text="Submit"
    android:textSize="16sp"/>
```

### Text Styling
```xml
<!-- Good: Uses sp for text that respects user preferences -->
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Welcome to our app!"
    android:textSize="18sp"
    android:lineSpacingExtra="4dp"/>
```

### Margins and Padding
```xml
<!-- Good: Uses dp for spacing -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp"
    android:layout_margin="8dp">
    
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Content"
        android:layout_marginBottom="12dp"/>
</LinearLayout>
```

## Common Mistakes to Avoid

### 1. Using px for UI Elements
```xml
<!-- ❌ Wrong -->
<ImageView
    android:layout_width="200px"
    android:layout_height="200px"/>

<!-- ✅ Correct -->
<ImageView
    android:layout_width="200dp"
    android:layout_height="200dp"/>
```

### 2. Using dp for Text Sizes
```xml
<!-- ❌ Wrong -->
<TextView
    android:textSize="16dp"/>

<!-- ✅ Correct -->
<TextView
    android:textSize="16sp"/>
```

### 3. Hardcoding Dimensions
```xml
<!-- ❌ Wrong: Fixed size that doesn't adapt -->
<LinearLayout
    android:layout_width="300dp"
    android:layout_height="400dp"/>

<!-- ✅ Correct: Flexible sizing -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## Best Practices

1. **Always use dp for UI dimensions** (width, height, margins, padding)
2. **Always use sp for text sizes** to respect accessibility settings
3. **Use match_parent and wrap_content** for flexible layouts
4. **Avoid px** for UI elements
5. **Test on different densities** to ensure consistency

## Quick Reference

| Use Case | Recommended Unit | Example |
|----------|------------------|---------|
| View dimensions | dp | `android:layout_width="100dp"` |
| Text size | sp | `android:textSize="16sp"` |
| Margins/Padding | dp | `android:layout_margin="16dp"` |
| Flexible width | match_parent | `android:layout_width="match_parent"` |
| Content-based height | wrap_content | `android:layout_height="wrap_content"` |

## Testing Your Units

To verify your unit choices work correctly:

1. **Test on different emulators** with various densities
2. **Use the Layout Inspector** in Android Studio
3. **Check accessibility settings** (font size changes)
4. **Preview in different orientations**

Remember: The goal is to make your app look consistent and professional across all Android devices!
