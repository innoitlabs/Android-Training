# Quick Reference: ViewPager2 + TabLayout

## Essential Dependencies
```kotlin
// build.gradle.kts
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.viewpager2:viewpager2:1.0.0")
```

## Layout Structure
```xml
<!-- activity_main.xml -->
<LinearLayout android:orientation="vertical">
    <com.google.android.material.tabs.TabLayout
        android:id="@+id/tab_layout"
        app:tabMode="fixed"
        app:tabGravity="fill"/>
    
    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/view_pager"/>
</LinearLayout>
```

## MainActivity Setup
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Dashboard"
                2 -> tab.text = "Profile"
            }
        }.attach()
    }
}
```

## ViewPagerAdapter
```kotlin
class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> DashboardFragment()
            2 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}
```

## Fragment Template
```kotlin
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
```

## Key Imports
```kotlin
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.adapter.FragmentStateAdapter
```

## Event Listeners
```kotlin
// Tab Selection Listener
tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) { /* handle selection */ }
    override fun onTabUnselected(tab: TabLayout.Tab?) { /* handle unselection */ }
    override fun onTabReselected(tab: TabLayout.Tab?) { /* handle reselection */ }
})

// Page Change Callback
viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) { /* handle page change */ }
})
```

## Common Customizations

### Add Icons to Tabs
```kotlin
TabLayoutMediator(tabLayout, viewPager) { tab, position ->
    when (position) {
        0 -> {
            tab.text = "Home"
            tab.setIcon(R.drawable.ic_home)
        }
        // ... other tabs
    }
}.attach()
```

### Custom Tab Layouts
```kotlin
val customView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
tab.setCustomView(customView)
```

### Dynamic Tabs
```kotlin
class DynamicViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = mutableListOf<Fragment>()
    
    fun addTab(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
    }
    
    fun removeTab(position: Int) {
        fragments.removeAt(position)
        notifyItemRemoved(position)
    }
    
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}
```

## Best Practices

1. **Use FragmentStateAdapter** for efficient fragment management
2. **Keep 3-5 tabs maximum** for better usability
3. **Use TabLayoutMediator** for synchronization
4. **Handle configuration changes** properly
5. **Add proper logging** for debugging
6. **Test on different screen sizes**

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Tabs not visible | Check Material Design dependency |
| Fragments not showing | Verify FragmentStateAdapter implementation |
| Swipe not working | Ensure ViewPager2 is properly configured |
| Sync issues | Call TabLayoutMediator.attach() |

## Performance Tips

- Use `FragmentStateAdapter` for large numbers of fragments
- Implement proper lifecycle management
- Consider using `RecyclerView` for simple content
- Cache fragments when appropriate
- Monitor memory usage during navigation
