# Alternative Resources and Resource Qualifiers

## Introduction

Android's resource system allows you to provide different versions of resources (layouts, drawables, values, etc.) for different device configurations. This is the foundation of responsive design in Android.

## Resource Qualifiers

Resource qualifiers are suffixes added to resource folder names that specify when those resources should be used.

### Basic Syntax
```
res/type-qualifier/name.xml
```

Examples:
- `res/layout/activity_main.xml` (default)
- `res/layout-land/activity_main.xml` (landscape)
- `res/layout-sw600dp/activity_main.xml` (tablet)

## Screen Size Qualifiers

### Smallest Width (sw)
Defines the smallest dimension of the screen (width or height, whichever is smaller).

| Qualifier | Description | Example Devices |
|-----------|-------------|-----------------|
| `sw320dp` | 320dp or larger | Small phones |
| `sw480dp` | 480dp or larger | Medium phones |
| `sw600dp` | 600dp or larger | Tablets |
| `sw720dp` | 720dp or larger | Large tablets |

### Available Width/Height (w/h)
Defines the available width or height of the screen.

| Qualifier | Description | Use Case |
|-----------|-------------|----------|
| `w600dp` | Width 600dp or larger | Landscape tablets |
| `h600dp` | Height 600dp or larger | Portrait tablets |

## Orientation Qualifiers

| Qualifier | Description |
|-----------|-------------|
| `port` | Portrait orientation |
| `land` | Landscape orientation |

## Density Qualifiers

| Qualifier | DPI Range | Scale Factor |
|-----------|-----------|--------------|
| `ldpi` | 120-160 | 0.75x |
| `mdpi` | 160-240 | 1.0x |
| `hdpi` | 240-320 | 1.5x |
| `xhdpi` | 320-480 | 2.0x |
| `xxhdpi` | 480-640 | 3.0x |
| `xxxhdpi` | 640+ | 4.0x |

## Combining Qualifiers

You can combine multiple qualifiers in order of specificity:

```
res/layout-sw600dp-land/activity_main.xml
```

**Qualifier Order** (most specific to least specific):
1. Screen size (sw, w, h)
2. Orientation (port, land)
3. Density (ldpi, mdpi, hdpi, etc.)
4. Night mode (night)

## Practical Examples

### 1. Layout Resources

#### Default Layout (Phone Portrait)
```xml
<!-- res/layout/activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:layout_gravity="center_horizontal"
        android:src="@drawable/profile_placeholder"/>

    <TextView
        android:id="@+id/nameText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="John Doe"
        android:textSize="24sp"
        android:gravity="center"/>

    <TextView
        android:id="@+id/emailText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="john.doe@example.com"
        android:textSize="16sp"
        android:gravity="center"/>

</LinearLayout>
```

#### Landscape Layout
```xml
<!-- res/layout-land/activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal"
    android:padding="16dp">

    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@drawable/profile_placeholder"/>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:orientation="vertical"
        android:layout_gravity="center_vertical">

        <TextView
            android:id="@+id/nameText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="John Doe"
            android:textSize="24sp"/>

        <TextView
            android:id="@+id/emailText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="john.doe@example.com"
            android:textSize="16sp"/>

    </LinearLayout>

</LinearLayout>
```

#### Tablet Layout
```xml
<!-- res/layout-sw600dp/activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="24dp">

    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:src="@drawable/profile_placeholder"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toStartOf="@id/verticalGuideline"/>

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/verticalGuideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuidePercent="0.4"/>

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintStart_toEndOf="@id/verticalGuideline"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/nameText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="John Doe"
            android:textSize="32sp"
            android:textStyle="bold"/>

        <TextView
            android:id="@+id/emailText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="john.doe@example.com"
            android:textSize="20sp"/>

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 2. Dimension Resources

#### Default Dimensions
```xml
<!-- res/values/dimens.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="text_size_small">14sp</dimen>
    <dimen name="text_size_medium">16sp</dimen>
    <dimen name="text_size_large">20sp</dimen>
    <dimen name="margin_small">8dp</dimen>
    <dimen name="margin_medium">16dp</dimen>
    <dimen name="margin_large">24dp</dimen>
    <dimen name="profile_image_size">120dp</dimen>
</resources>
```

#### Tablet Dimensions
```xml
<!-- res/values-sw600dp/dimens.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="text_size_small">16sp</dimen>
    <dimen name="text_size_medium">20sp</dimen>
    <dimen name="text_size_large">28sp</dimen>
    <dimen name="margin_small">12dp</dimen>
    <dimen name="margin_medium">24dp</dimen>
    <dimen name="margin_large">32dp</dimen>
    <dimen name="profile_image_size">200dp</dimen>
</resources>
```

### 3. String Resources

#### Default Strings
```xml
<!-- res/values/strings.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Responsive Design Demo</string>
    <string name="profile_title">Profile</string>
    <string name="edit_profile">Edit Profile</string>
    <string name="save_changes">Save Changes</string>
</resources>
```

#### Landscape Strings (if needed)
```xml
<!-- res/values-land/strings.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="profile_title">User Profile</string>
</resources>
```

## Using Resources in Code

### Accessing Resources
```kotlin
// Get dimension
val marginSize = resources.getDimensionPixelSize(R.dimen.margin_medium)

// Get string
val title = getString(R.string.profile_title)

// Apply dimensions to views
profileImage.layoutParams.width = resources.getDimensionPixelSize(R.dimen.profile_image_size)
```

### Runtime Configuration Changes
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Handle configuration changes
        handleScreenSize()
    }
    
    private fun handleScreenSize() {
        val screenWidth = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        val screenWidthDp = screenWidth / density
        
        when {
            screenWidthDp >= 600 -> {
                // Tablet layout
                setupTabletUI()
            }
            screenWidthDp >= 480 -> {
                // Large phone
                setupLargePhoneUI()
            }
            else -> {
                // Small phone
                setupSmallPhoneUI()
            }
        }
    }
}
```

## Best Practices

### 1. Start with Default Resources
Always provide default resources that work on all devices.

### 2. Use Specific Qualifiers
Be specific with your qualifiers to avoid conflicts.

### 3. Test Multiple Configurations
Test your app on various screen sizes and orientations.

### 4. Use Dimension Resources
Define dimensions in `dimens.xml` files for consistency.

### 5. Consider Content
Don't just resize elements; consider how content should be reorganized.

## Common Patterns

### Master-Detail Layout
```xml
<!-- res/layout-sw600dp/activity_main.xml -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal">

    <!-- Master pane -->
    <fragment
        android:id="@+id/master_fragment"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"/>

    <!-- Detail pane -->
    <fragment
        android:id="@+id/detail_fragment"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="2"/>

</LinearLayout>
```

### Responsive Grid
```xml
<!-- res/layout/activity_main.xml (phone) -->
<GridLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:columnCount="2">
    <!-- 2 columns for phones -->
</GridLayout>

<!-- res/layout-sw600dp/activity_main.xml (tablet) -->
<GridLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:columnCount="4">
    <!-- 4 columns for tablets -->
</GridLayout>
```

## Testing Your Resources

1. **Use Android Studio's Layout Editor** to preview different configurations
2. **Create multiple AVDs** with different screen sizes
3. **Test orientation changes** on physical devices
4. **Use the Configuration Qualifier** tool in Android Studio

Alternative resources are the backbone of responsive Android design. Master these techniques to create truly adaptive applications!
