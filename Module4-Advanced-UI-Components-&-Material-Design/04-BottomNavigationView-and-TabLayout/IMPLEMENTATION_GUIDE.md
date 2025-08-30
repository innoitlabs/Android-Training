# Implementation Guide: BottomNavigationView and TabLayout

## Step-by-Step Implementation

### Step 1: Update Dependencies

First, we need to add ViewPager2 dependency to `app/build.gradle.kts`:

```kotlin
dependencies {
    // ... existing dependencies ...
    implementation("androidx.viewpager2:viewpager2:1.1.0")
}
```

### Step 2: Create Menu Resource

Create `res/menu/bottom_nav_menu.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/nav_home"
        android:title="Home"
        android:icon="@android:drawable/ic_menu_view"/>
    <item
        android:id="@+id/nav_search"
        android:title="Search"
        android:icon="@android:drawable/ic_menu_search"/>
    <item
        android:id="@+id/nav_profile"
        android:title="Profile"
        android:icon="@android:drawable/ic_menu_myplaces"/>
</menu>
```

### Step 3: Update Main Layout

Replace `activity_main.xml` with:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu" />
</LinearLayout>
```

### Step 4: Create Fragment Layouts

#### fragment_home.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.tabs.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>
</LinearLayout>
```

#### fragment_search.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Search Screen"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp"/>

    <EditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter search term..."
        android:padding="12dp"
        android:background="@android:drawable/editbox_background"/>

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Search"
        android:layout_marginTop="16dp"/>
</LinearLayout>
```

#### fragment_profile.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Profile Screen"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="User Name: John Doe"
        android:textSize="16sp"
        android:layout_marginBottom="8dp"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Email: john.doe@example.com"
        android:textSize="16sp"
        android:layout_marginBottom="8dp"/>

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Edit Profile"
        android:layout_marginTop="16dp"/>
</LinearLayout>
```

### Step 5: Create Kotlin Classes

#### MainActivity.kt
```kotlin
package com.example.bottomnavigationviewandtablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bottomnavigationviewandtablayout.databinding.ActivityMainBinding
import com.example.bottomnavigationviewandtablayout.fragments.HomeFragment
import com.example.bottomnavigationviewandtablayout.fragments.ProfileFragment
import com.example.bottomnavigationviewandtablayout.fragments.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load default fragment
        loadFragment(HomeFragment())

        // Set up bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_search -> loadFragment(SearchFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
```

#### HomeFragment.kt
```kotlin
package com.example.bottomnavigationviewandtablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.bottomnavigationviewandtablayout.R
import com.example.bottomnavigationviewandtablayout.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        val adapter = ViewPagerAdapter(requireActivity())
        viewPager.adapter = adapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${position + 1}"
        }.attach()
    }
}
```

#### SearchFragment.kt
```kotlin
package com.example.bottomnavigationviewandtablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigationviewandtablayout.R

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}
```

#### ProfileFragment.kt
```kotlin
package com.example.bottomnavigationviewandtablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bottomnavigationviewandtablayout.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}
```

#### ViewPagerAdapter.kt
```kotlin
package com.example.bottomnavigationviewandtablayout.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bottomnavigationviewandtablayout.fragments.TabFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    
    private val fragments = listOf(
        TabFragment.newInstance("Tab 1 Content"),
        TabFragment.newInstance("Tab 2 Content"),
        TabFragment.newInstance("Tab 3 Content")
    )

    override fun getItemCount() = fragments.size
    
    override fun createFragment(position: Int): Fragment = fragments[position]
}
```

#### TabFragment.kt
```kotlin
package com.example.bottomnavigationviewandtablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TabFragment : Fragment() {

    private var content: String? = null

    companion object {
        fun newInstance(content: String) = TabFragment().apply {
            arguments = Bundle().apply { putString("content", content) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString("content")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textView = TextView(context).apply {
            text = content
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        return textView
    }
}
```

## Testing the Implementation

### Build and Run:
1. Sync project with Gradle files
2. Build the project (Build → Make Project)
3. Run on emulator or device

### Expected Behavior:
- BottomNavigationView appears at the bottom with 3 items
- Tapping Home shows TabLayout with 3 tabs
- Swiping between tabs works smoothly
- Search and Profile tabs show static content
- No build or runtime errors

### Troubleshooting:
- Ensure all dependencies are synced
- Check that all layout files are in correct directories
- Verify package names match in all Kotlin files
- Make sure ViewPager2 dependency is added

## Customization Options

### Changing Icons:
Replace `@android:drawable/` references with custom drawable resources

### Adding More Tabs:
1. Add items to `bottom_nav_menu.xml`
2. Create corresponding fragments
3. Update MainActivity navigation logic

### Styling:
- Customize colors in `colors.xml`
- Apply themes in `themes.xml`
- Use Material Design components for consistent styling
