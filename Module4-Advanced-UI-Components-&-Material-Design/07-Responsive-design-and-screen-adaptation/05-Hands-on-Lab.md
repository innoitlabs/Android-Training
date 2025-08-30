# Hands-on Lab: Building a Responsive Profile Screen

## Lab Overview

In this hands-on lab, you'll build a responsive profile screen that adapts to different screen sizes and orientations. You'll implement the concepts learned in the previous sections.

## Learning Objectives

By the end of this lab, you will be able to:
- Create responsive layouts using ConstraintLayout
- Implement alternative resources for different screen configurations
- Use dimension resources for consistent sizing
- Handle runtime configuration changes
- Test layouts across multiple device configurations

## Project Structure

```
ResponsiveDesign/
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── example/
│           │           └── responsivedesign/
│           │               └── MainActivity.kt
│           └── res/
│               ├── layout/
│               │   └── activity_main.xml (phone portrait)
│               ├── layout-land/
│               │   └── activity_main.xml (landscape)
│               ├── layout-sw600dp/
│               │   └── activity_main.xml (tablet)
│               ├── values/
│               │   ├── dimens.xml (default dimensions)
│               │   └── strings.xml
│               └── values-sw600dp/
│                   └── dimens.xml (tablet dimensions)
```

## Step 1: Create Dimension Resources

### Default Dimensions (res/values/dimens.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Text sizes -->
    <dimen name="text_size_small">14sp</dimen>
    <dimen name="text_size_medium">16sp</dimen>
    <dimen name="text_size_large">20sp</dimen>
    <dimen name="text_size_xlarge">24sp</dimen>
    
    <!-- Margins and padding -->
    <dimen name="margin_small">8dp</dimen>
    <dimen name="margin_medium">16dp</dimen>
    <dimen name="margin_large">24dp</dimen>
    <dimen name="margin_xlarge">32dp</dimen>
    
    <!-- Profile image sizes -->
    <dimen name="profile_image_size_small">80dp</dimen>
    <dimen name="profile_image_size_medium">120dp</dimen>
    <dimen name="profile_image_size_large">200dp</dimen>
    
    <!-- Button sizes -->
    <dimen name="button_height">48dp</dimen>
    <dimen name="button_min_width">120dp</dimen>
</resources>
```

### Tablet Dimensions (res/values-sw600dp/dimens.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Text sizes for tablets -->
    <dimen name="text_size_small">16sp</dimen>
    <dimen name="text_size_medium">20sp</dimen>
    <dimen name="text_size_large">28sp</dimen>
    <dimen name="text_size_xlarge">36sp</dimen>
    
    <!-- Margins and padding for tablets -->
    <dimen name="margin_small">12dp</dimen>
    <dimen name="margin_medium">24dp</dimen>
    <dimen name="margin_large">32dp</dimen>
    <dimen name="margin_xlarge">48dp</dimen>
    
    <!-- Profile image sizes for tablets -->
    <dimen name="profile_image_size_small">120dp</dimen>
    <dimen name="profile_image_size_medium">180dp</dimen>
    <dimen name="profile_image_size_large">250dp</dimen>
    
    <!-- Button sizes for tablets -->
    <dimen name="button_height">56dp</dimen>
    <dimen name="button_min_width">160dp</dimen>
</resources>
```

## Step 2: Create String Resources

### res/values/strings.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Responsive Profile</string>
    <string name="profile_name">John Doe</string>
    <string name="profile_email">john.doe@example.com</string>
    <string name="profile_phone">+1 (555) 123-4567</string>
    <string name="profile_location">San Francisco, CA</string>
    <string name="profile_bio">Android Developer with 5+ years of experience in mobile app development. Passionate about creating user-friendly and responsive applications.</string>
    <string name="edit_profile">Edit Profile</string>
    <string name="save_changes">Save Changes</string>
    <string name="follow">Follow</string>
    <string name="message">Message</string>
    <string name="profile_title">Profile</string>
</resources>
```

## Step 3: Create Phone Portrait Layout

### res/layout/activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="@dimen/margin_medium">

        <!-- Profile Image -->
        <ImageView
            android:id="@+id/profileImage"
            android:layout_width="@dimen/profile_image_size_medium"
            android:layout_height="@dimen/profile_image_size_medium"
            android:src="@drawable/profile_placeholder"
            android:scaleType="centerCrop"
            android:background="@drawable/circle_background"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>

        <!-- Profile Name -->
        <TextView
            android:id="@+id/nameText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="@string/profile_name"
            android:textSize="@dimen/text_size_xlarge"
            android:textStyle="bold"
            android:gravity="center"
            android:layout_marginTop="@dimen/margin_medium"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/profileImage"/>

        <!-- Email -->
        <TextView
            android:id="@+id/emailText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="@string/profile_email"
            android:textSize="@dimen/text_size_medium"
            android:gravity="center"
            android:layout_marginTop="@dimen/margin_small"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/nameText"/>

        <!-- Phone -->
        <TextView
            android:id="@+id/phoneText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="@string/profile_phone"
            android:textSize="@dimen/text_size_medium"
            android:gravity="center"
            android:layout_marginTop="@dimen/margin_small"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/emailText"/>

        <!-- Location -->
        <TextView
            android:id="@+id/locationText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="@string/profile_location"
            android:textSize="@dimen/text_size_medium"
            android:gravity="center"
            android:layout_marginTop="@dimen/margin_small"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/phoneText"/>

        <!-- Bio -->
        <TextView
            android:id="@+id/bioText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="@string/profile_bio"
            android:textSize="@dimen/text_size_medium"
            android:gravity="center"
            android:layout_marginTop="@dimen/margin_large"
            android:lineSpacingExtra="4dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/locationText"/>

        <!-- Action Buttons -->
        <LinearLayout
            android:id="@+id/buttonContainer"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center"
            android:layout_marginTop="@dimen/margin_large"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/bioText">

            <Button
                android:id="@+id/editButton"
                android:layout_width="wrap_content"
                android:layout_height="@dimen/button_height"
                android:text="@string/edit_profile"
                android:layout_marginEnd="@dimen/margin_small"
                android:minWidth="@dimen/button_min_width"/>

            <Button
                android:id="@+id/followButton"
                android:layout_width="wrap_content"
                android:layout_height="@dimen/button_height"
                android:text="@string/follow"
                android:layout_marginStart="@dimen/margin_small"
                android:minWidth="@dimen/button_min_width"/>

        </LinearLayout>

    </androidx.constraintlayout.widget.ConstraintLayout>

</ScrollView>
```

## Step 4: Create Landscape Layout

### res/layout-land/activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/margin_medium">

    <!-- Profile Image -->
    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="@dimen/profile_image_size_small"
        android:layout_height="@dimen/profile_image_size_small"
        android:src="@drawable/profile_placeholder"
        android:scaleType="centerCrop"
        android:background="@drawable/circle_background"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

    <!-- Profile Info Container -->
    <LinearLayout
        android:id="@+id/infoContainer"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_marginStart="@dimen/margin_medium"
        app:layout_constraintStart_toEndOf="@id/profileImage"
        app:layout_constraintEnd_toStartOf="@id/verticalGuideline"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/nameText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_name"
            android:textSize="@dimen/text_size_xlarge"
            android:textStyle="bold"/>

        <TextView
            android:id="@+id/emailText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_email"
            android:textSize="@dimen/text_size_medium"
            android:layout_marginTop="@dimen/margin_small"/>

        <TextView
            android:id="@+id/phoneText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_phone"
            android:textSize="@dimen/text_size_medium"
            android:layout_marginTop="@dimen/margin_small"/>

        <TextView
            android:id="@+id/locationText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_location"
            android:textSize="@dimen/text_size_medium"
            android:layout_marginTop="@dimen/margin_small"/>

    </LinearLayout>

    <!-- Vertical Guideline -->
    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/verticalGuideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuidePercent="0.6"/>

    <!-- Bio and Actions Container -->
    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintStart_toEndOf="@id/verticalGuideline"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/bioText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_bio"
            android:textSize="@dimen/text_size_medium"
            android:lineSpacingExtra="4dp"/>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_marginTop="@dimen/margin_medium">

            <Button
                android:id="@+id/editButton"
                android:layout_width="match_parent"
                android:layout_height="@dimen/button_height"
                android:text="@string/edit_profile"
                android:layout_marginBottom="@dimen/margin_small"/>

            <Button
                android:id="@+id/followButton"
                android:layout_width="match_parent"
                android:layout_height="@dimen/button_height"
                android:text="@string/follow"/>

        </LinearLayout>

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 5: Create Tablet Layout

### res/layout-sw600dp/activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/margin_large">

    <!-- Profile Image -->
    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="@dimen/profile_image_size_large"
        android:layout_height="@dimen/profile_image_size_large"
        android:src="@drawable/profile_placeholder"
        android:scaleType="centerCrop"
        android:background="@drawable/circle_background"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toStartOf="@id/verticalGuideline"/>

    <!-- Vertical Guideline -->
    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/verticalGuideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuidePercent="0.4"/>

    <!-- Profile Info Container -->
    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_marginStart="@dimen/margin_large"
        app:layout_constraintStart_toEndOf="@id/verticalGuideline"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/nameText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_name"
            android:textSize="@dimen/text_size_xlarge"
            android:textStyle="bold"/>

        <TextView
            android:id="@+id/emailText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_email"
            android:textSize="@dimen/text_size_large"
            android:layout_marginTop="@dimen/margin_medium"/>

        <TextView
            android:id="@+id/phoneText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_phone"
            android:textSize="@dimen/text_size_large"
            android:layout_marginTop="@dimen/margin_medium"/>

        <TextView
            android:id="@+id/locationText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_location"
            android:textSize="@dimen/text_size_large"
            android:layout_marginTop="@dimen/margin_medium"/>

        <TextView
            android:id="@+id/bioText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/profile_bio"
            android:textSize="@dimen/text_size_medium"
            android:lineSpacingExtra="6dp"
            android:layout_marginTop="@dimen/margin_large"/>

        <!-- Action Buttons -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:layout_marginTop="@dimen/margin_large">

            <Button
                android:id="@+id/editButton"
                android:layout_width="0dp"
                android:layout_height="@dimen/button_height"
                android:layout_weight="1"
                android:text="@string/edit_profile"
                android:layout_marginEnd="@dimen/margin_medium"/>

            <Button
                android:id="@+id/followButton"
                android:layout_width="0dp"
                android:layout_height="@dimen/button_height"
                android:layout_weight="1"
                android:text="@string/follow"
                android:layout_marginStart="@dimen/margin_medium"/>

        </LinearLayout>

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 6: Create Drawable Resources

### res/drawable/circle_background.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#E0E0E0"/>
    <stroke
        android:width="2dp"
        android:color="#CCCCCC"/>
</shape>
```

### res/drawable/profile_placeholder.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#757575"
        android:pathData="M12,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4zM12,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4z"/>
</vector>
```

## Step 7: Update MainActivity

### MainActivity.kt
```kotlin
package com.example.responsivedesign

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneText: TextView
    private lateinit var locationText: TextView
    private lateinit var bioText: TextView
    private lateinit var editButton: Button
    private lateinit var followButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupClickListeners()
        handleScreenConfiguration()
    }

    private fun initializeViews() {
        nameText = findViewById(R.id.nameText)
        emailText = findViewById(R.id.emailText)
        phoneText = findViewById(R.id.phoneText)
        locationText = findViewById(R.id.locationText)
        bioText = findViewById(R.id.bioText)
        editButton = findViewById(R.id.editButton)
        followButton = findViewById(R.id.followButton)
    }

    private fun setupClickListeners() {
        editButton.setOnClickListener {
            // Handle edit profile action
            showEditProfileDialog()
        }

        followButton.setOnClickListener {
            // Handle follow action
            toggleFollowState()
        }
    }

    private fun handleScreenConfiguration() {
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

    private fun setupTabletUI() {
        // Additional tablet-specific setup if needed
        followButton.text = getString(R.string.follow)
    }

    private fun setupLargePhoneUI() {
        // Additional large phone setup if needed
    }

    private fun setupSmallPhoneUI() {
        // Additional small phone setup if needed
    }

    private fun showEditProfileDialog() {
        // Implementation for edit profile dialog
        // This would typically show a dialog or navigate to an edit screen
    }

    private fun toggleFollowState() {
        val currentText = followButton.text.toString()
        if (currentText == getString(R.string.follow)) {
            followButton.text = "Following"
        } else {
            followButton.text = getString(R.string.follow)
        }
    }
}
```

## Step 8: Testing

### Test on Multiple Configurations

1. **Small Phone (320dp width)**
   - Pixel 4a or similar
   - Verify portrait layout works correctly
   - Check that ScrollView handles overflow

2. **Large Phone (480dp+ width)**
   - Pixel 7 Pro or similar
   - Test both portrait and landscape
   - Verify text sizing and spacing

3. **Tablet (600dp+ width)**
   - Pixel C or similar tablet
   - Test both orientations
   - Verify two-column layout works

### Testing Checklist

- [ ] Portrait layout displays correctly on phones
- [ ] Landscape layout adapts properly
- [ ] Tablet layout shows two-column design
- [ ] Text sizes scale appropriately
- [ ] Buttons are properly sized for touch
- [ ] ScrollView works on small screens
- [ ] No layout overflow or clipping
- [ ] All text is readable
- [ ] Touch targets are appropriate size

## Step 9: Advanced Enhancements

### Optional: Add Animation
```kotlin
// Add smooth transitions when rotating device
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    
    // Animate layout changes
    val rootView = findViewById<View>(android.R.id.content)
    rootView.animate()
        .alpha(0f)
        .setDuration(150)
        .withEndAction {
            rootView.animate()
                .alpha(1f)
                .setDuration(150)
                .start()
        }
        .start()
}
```

### Optional: Add Dynamic Content
```kotlin
// Load profile data from a data source
private fun loadProfileData() {
    // Simulate loading from API or database
    val profile = Profile(
        name = "John Doe",
        email = "john.doe@example.com",
        phone = "+1 (555) 123-4567",
        location = "San Francisco, CA",
        bio = "Android Developer with 5+ years of experience..."
    )
    
    updateUI(profile)
}

private fun updateUI(profile: Profile) {
    nameText.text = profile.name
    emailText.text = profile.email
    phoneText.text = profile.phone
    locationText.text = profile.location
    bioText.text = profile.bio
}
```

## Summary

In this lab, you've successfully created a responsive profile screen that:

1. **Adapts to different screen sizes** using alternative layouts
2. **Uses proper units** (dp, sp) for consistent sizing
3. **Implements ConstraintLayout** for flexible positioning
4. **Provides appropriate resources** for different configurations
5. **Handles runtime changes** gracefully
6. **Follows Material Design** principles

This foundation will serve you well in building responsive Android applications that provide excellent user experiences across all device types!
