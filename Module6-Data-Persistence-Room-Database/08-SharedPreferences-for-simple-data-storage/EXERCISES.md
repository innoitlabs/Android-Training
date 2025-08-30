# SharedPreferences Exercises

## Practice Problems to Reinforce Learning

Complete these exercises to master SharedPreferences implementation in Android.

## Easy Level Exercises

### Exercise 1: First-Time User Detection
**Objective:** Implement a feature that detects if the user is opening the app for the first time.

**Requirements:**
- Create a boolean preference called `is_first_time`
- Set it to `true` by default
- When the app starts, check this value
- If `true`, show a welcome message and set it to `false`
- If `false`, proceed normally

**Expected Output:**
```
First time: "Welcome! This is your first time using the app."
Subsequent times: No special message
```

### Exercise 2: Simple Counter with Persistence
**Objective:** Create a counter that persists its value across app restarts.

**Requirements:**
- Display a counter value
- Add increment and decrement buttons
- Save the counter value to SharedPreferences
- Load the saved value when the app starts
- Counter should not go below 0

**Expected Output:**
```
Counter: 5
[+] [-] buttons
Value persists after app restart
```

### Exercise 3: User's Favorite Color
**Objective:** Save and display the user's favorite color preference.

**Requirements:**
- Create a dropdown/spinner with color options
- Save the selected color to SharedPreferences
- Display the saved color when the app starts
- Provide a default color if none is selected

**Expected Output:**
```
Favorite Color: [Dropdown with colors]
Selected: Blue (saved and restored)
```

## Intermediate Level Exercises

### Exercise 4: Multiple Preference Categories
**Objective:** Organize preferences into different categories using multiple SharedPreferences files.

**Requirements:**
- Create separate preference files for:
  - User profile (`user_prefs`)
  - App settings (`app_prefs`)
  - Game data (`game_prefs`)
- Implement save/load for each category
- Add a "Clear Category" button for each

**Expected Output:**
```
User Profile: [Save/Load/Clear]
App Settings: [Save/Load/Clear]
Game Data: [Save/Load/Clear]
```

### Exercise 5: Data Validation and Error Handling
**Objective:** Implement robust data validation and error handling for SharedPreferences.

**Requirements:**
- Validate user input before saving
- Handle edge cases (empty strings, invalid numbers)
- Show appropriate error messages
- Provide fallback values for invalid data
- Log validation errors

**Expected Output:**
```
Input validation with error messages
Graceful handling of invalid data
Fallback to default values
```

### Exercise 6: Settings Screen with Various Types
**Objective:** Create a comprehensive settings screen with different preference types.

**Requirements:**
- Text input (username, email)
- Number input (age, score)
- Boolean switches (notifications, dark mode)
- Dropdown selection (language, theme)
- Save all settings at once
- Reset to defaults option

**Expected Output:**
```
Complete settings form
Validation and error handling
Save/Reset functionality
```

## Advanced Level Exercises

### Exercise 7: Preference Migration System
**Objective:** Implement a system to migrate preferences when app version changes.

**Requirements:**
- Track app version in SharedPreferences
- Detect version changes
- Migrate old preference keys to new ones
- Handle preference format changes
- Maintain backward compatibility

**Expected Output:**
```
Version tracking
Automatic migration
Backward compatibility
```

### Exercise 8: Preference Change Listeners
**Objective:** Implement listeners to react to preference changes in real-time.

**Requirements:**
- Register preference change listeners
- Update UI immediately when preferences change
- Handle listener lifecycle properly
- Implement custom preference change callbacks

**Expected Output:**
```
Real-time UI updates
Proper listener management
Custom callback handling
```

### Exercise 9: Preference Backup and Restore
**Objective:** Create a system to backup and restore SharedPreferences data.

**Requirements:**
- Export preferences to JSON file
- Import preferences from JSON file
- Validate imported data
- Handle import conflicts
- Provide backup/restore UI

**Expected Output:**
```
Backup to file
Restore from file
Data validation
Conflict resolution
```

### Exercise 10: Encrypted SharedPreferences
**Objective:** Implement secure storage using EncryptedSharedPreferences.

**Requirements:**
- Add security library dependency
- Create encrypted preference instance
- Store sensitive data (passwords, tokens)
- Handle encryption/decryption errors
- Implement secure data access patterns

**Expected Output:**
```
Secure data storage
Encryption/decryption
Error handling
Secure access patterns
```

## Bonus Challenges

### Challenge 1: Preference Analytics
Track how often users change specific preferences and store analytics data.

### Challenge 2: Multi-User Support
Implement support for multiple user profiles with separate preference sets.

### Challenge 3: Cloud Sync Simulation
Create a mock cloud sync system that simulates syncing preferences across devices.

### Challenge 4: Preference Search
Implement a search functionality to find specific preferences across all preference files.

### Challenge 5: Preference Export Formats
Support multiple export formats (JSON, XML, CSV) for preference data.

## Solutions and Hints

### General Tips:
1. Always use `apply()` for better performance
2. Provide meaningful default values
3. Group related preferences together
4. Handle null values appropriately
5. Test persistence across app restarts

### Common Pitfalls to Avoid:
- Forgetting to call `apply()` or `commit()`
- Not providing default values
- Storing sensitive data in plain SharedPreferences
- Not handling data type mismatches
- Ignoring preference file naming conflicts

### Testing Your Solutions:
1. Test data persistence by restarting the app
2. Test with different data types and values
3. Test edge cases (empty values, null values)
4. Test performance with large amounts of data
5. Test on different Android versions

## Submission Guidelines

For each exercise:
1. Create a new branch in your project
2. Implement the required functionality
3. Add comments explaining your approach
4. Test thoroughly
5. Document any challenges faced
6. Share your solution with peers for review

## Learning Outcomes

After completing these exercises, you should be able to:
- Implement various SharedPreferences use cases
- Handle data validation and error scenarios
- Organize preferences effectively
- Implement advanced preference features
- Apply best practices in real-world scenarios

Happy coding! 🚀
