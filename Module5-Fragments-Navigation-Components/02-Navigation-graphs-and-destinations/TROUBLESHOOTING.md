# Troubleshooting Guide for Navigation Graphs

## Common Issues and Solutions

### 1. Build Errors

#### Issue: "Cannot resolve symbol 'HomeFragmentDirections'"
**Cause**: Safe Args plugin not properly configured or not generating classes.

**Solutions**:
1. Verify Safe Args plugin is added to project-level `build.gradle.kts`:
   ```kotlin
   plugins {
       id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
   }
   ```

2. Verify Safe Args plugin is applied in app-level `build.gradle.kts`:
   ```kotlin
   plugins {
       id("androidx.navigation.safeargs.kotlin")
   }
   ```

3. Clean and rebuild project:
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

4. Invalidate caches and restart Android Studio:
   - File → Invalidate Caches and Restart

#### Issue: "Navigation graph not found"
**Cause**: Navigation graph file not in correct location or has syntax errors.

**Solutions**:
1. Ensure navigation graph is in `res/navigation/` folder
2. Check XML syntax in navigation graph
3. Verify file name matches reference in layout

#### Issue: "Fragment class not found"
**Cause**: Fragment class doesn't exist or package name is incorrect.

**Solutions**:
1. Verify fragment class exists and extends `Fragment`
2. Check package name in navigation graph matches actual package
3. Ensure proper imports in fragment class

---

### 2. Runtime Errors

#### Issue: "NavHostFragment not found"
**Cause**: NavHostFragment not properly configured in layout.

**Solutions**:
1. Verify NavHostFragment in `activity_main.xml`:
   ```xml
   <androidx.fragment.app.FragmentContainerView
       android:id="@+id/nav_host_fragment"
       android:name="androidx.navigation.fragment.NavHostFragment"
       app:navGraph="@navigation/nav_graph"
       app:defaultNavHost="true" />
   ```

2. Check that navigation graph ID matches

#### Issue: "Navigation action not found"
**Cause**: Action ID doesn't exist in navigation graph.

**Solutions**:
1. Verify action exists in navigation graph
2. Check action ID spelling in code
3. Rebuild project after adding new actions

#### Issue: "Fragment not found in navigation graph"
**Cause**: Fragment not properly defined in navigation graph.

**Solutions**:
1. Check fragment ID in navigation graph
2. Verify fragment class name is correct
3. Ensure fragment is in the correct package

---

### 3. Navigation Issues

#### Issue: Navigation not working
**Cause**: NavController not properly set up or action not defined.

**Solutions**:
1. Verify NavController setup:
   ```kotlin
   val navController = findNavController()
   navController.navigate(R.id.action_homeFragment_to_detailFragment)
   ```

2. Check action exists in navigation graph
3. Ensure fragment is in the same navigation graph

#### Issue: Back button not working
**Cause**: Navigation graph not properly configured or back stack issues.

**Solutions**:
1. Verify `app:defaultNavHost="true"` is set
2. Check that fragments are in the same navigation graph
3. Ensure proper back stack handling

#### Issue: Data not passing between fragments
**Cause**: Safe Args not working or arguments not properly defined.

**Solutions**:
1. Check argument definition in navigation graph:
   ```xml
   <argument
       android:name="username"
       app:argType="string" />
   ```

2. Verify Safe Args usage:
   ```kotlin
   val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John")
   findNavController().navigate(action)
   ```

3. Check argument reception:
   ```kotlin
   private val args: DetailFragmentArgs by navArgs()
   ```

---

### 4. Bottom Navigation Issues

#### Issue: Bottom navigation not working
**Cause**: Bottom navigation not properly connected to navigation controller.

**Solutions**:
1. Verify setup in activity:
   ```kotlin
   val navController = navHostFragment.navController
   bottomNav.setupWithNavController(navController)
   ```

2. Check menu IDs match fragment IDs in navigation graph
3. Ensure navigation graph contains all fragments

#### Issue: Bottom navigation highlighting issues
**Cause**: Menu item IDs don't match fragment IDs.

**Solutions**:
1. Ensure menu item IDs match fragment IDs exactly
2. Check navigation graph fragment IDs
3. Verify menu file is properly referenced

---

### 5. Safe Args Issues

#### Issue: "Cannot resolve symbol 'Directions'"
**Cause**: Safe Args classes not generated.

**Solutions**:
1. Clean and rebuild project
2. Check Safe Args plugin configuration
3. Verify argument types are supported

#### Issue: "Argument type not supported"
**Cause**: Using unsupported argument type.

**Solutions**:
1. Use supported argument types:
   - `string`, `integer`, `long`, `float`, `boolean`
   - `string[]`, `int[]`, `long[]`, `float[]`, `boolean[]`
   - Custom types with `@Parcelize`

2. For custom objects, implement `Parcelable`:
   ```kotlin
   @Parcelize
   data class User(val name: String, val age: Int) : Parcelable
   ```

#### Issue: "Default value not working"
**Cause**: Default value not properly set or type mismatch.

**Solutions**:
1. Check default value syntax:
   ```xml
   <argument
       android:name="username"
       app:argType="string"
       android:defaultValue="Guest" />
   ```

2. Ensure default value type matches argument type

---

### 6. Performance Issues

#### Issue: Slow navigation
**Cause**: Heavy fragment initialization or memory leaks.

**Solutions**:
1. Optimize fragment initialization
2. Use `setMaxLifecycle` for fragment lifecycle management
3. Avoid heavy operations in `onCreateView`

#### Issue: Memory leaks
**Cause**: Fragments not properly cleaned up.

**Solutions**:
1. Use `viewLifecycleOwner` for LiveData observations
2. Properly handle fragment lifecycle
3. Avoid static references to fragments

---

### 7. Testing Issues

#### Issue: Navigation tests failing
**Cause**: Test setup not properly configured.

**Solutions**:
1. Use `launchFragmentInContainer` for fragment testing
2. Mock NavController for unit tests
3. Use `TestNavHostController` for integration tests

#### Issue: Safe Args not working in tests
**Cause**: Test environment not properly set up.

**Solutions**:
1. Ensure test dependencies are included
2. Use proper test annotations
3. Mock Safe Args classes if needed

---

### 8. Debugging Tips

#### Enable Navigation Logging
Add to your application class or activity:
```kotlin
NavController.OnDestinationChangedListener { _, destination, _ ->
    Log.d("Navigation", "Navigated to ${destination.label}")
}
```

#### Check Navigation State
```kotlin
val navController = findNavController()
Log.d("Navigation", "Current destination: ${navController.currentDestination}")
Log.d("Navigation", "Back stack: ${navController.backQueue}")
```

#### Verify Safe Args Generation
Check that generated classes exist in:
```
app/build/generated/source/navigation-args/
```

---

### 9. Common Configuration Mistakes

#### Mistake: Wrong package name
**Solution**: Ensure package names match exactly in all files.

#### Mistake: Missing dependencies
**Solution**: Verify all Navigation Component dependencies are included.

#### Mistake: Incorrect navigation graph reference
**Solution**: Check that `app:navGraph` references the correct file.

#### Mistake: Fragment not in navigation graph
**Solution**: Ensure all fragments used in navigation are defined in the graph.

---

### 10. Getting Help

If you're still experiencing issues:

1. **Check Logcat**: Look for specific error messages
2. **Verify Dependencies**: Ensure all versions are compatible
3. **Test in Isolation**: Create a minimal example to reproduce the issue
4. **Check Documentation**: Refer to official Android Navigation documentation
5. **Community Support**: Use Stack Overflow or Android Developer forums

### Useful Commands

```bash
# Clean and rebuild
./gradlew clean build

# Check for dependency conflicts
./gradlew app:dependencies

# Run tests
./gradlew test

# Generate Safe Args classes
./gradlew generateSafeArgs
```

### Resources

- [Android Navigation Component Documentation](https://developer.android.com/guide/navigation)
- [Safe Args Documentation](https://developer.android.com/guide/navigation/navigation-pass-data)
- [Navigation Testing Guide](https://developer.android.com/guide/navigation/navigation-testing)
- [Android Developer Forums](https://developer.android.com/community)
