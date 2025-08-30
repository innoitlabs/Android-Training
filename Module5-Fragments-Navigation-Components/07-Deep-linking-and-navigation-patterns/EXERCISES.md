# Deep Linking Exercises

## Exercise 1: Basic Deep Link Implementation (Easy)

### Objective
Add a deep link for the HomeFragment and implement a simple counter that persists across deep link navigation.

### Steps

1. **Add Home Deep Link**
   - Open `nav_graph.xml`
   - Add a deep link to the homeFragment:
   ```xml
   <deepLink app:uri="myapp://home" />
   ```

2. **Implement Counter in HomeFragment**
   - Add a TextView to display the counter
   - Add a Button to increment the counter
   - Use SharedPreferences to persist the counter value

3. **Test the Implementation**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://home" com.example.deeplinking
   ```

### Expected Result
- The app should open to HomeFragment when using the deep link
- Counter should persist across app restarts
- Counter should increment when button is pressed

---

## Exercise 2: Multiple Arguments Deep Link (Intermediate)

### Objective
Pass two arguments via deep link (e.g., `userId` and `postId`) and implement validation.

### Steps

1. **Update Navigation Graph**
   - Add two arguments to DetailFragment:
   ```xml
   <argument android:name="userId" app:argType="string" />
   <argument android:name="postId" app:argType="string" />
   ```
   - Update the deep link URI:
   ```xml
   <deepLink app:uri="myapp://detail/{userId}/{postId}" />
   ```

2. **Update DetailFragment**
   - Receive both arguments using Safe Args
   - Display both values in the UI
   - Add validation (e.g., check if userId is numeric)

3. **Test Multiple Arguments**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/123/456" com.example.deeplinking
   ```

### Expected Result
- Deep link should accept two parameters
- Both parameters should be displayed in DetailFragment
- Invalid parameters should be handled gracefully

---

## Exercise 3: Search Functionality (Intermediate)

### Objective
Implement a search functionality that accepts query parameters via deep link.

### Steps

1. **Create SearchFragment**
   - Create a new fragment for search results
   - Add a search query argument
   - Display search results (mock data)

2. **Add Search Deep Link**
   ```xml
   <fragment android:id="@+id/searchFragment" ...>
       <argument android:name="query" app:argType="string" />
       <deepLink app:uri="myapp://search?query={query}" />
   </fragment>
   ```

3. **Implement Search Logic**
   - Create a simple search function
   - Filter mock data based on query
   - Display results in a RecyclerView

4. **Test Search Deep Link**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://search?query=android" com.example.deeplinking
   ```

### Expected Result
- Search deep link should work with query parameters
- Search results should be displayed
- Empty queries should be handled appropriately

---

## Exercise 4: Notification Deep Link (Advanced)

### Objective
Trigger deep link from a notification using PendingIntent.

### Steps

1. **Create NotificationHelper**
   ```kotlin
   class NotificationHelper(private val context: Context) {
       fun showDeepLinkNotification(itemId: String) {
           val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://detail/$itemId"))
           val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
           
           val notification = NotificationCompat.Builder(context, CHANNEL_ID)
               .setContentTitle("Deep Link Notification")
               .setContentText("Tap to open detail for item $itemId")
               .setSmallIcon(R.drawable.ic_notification)
               .setContentIntent(pendingIntent)
               .setAutoCancel(true)
               .build()
           
           NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
       }
   }
   ```

2. **Add Notification Permission**
   - Add permission to AndroidManifest.xml
   - Request runtime permission for Android 13+

3. **Create Notification Channel**
   - Implement notification channel creation
   - Handle different Android versions

4. **Test Notification Deep Link**
   - Show notification from app
   - Tap notification to trigger deep link
   - Verify navigation to DetailFragment

### Expected Result
- Notification should appear with deep link
- Tapping notification should open DetailFragment
- Deep link should work even when app is closed

---

## Exercise 5: Web-Based Deep Links (Advanced)

### Objective
Implement web-based deep links (https://yourapp.com/detail/42).

### Steps

1. **Update AndroidManifest.xml**
   ```xml
   <activity android:name=".MainActivity" android:exported="true">
       <intent-filter android:autoVerify="true">
           <action android:name="android.intent.action.VIEW" />
           <category android:name="android.intent.category.DEFAULT" />
           <category android:name="android.intent.category.BROWSABLE" />
           <data android:scheme="https" android:host="yourapp.com" />
       </intent-filter>
   </activity>
   ```

2. **Create Digital Asset Links**
   - Host a `.well-known/assetlinks.json` file
   - Verify app ownership
   - Enable automatic app verification

3. **Handle Web URLs**
   - Parse web URLs in MainActivity
   - Convert to internal deep link format
   - Navigate to appropriate fragment

4. **Test Web Deep Links**
   - Open https://yourapp.com/detail/42 in browser
   - App should open automatically
   - Verify navigation to DetailFragment

### Expected Result
- Web URLs should open the app automatically
- Deep linking should work from web browsers
- App verification should work correctly

---

## Exercise 6: Back Stack Management (Advanced)

### Objective
Handle deep links that should replace the entire back stack.

### Steps

1. **Create Different Navigation Patterns**
   - Normal navigation (add to back stack)
   - Replace back stack navigation
   - Clear and navigate pattern

2. **Implement Back Stack Options**
   ```kotlin
   val options = NavOptions.Builder()
       .setPopUpTo(R.id.homeFragment, true)
       .build()
   navController.navigate(R.id.detailFragment, args, options)
   ```

3. **Add Deep Link Flags**
   - Create different deep link URIs for different behaviors
   - Handle back stack clearing via deep link
   - Implement proper back navigation

4. **Test Back Stack Scenarios**
   ```bash
   # Normal deep link
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
   
   # Replace back stack deep link
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42?clearStack=true" com.example.deeplinking
   ```

### Expected Result
- Different deep link patterns should have different back stack behavior
- Back navigation should work correctly
- Users shouldn't get stuck in unexpected states

---

## Exercise 7: Deep Link Analytics (Advanced)

### Objective
Implement deep link analytics and tracking.

### Steps

1. **Create Deep Link Tracker**
   ```kotlin
   class DeepLinkTracker {
       fun trackDeepLink(uri: Uri, source: String) {
           // Log deep link events
           // Send analytics data
           // Track user behavior
       }
   }
   ```

2. **Track Deep Link Sources**
   - Notification clicks
   - Browser links
   - External app links
   - Search results

3. **Implement Analytics Events**
   - Deep link opened
   - Deep link failed
   - User navigation after deep link
   - Conversion tracking

4. **Add Error Tracking**
   - Invalid deep links
   - Missing parameters
   - Navigation failures
   - Performance metrics

### Expected Result
- Deep link usage should be tracked
- Analytics data should be collected
- Error scenarios should be logged
- Performance metrics should be available

---

## Bonus Exercise: Custom URI Scheme with Multiple Segments

### Objective
Create a custom URI scheme with multiple path segments and complex routing.

### Steps

1. **Design Complex URI Structure**
   ```
   myapp://category/electronics/product/123/review/456
   myapp://user/789/profile/settings
   myapp://search/query?category=books&sort=price
   ```

2. **Implement URI Parser**
   - Parse complex URI structures
   - Extract multiple parameters
   - Handle optional segments

3. **Create Dynamic Navigation**
   - Route to different fragments based on URI
   - Handle nested navigation
   - Support deep navigation trees

4. **Test Complex Deep Links**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW -d "myapp://category/electronics/product/123/review/456" com.example.deeplinking
   ```

### Expected Result
- Complex URIs should be parsed correctly
- Multiple parameters should be extracted
- Navigation should work for deep structures
- Error handling should be robust

---

## Submission Guidelines

### For Each Exercise:
1. **Code Implementation**: Complete the required functionality
2. **Testing**: Test all scenarios and edge cases
3. **Documentation**: Add comments explaining your implementation
4. **Screenshots**: Capture the app behavior for each exercise

### Evaluation Criteria:
- **Functionality**: Does the deep link work as expected?
- **Code Quality**: Is the code clean and well-structured?
- **Error Handling**: Are edge cases handled properly?
- **User Experience**: Is the navigation smooth and intuitive?

### Tips:
- Start with simple exercises and progress to complex ones
- Test thoroughly with different scenarios
- Consider user experience and error cases
- Document your learning and challenges

---

**Note**: These exercises are designed to progressively build your understanding of Android deep linking and navigation patterns. Complete them in order for the best learning experience.
