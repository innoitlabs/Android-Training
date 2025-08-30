# Testing Guide - Deep Linking Demo App

## Prerequisites

- Android device or emulator running API 24+
- ADB (Android Debug Bridge) installed and configured
- App installed and running

## Testing Methods

### Method 1: ADB Command Line Testing

#### Basic Deep Link Testing

1. **Test Detail Fragment Deep Link**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
   ```
   **Expected Result**: App opens to DetailFragment showing "Item ID from Deep Link: 42"

2. **Test Home Fragment Deep Link**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://home" com.example.deeplinking
   ```
   **Expected Result**: App opens to HomeFragment

3. **Test Different Item IDs**
   ```bash
   # Test with numeric ID
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/123" com.example.deeplinking
   
   # Test with string ID
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/abc" com.example.deeplinking
   
   # Test with special characters
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/user-123" com.example.deeplinking
   ```

#### Edge Case Testing

1. **Test Invalid Deep Links**
   ```bash
   # Invalid URI scheme
   adb shell am start -W -a android.intent.action.VIEW -d "invalid://detail/42" com.example.deeplinking
   
   # Missing itemId parameter
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/" com.example.deeplinking
   
   # Non-existent deep link
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://nonexistent" com.example.deeplinking
   ```

2. **Test App State Scenarios**
   ```bash
   # Test when app is closed
   adb shell am force-stop com.example.deeplinking
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
   
   # Test when app is in background
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
   ```

### Method 2: Browser Testing

1. **Open Browser on Device**
2. **Type Deep Link URI**
   ```
   myapp://detail/42
   ```
3. **Press Enter**
4. **Expected Result**: App should open to DetailFragment

### Method 3: Intent Testing from Another App

1. **Create a simple test app or use existing app**
2. **Add intent code**:
   ```kotlin
   val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://detail/42"))
   startActivity(intent)
   ```
3. **Expected Result**: Deep linking app should open

## Test Scenarios

### Scenario 1: Normal Navigation Flow

1. **Launch app normally**
   - App should open to HomeFragment
   - Verify "Go to Detail (ID: 42)" button is visible

2. **Click button navigation**
   - Click "Go to Detail (ID: 42)" button
   - Should navigate to DetailFragment
   - Should display "Item ID from Deep Link: 42"

3. **Test back navigation**
   - Press back button
   - Should return to HomeFragment

### Scenario 2: Deep Link Navigation

1. **Test direct deep link**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/999" com.example.deeplinking
   ```
   - App should open directly to DetailFragment
   - Should display "Item ID from Deep Link: 999"

2. **Test back navigation from deep link**
   - Press back button
   - Should return to HomeFragment (not launcher)

### Scenario 3: Multiple Deep Links

1. **Test rapid deep link navigation**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/1" com.example.deeplinking
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/2" com.example.deeplinking
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/3" com.example.deeplinking
   ```
   - Each should open DetailFragment with correct ID
   - Back navigation should work correctly

### Scenario 4: App State Testing

1. **Test with app closed**
   ```bash
   adb shell am force-stop com.example.deeplinking
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
   ```

2. **Test with app in background**
   - Open app normally
   - Press home button to background app
   - Execute deep link command
   - App should come to foreground with correct screen

## Expected Behaviors

### ✅ Correct Behaviors

1. **Deep links work correctly**
   - `myapp://detail/{id}` opens DetailFragment with correct ID
   - `myapp://home` opens HomeFragment

2. **Navigation is smooth**
   - No crashes or ANRs
   - Proper back stack behavior
   - Action bar shows correct title

3. **Arguments are passed correctly**
   - Item ID is displayed in DetailFragment
   - No null or empty values

4. **App handles different states**
   - Works when app is closed
   - Works when app is in background
   - Works when app is already open

### ❌ Common Issues to Check

1. **Deep link not working**
   - Check AndroidManifest.xml configuration
   - Verify navigation graph setup
   - Ensure app is installed and running

2. **Navigation crashes**
   - Check fragment class names
   - Verify Safe Args plugin is applied
   - Check for missing dependencies

3. **Arguments not received**
   - Verify argument names in navigation graph
   - Check Safe Args generation
   - Ensure proper argument types

## Debug Commands

### Check App Installation
```bash
adb shell pm list packages | grep deeplinking
```

### Check App Logs
```bash
adb logcat | grep deeplinking
```

### Force Stop App
```bash
adb shell am force-stop com.example.deeplinking
```

### Clear App Data
```bash
adb shell pm clear com.example.deeplinking
```

### Check Intent Filters
```bash
adb shell dumpsys package com.example.deeplinking | grep -A 10 "Activity Resolver Table"
```

## Performance Testing

### Test Deep Link Response Time
```bash
# Measure time to open deep link
time adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
```

### Test Memory Usage
```bash
adb shell dumpsys meminfo com.example.deeplinking
```

## Security Testing

### Test Input Validation
```bash
# Test with very long IDs
adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/$(printf 'a%.0s' {1..1000})" com.example.deeplinking

# Test with special characters
adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/<script>alert('xss')</script>" com.example.deeplinking
```

## Reporting Issues

When reporting issues, include:

1. **Device/Emulator Information**
   - Android version
   - Device model
   - API level

2. **Steps to Reproduce**
   - Exact commands used
   - Expected vs actual behavior

3. **Logs**
   - Relevant logcat output
   - Error messages

4. **Screenshots**
   - Current screen state
   - Error dialogs

---

**Note**: This testing guide covers the basic functionality. For advanced testing scenarios, refer to the exercises in `EXERCISES.md`.
