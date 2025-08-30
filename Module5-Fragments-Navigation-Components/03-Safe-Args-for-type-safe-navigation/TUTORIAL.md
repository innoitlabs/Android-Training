# Safe Args Tutorial - Step by Step Implementation

## Overview
This tutorial will guide you through implementing Safe Args in an Android project, from basic setup to advanced usage with complex objects.

## Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Basic knowledge of Android Fragments and Navigation Component
- Kotlin programming experience

## Step 1: Project Setup

### 1.1 Add Safe Args Plugin
In your project-level `build.gradle.kts`:
```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}
```

### 1.2 Apply Plugin in App Module
In your app-level `build.gradle.kts`:
```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin")
}
```

### 1.3 Add Navigation Dependencies
```kotlin
dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
}
```

### 1.4 Sync Project
Click "Sync Now" in Android Studio and wait for the sync to complete.

## Step 2: Create Navigation Graph

### 2.1 Create Navigation Resource
1. Right-click on `res` folder
2. Select `New` → `Android Resource File`
3. Choose `Navigation` as Resource type
4. Name it `nav_graph`
5. Click OK

### 2.2 Define Navigation Structure
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.safeargs.HomeFragment"
        android:label="Home">
        <action
            android:id="@+id/action_homeFragment_to_detailFragment"
            app:destination="@id/detailFragment" />
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.safeargs.DetailFragment"
        android:label="Detail">
        <argument
            android:name="username"
            app:argType="string" />
        <argument
            android:name="age"
            app:argType="integer" />
        <argument
            android:name="isActive"
            app:argType="boolean"
            android:defaultValue="true" />
    </fragment>
</navigation>
```

## Step 3: Configure MainActivity

### 3.1 Update MainActivity.kt
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

### 3.2 Update activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/nav_graph" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 4: Create Fragments

### 4.1 Create HomeFragment
```kotlin
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val button = view.findViewById<Button>(R.id.navigateButton)
        button.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToDetailFragment("Alice", 25, true)
            findNavController().navigate(action)
        }

        return view
    }
}
```

### 4.2 Create DetailFragment
```kotlin
class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val textView = view.findViewById<TextView>(R.id.detailText)
        textView.text = "Hello ${args.username}, you are ${args.age} years old!"

        return view
    }
}
```

## Step 5: Build and Test

### 5.1 Build Project
1. Click `Build` → `Make Project`
2. Wait for build to complete
3. Check for any errors

### 5.2 Run App
1. Connect device or start emulator
2. Click `Run` → `Run 'app'`
3. Test navigation between fragments

## Step 6: Advanced Usage - Complex Objects

### 6.1 Create Parcelable Data Class
```kotlin
@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String
) : Parcelable
```

### 6.2 Add to Navigation Graph
```xml
<argument
    android:name="user"
    app:argType="com.example.safeargs.User"
    app:nullable="true" />
```

### 6.3 Pass Complex Object
```kotlin
val user = User(1, "Bob", "bob@example.com")
val action = HomeFragmentDirections
    .actionHomeFragmentToDetailFragment("", 0, false, user)
findNavController().navigate(action)
```

### 6.4 Receive Complex Object
```kotlin
if (args.user != null) {
    val user = args.user!!
    textView.text = "User: ${user.name}\nEmail: ${user.email}"
}
```

## Step 7: Best Practices

### 7.1 Input Validation
Always validate input before navigation:
```kotlin
if (name.isEmpty()) {
    Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
    return@setOnClickListener
}
```

### 7.2 Error Handling
Handle potential errors gracefully:
```kotlin
val age = ageText.toIntOrNull()
if (age == null || age <= 0) {
    Toast.makeText(context, "Please enter a valid age", Toast.LENGTH_SHORT).show()
    return@setOnClickListener
}
```

### 7.3 Default Values
Use default values for optional arguments:
```xml
<argument
    android:name="isActive"
    app:argType="boolean"
    android:defaultValue="true" />
```

## Step 8: Testing

### 8.1 Manual Testing
- Test navigation with valid data
- Test navigation with invalid data
- Test back navigation
- Test configuration changes (rotation)

### 8.2 Unit Testing
```kotlin
@Test
fun testSafeArgsNavigation() {
    val action = HomeFragmentDirections
        .actionHomeFragmentToDetailFragment("John", 25, true)
    
    assertEquals("John", action.username)
    assertEquals(25, action.age)
    assertTrue(action.isActive)
}
```

## Common Issues and Solutions

### Issue 1: Generated Classes Not Found
**Solution**: Clean and rebuild project
```bash
./gradlew clean
./gradlew build
```

### Issue 2: Navigation Not Working
**Solution**: Check NavHostFragment setup and navigation graph

### Issue 3: Type Mismatch Errors
**Solution**: Verify argument types in navigation graph match usage

## Next Steps

1. **Explore the complete project** in the `SafeArgs/` folder
2. **Try the exercises** in `EXERCISES.md`
3. **Experiment with different argument types**
4. **Implement in your own projects**

## Resources

- [Navigation Component Documentation](https://developer.android.com/guide/navigation)
- [Safe Args Documentation](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args)
- [Parcelable Documentation](https://developer.android.com/reference/android/os/Parcelable)

---

**Note**: This tutorial assumes you're using the latest versions of Android Studio and the Navigation Component. Adjust version numbers as needed for your specific setup.
