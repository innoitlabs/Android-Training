# 8. Resource Management and Organization

## 📁 What are Android Resources?

Android resources are external files and values that are used by your application, such as images, strings, layouts, and colors. They are stored in the `res/` directory and provide a way to organize and manage your app's assets.

### Benefits of Resource Management
- **Localization**: Support multiple languages
- **Device Adaptation**: Different screen sizes and densities
- **Maintainability**: Centralized resource management
- **Performance**: Optimized resource loading
- **Consistency**: Standardized app appearance

## 🗂️ Resource Directory Structure

### Standard Resource Folders
```
res/
├── drawable/           # Images and drawable resources
├── layout/            # UI layout files
├── values/            # Strings, colors, dimensions, styles
├── menu/              # Menu definitions
├── mipmap/            # App icons
├── navigation/        # Navigation graphs
├── anim/              # Animation files
├── raw/               # Raw files (audio, video, etc.)
└── xml/               # XML configuration files
```

### Resource Qualifiers
```
res/
├── drawable-mdpi/     # Medium density screens
├── drawable-hdpi/     # High density screens
├── drawable-xhdpi/    # Extra high density screens
├── layout-land/       # Landscape orientation
├── values-es/         # Spanish language
├── values-fr/         # French language
└── layout-large/      # Large screens
```

## 🎨 Drawable Resources

### Image Formats
```yaml
# Supported Formats:
PNG: Best for images with transparency
JPEG: Best for photographs
WebP: Modern format, smaller file sizes
SVG: Vector graphics (converted to drawable)
```

### Drawable Types
```xml
<!-- Vector Drawable -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FF000000"
        android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2z"/>
</vector>

<!-- Shape Drawable -->
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FF6200EE"/>
    <corners android:radius="8dp"/>
    <stroke
        android:width="2dp"
        android:color="#FF000000"/>
</shape>

<!-- Selector Drawable -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true"
        android:drawable="@drawable/button_pressed"/>
    <item android:state_focused="true"
        android:drawable="@drawable/button_focused"/>
    <item android:drawable="@drawable/button_normal"/>
</selector>
```

### Drawable Organization
```
drawable/
├── icons/             # App icons
│   ├── ic_home.xml
│   ├── ic_settings.xml
│   └── ic_profile.xml
├── backgrounds/       # Background drawables
│   ├── bg_button.xml
│   ├── bg_card.xml
│   └── bg_gradient.xml
├── shapes/            # Shape drawables
│   ├── shape_rounded.xml
│   ├── shape_circle.xml
│   └── shape_oval.xml
└── selectors/         # State-based drawables
    ├── selector_button.xml
    ├── selector_checkbox.xml
    └── selector_radio.xml
```

## 📝 String Resources

### Basic String Resources
```xml
<!-- res/values/strings.xml -->
<resources>
    <string name="app_name">My Android App</string>
    <string name="welcome_message">Welcome to my app!</string>
    <string name="button_submit">Submit</string>
    <string name="button_cancel">Cancel</string>
    <string name="error_network">Network connection failed</string>
    <string name="success_saved">Data saved successfully</string>
</resources>
```

### String Formatting
```xml
<!-- String with placeholders -->
<string name="welcome_user">Welcome, %1$s!</string>
<string name="user_age">%1$s is %2$d years old</string>
<string name="price_format">Price: $%1$.2f</string>

<!-- HTML formatting -->
<string name="formatted_text">This is <b>bold</b> and <i>italic</i> text</string>
<string name="link_text">Visit our <a href="https://example.com">website</a></string>
```

### String Arrays
```xml
<!-- String arrays -->
<string-array name="days_of_week">
    <item>Sunday</item>
    <item>Monday</item>
    <item>Tuesday</item>
    <item>Wednesday</item>
    <item>Thursday</item>
    <item>Friday</item>
    <item>Saturday</item>
</string-array>

<string-array name="months">
    <item>January</item>
    <item>February</item>
    <item>March</item>
    <!-- ... more months -->
</string-array>
```

### Using Strings in Kotlin
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Basic string usage
        val welcomeText = getString(R.string.welcome_message)
        
        // String with formatting
        val userName = "John"
        val welcomeUser = getString(R.string.welcome_user, userName)
        
        // String with multiple parameters
        val name = "Alice"
        val age = 25
        val userInfo = getString(R.string.user_age, name, age)
        
        // Price formatting
        val price = 19.99
        val priceText = getString(R.string.price_format, price)
        
        // HTML formatting
        val formattedText = Html.fromHtml(getString(R.string.formatted_text), Html.FROM_HTML_MODE_COMPACT)
        
        // String arrays
        val days = resources.getStringArray(R.array.days_of_week)
    }
}
```

## 🎨 Color Resources

### Color Definitions
```xml
<!-- res/values/colors.xml -->
<resources>
    <!-- Brand Colors -->
    <color name="brand_primary">#FF6200EE</color>
    <color name="brand_primary_dark">#FF3700B3</color>
    <color name="brand_accent">#FF03DAC5</color>
    
    <!-- Semantic Colors -->
    <color name="success_green">#FF4CAF50</color>
    <color name="error_red">#FFF44336</color>
    <color name="warning_orange">#FFFF9800</color>
    <color name="info_blue">#FF2196F3</color>
    
    <!-- Neutral Colors -->
    <color name="white">#FFFFFFFF</color>
    <color name="black">#FF000000</color>
    <color name="gray_light">#FFF5F5F5</color>
    <color name="gray_medium">#FF9E9E9E</color>
    <color name="gray_dark">#FF424242</color>
    
    <!-- Text Colors -->
    <color name="text_primary">#FF212121</color>
    <color name="text_secondary">#FF757575</color>
    <color name="text_hint">#FFBDBDBD</color>
</resources>
```

### Color State Lists
```xml
<!-- res/color/button_text_color.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:color="@color/white"/>
    <item android:state_enabled="false" android:color="@color/gray_medium"/>
    <item android:color="@color/text_primary"/>
</selector>
```

### Using Colors in Kotlin
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Get color resource
        val primaryColor = ContextCompat.getColor(this, R.color.brand_primary)
        
        // Set background color
        findViewById<View>(R.id.container).setBackgroundColor(primaryColor)
        
        // Set text color
        findViewById<TextView>(R.id.textView).setTextColor(
            ContextCompat.getColor(this, R.color.text_primary)
        )
    }
}
```

## 📏 Dimension Resources

### Dimension Definitions
```xml
<!-- res/values/dimens.xml -->
<resources>
    <!-- Spacing -->
    <dimen name="spacing_tiny">4dp</dimen>
    <dimen name="spacing_small">8dp</dimen>
    <dimen name="spacing_medium">16dp</dimen>
    <dimen name="spacing_large">24dp</dimen>
    <dimen name="spacing_xlarge">32dp</dimen>
    
    <!-- Text Sizes -->
    <dimen name="text_caption">12sp</dimen>
    <dimen name="text_body">14sp</dimen>
    <dimen name="text_subtitle">16sp</dimen>
    <dimen name="text_title">20sp</dimen>
    <dimen name="text_headline">24sp</dimen>
    
    <!-- Component Sizes -->
    <dimen name="button_height">48dp</dimen>
    <dimen name="card_corner_radius">8dp</dimen>
    <dimen name="toolbar_height">56dp</dimen>
</resources>
```

### Using Dimensions in Layouts
```xml
<!-- Using dimensions in layout -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="@dimen/spacing_medium">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="@dimen/text_title"
        android:layout_marginBottom="@dimen/spacing_small"
        android:text="@string/welcome_message"/>

    <Button
        android:layout_width="match_parent"
        android:layout_height="@dimen/button_height"
        android:text="@string/button_submit"/>

</LinearLayout>
```

## 🎭 Style and Theme Resources

### Style Definitions
```xml
<!-- res/values/styles.xml -->
<resources>
    <!-- Button Styles -->
    <style name="Button.Primary" parent="Widget.MaterialComponents.Button">
        <item name="android:textColor">@color/white</item>
        <item name="backgroundTint">@color/brand_primary</item>
        <item name="android:textSize">@dimen/text_body</item>
        <item name="android:padding">@dimen/spacing_medium</item>
    </style>

    <style name="Button.Secondary" parent="Widget.MaterialComponents.Button.OutlinedButton">
        <item name="android:textColor">@color/brand_primary</item>
        <item name="strokeColor">@color/brand_primary</item>
        <item name="android:textSize">@dimen/text_body</item>
    </style>

    <!-- Text Styles -->
    <style name="TextAppearance.App.Headline" parent="TextAppearance.MaterialComponents.Headline4">
        <item name="android:textColor">@color/text_primary</item>
        <item name="android:textSize">@dimen/text_headline</item>
        <item name="android:fontFamily">@font/roboto_bold</item>
    </style>

    <style name="TextAppearance.App.Body" parent="TextAppearance.MaterialComponents.Body1">
        <item name="android:textColor">@color/text_primary</item>
        <item name="android:textSize">@dimen/text_body</item>
        <item name="android:fontFamily">@font/roboto_regular</item>
    </style>

    <!-- Card Styles -->
    <style name="Card.App" parent="Widget.MaterialComponents.CardView">
        <item name="cardCornerRadius">@dimen/card_corner_radius</item>
        <item name="cardElevation">4dp</item>
        <item name="android:layout_margin">@dimen/spacing_small</item>
    </style>
</resources>
```

### Theme Definitions
```xml
<!-- res/values/themes.xml -->
<resources>
    <!-- Base application theme -->
    <style name="Theme.MyApp" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand colors -->
        <item name="colorPrimary">@color/brand_primary</item>
        <item name="colorPrimaryVariant">@color/brand_primary_dark</item>
        <item name="colorOnPrimary">@color/white</item>
        
        <!-- Secondary brand colors -->
        <item name="colorSecondary">@color/brand_accent</item>
        <item name="colorSecondaryVariant">@color/brand_accent</item>
        <item name="colorOnSecondary">@color/black</item>
        
        <!-- Status bar color -->
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
        
        <!-- Text appearances -->
        <item name="textAppearanceHeadline4">@style/TextAppearance.App.Headline</item>
        <item name="textAppearanceBody1">@style/TextAppearance.App.Body</item>
        
        <!-- Component styles -->
        <item name="materialButtonStyle">@style/Button.Primary</item>
        <item name="materialCardViewStyle">@style/Card.App</item>
    </style>

    <!-- Dark theme variant -->
    <style name="Theme.MyApp.Dark" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <!-- Dark theme colors -->
        <item name="android:windowBackground">@color/black</item>
        <item name="colorSurface">@color/gray_dark</item>
        <item name="colorOnSurface">@color/white</item>
    </style>
</resources>
```

### Using Styles in Layouts
```xml
<!-- Applying styles to views -->
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    style="@style/Button.Primary"
    android:text="@string/button_submit"/>

<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/welcome_message"
    android:textAppearance="@style/TextAppearance.App.Headline"/>

<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    style="@style/Card.App">
    <!-- Card content -->
</com.google.android.material.card.MaterialCardView>
```

## 🌍 Localization

### String Localization
```xml
<!-- res/values/strings.xml (Default/English) -->
<resources>
    <string name="welcome_message">Welcome to my app!</string>
    <string name="button_submit">Submit</string>
    <string name="button_cancel">Cancel</string>
</resources>

<!-- res/values-es/strings.xml (Spanish) -->
<resources>
    <string name="welcome_message">¡Bienvenido a mi aplicación!</string>
    <string name="button_submit">Enviar</string>
    <string name="button_cancel">Cancelar</string>
</resources>

<!-- res/values-fr/strings.xml (French) -->
<resources>
    <string name="welcome_message">Bienvenue dans mon application !</string>
    <string name="button_submit">Soumettre</string>
    <string name="button_cancel">Annuler</string>
</resources>
```

### Layout Localization
```xml
<!-- res/layout/activity_main.xml (Default) -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/welcome_message"/>

</LinearLayout>

<!-- res/layout-rtl/activity_main.xml (Right-to-left languages) -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:layoutDirection="rtl">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/welcome_message"
        android:textAlignment="viewStart"/>

</LinearLayout>
```

## 📱 Screen Density Support

### Drawable Organization by Density
```
res/
├── drawable-mdpi/     # 160 dpi (1x)
├── drawable-hdpi/     # 240 dpi (1.5x)
├── drawable-xhdpi/    # 320 dpi (2x)
├── drawable-xxhdpi/   # 480 dpi (3x)
└── drawable-xxxhdpi/  # 640 dpi (4x)
```

### Screen Size Support
```
res/
├── layout/            # Default layout
├── layout-small/      # Small screens
├── layout-normal/     # Normal screens
├── layout-large/      # Large screens
└── layout-xlarge/     # Extra large screens
```

## 🔧 Resource Best Practices

### Naming Conventions
```yaml
# Colors:
- Use descriptive names: brand_primary, text_primary
- Group related colors: button_primary, button_secondary
- Use semantic names: success_green, error_red

# Strings:
- Use descriptive names: welcome_message, button_submit
- Group by feature: login_title, login_button
- Use consistent naming: action_save, action_delete

# Dimensions:
- Use descriptive names: spacing_medium, text_title
- Group by purpose: margin_small, padding_large
- Use consistent units: dp for layouts, sp for text

# Drawables:
- Use descriptive names: ic_home, bg_button
- Group by type: ic_ (icons), bg_ (backgrounds)
- Use consistent naming: selector_button, shape_rounded
```

### Organization Guidelines
```yaml
# Do:
- Group related resources together
- Use consistent naming conventions
- Provide alternative resources for different configurations
- Keep resources organized in subdirectories
- Use resource qualifiers appropriately

# Don't:
- Hardcode values in layouts
- Use inconsistent naming
- Mix different resource types in the same file
- Create resources that are too specific
- Ignore localization needs
```

### Performance Considerations
```yaml
# Optimization Tips:
- Use vector drawables when possible
- Optimize image sizes for target densities
- Use WebP format for better compression
- Minimize resource file sizes
- Use resource qualifiers efficiently
```

## 🚨 Common Resource Issues

### Missing Resources
```kotlin
// Handle missing resources gracefully
try {
    val color = ContextCompat.getColor(this, R.color.missing_color)
} catch (Resources.NotFoundException e) {
    // Use fallback color
    val fallbackColor = ContextCompat.getColor(this, R.color.default_color)
}
```

### Resource Conflicts
```xml
<!-- Avoid resource conflicts by using unique names -->
<!-- Good -->
<color name="button_primary_background">#FF6200EE</color>
<color name="card_primary_background">#FF6200EE</color>

<!-- Bad -->
<color name="primary">#FF6200EE</color>
<color name="primary">#FF3700B3</color>  <!-- Conflict! -->
```

## 📚 Additional Resources

### Official Documentation
- [Android Resources](https://developer.android.com/guide/topics/resources)
- [Resource Types](https://developer.android.com/guide/topics/resources/providing-resources)
- [Localization](https://developer.android.com/guide/topics/resources/localization)

### Best Practices
- [Material Design Resources](https://material.io/design)
- [Android Resource Naming](https://developer.android.com/guide/topics/resources/providing-resources#ResourceNaming)

### Tools
- [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/)
- [Vector Asset Studio](https://developer.android.com/studio/write/vector-assets)

---

**Next**: [Lab Project - Hello World App](../labs/hello-world-app/README.md)
