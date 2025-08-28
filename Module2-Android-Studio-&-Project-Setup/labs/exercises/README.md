# Additional Exercises

## 🎯 Practice Exercises

These exercises will help you reinforce your understanding of Android Studio and project setup concepts. Complete them after finishing the main Hello World lab.

## 📝 Exercise 1: Enhanced Hello World

### Objective
Enhance the Hello World app with additional features.

### Requirements
1. **Add a counter** that increments with each button click
2. **Display the counter value** in a separate TextView
3. **Add a reset button** to clear the counter
4. **Use different colors** for the counter display

### Implementation
```kotlin
// Enhanced MainActivity.kt
class MainActivity : AppCompatActivity() {
    
    private var clickCount = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
    }
    
    private fun setupUI() {
        val textView = findViewById<TextView>(R.id.textView)
        val counterView = findViewById<TextView>(R.id.counterView)
        val button = findViewById<Button>(R.id.button)
        val resetButton = findViewById<Button>(R.id.resetButton)
        
        button.setOnClickListener {
            clickCount++
            textView.text = getString(R.string.button_clicked)
            counterView.text = "Clicks: $clickCount"
            
            // Change color based on count
            when (clickCount % 3) {
                0 -> counterView.setTextColor(Color.BLUE)
                1 -> counterView.setTextColor(Color.RED)
                2 -> counterView.setTextColor(Color.GREEN)
            }
        }
        
        resetButton.setOnClickListener {
            clickCount = 0
            textView.text = getString(R.string.hello_world)
            counterView.text = "Clicks: 0"
            counterView.setTextColor(Color.BLACK)
        }
    }
}
```

### Layout Updates
```xml
<!-- Enhanced activity_main.xml -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_world"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/counterView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Clicks: 0"
        android:textSize="18sp"
        android:layout_marginBottom="16dp"/>

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/click_me"
        android:layout_marginBottom="8dp"/>

    <Button
        android:id="@+id/resetButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Reset"
        style="@style/Widget.MaterialComponents.Button.OutlinedButton"/>

</LinearLayout>
```

## 📝 Exercise 2: User Input App

### Objective
Create an app that accepts user input and displays it.

### Requirements
1. **Add an EditText** for user input
2. **Add a submit button** to process the input
3. **Display the input** in a TextView
4. **Add input validation** (non-empty check)

### Implementation
```kotlin
// UserInputActivity.kt
class UserInputActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_input)
        setupUI()
    }
    
    private fun setupUI() {
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        
        submitButton.setOnClickListener {
            val userInput = inputEditText.text.toString().trim()
            
            if (userInput.isEmpty()) {
                inputEditText.error = "Please enter some text"
                return@setOnClickListener
            }
            
            resultTextView.text = "You entered: $userInput"
            inputEditText.text.clear()
        }
    }
}
```

### Layout
```xml
<!-- activity_user_input.xml -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:gravity="center">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Enter your name:"
        android:textSize="18sp"
        android:layout_marginBottom="16dp"/>

    <EditText
        android:id="@+id/inputEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Type here..."
        android:inputType="textPersonName"
        android:layout_marginBottom="16dp"/>

    <Button
        android:id="@+id/submitButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Submit"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/resultTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:textStyle="italic"/>

</LinearLayout>
```

## 📝 Exercise 3: Settings App

### Objective
Create an app that demonstrates different UI components and settings.

### Requirements
1. **Add a Switch** for toggling a feature
2. **Add a SeekBar** for adjusting a value
3. **Add RadioButtons** for selecting options
4. **Display current settings** in a summary

### Implementation
```kotlin
// SettingsActivity.kt
class SettingsActivity : AppCompatActivity() {
    
    private var isFeatureEnabled = false
    private var brightnessLevel = 50
    private var selectedTheme = "Light"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupUI()
    }
    
    private fun setupUI() {
        val featureSwitch = findViewById<Switch>(R.id.featureSwitch)
        val brightnessSeekBar = findViewById<SeekBar>(R.id.brightnessSeekBar)
        val lightRadioButton = findViewById<RadioButton>(R.id.lightRadioButton)
        val darkRadioButton = findViewById<RadioButton>(R.id.darkRadioButton)
        val summaryTextView = findViewById<TextView>(R.id.summaryTextView)
        
        // Switch listener
        featureSwitch.setOnCheckedChangeListener { _, isChecked ->
            isFeatureEnabled = isChecked
            updateSummary(summaryTextView)
        }
        
        // SeekBar listener
        brightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                brightnessLevel = progress
                updateSummary(summaryTextView)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        // Radio button listeners
        lightRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedTheme = "Light"
                updateSummary(summaryTextView)
            }
        }
        
        darkRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedTheme = "Dark"
                updateSummary(summaryTextView)
            }
        }
        
        updateSummary(summaryTextView)
    }
    
    private fun updateSummary(summaryTextView: TextView) {
        val summary = """
            Current Settings:
            Feature Enabled: $isFeatureEnabled
            Brightness Level: $brightnessLevel%
            Theme: $selectedTheme
        """.trimIndent()
        summaryTextView.text = summary
    }
}
```

### Layout
```xml
<!-- activity_settings.xml -->
<ScrollView
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="App Settings"
            android:textSize="24sp"
            android:textStyle="bold"
            android:layout_marginBottom="24dp"/>

        <!-- Feature Switch -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:layout_marginBottom="16dp">

            <TextView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="Enable Feature"
                android:textSize="16sp"/>

            <Switch
                android:id="@+id/featureSwitch"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"/>

        </LinearLayout>

        <!-- Brightness SeekBar -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Brightness Level"
            android:textSize="16sp"
            android:layout_marginBottom="8dp"/>

        <SeekBar
            android:id="@+id/brightnessSeekBar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:max="100"
            android:progress="50"
            android:layout_marginBottom="24dp"/>

        <!-- Theme Selection -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Theme"
            android:textSize="16sp"
            android:layout_marginBottom="8dp"/>

        <RadioGroup
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="24dp">

            <RadioButton
                android:id="@+id/lightRadioButton"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Light Theme"
                android:checked="true"/>

            <RadioButton
                android:id="@+id/darkRadioButton"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Dark Theme"/>

        </RadioGroup>

        <!-- Summary -->
        <TextView
            android:id="@+id/summaryTextView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@drawable/summary_background"
            android:padding="16dp"
            android:textSize="14sp"/>

    </LinearLayout>

</ScrollView>
```

## 📝 Exercise 4: Data Persistence

### Objective
Learn to save and retrieve data using SharedPreferences.

### Requirements
1. **Save user preferences** using SharedPreferences
2. **Load saved preferences** when app starts
3. **Create a settings screen** with persistent data
4. **Handle data migration** for app updates

### Implementation
```kotlin
// DataPersistenceActivity.kt
class DataPersistenceActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_persistence)
        
        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        setupUI()
        loadSavedData()
    }
    
    private fun setupUI() {
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val loadButton = findViewById<Button>(R.id.loadButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val dataTextView = findViewById<TextView>(R.id.dataTextView)
        
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull() ?: 0
            
            with(sharedPreferences.edit()) {
                putString("user_name", name)
                putInt("user_age", age)
                putLong("last_saved", System.currentTimeMillis())
                apply()
            }
            
            Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show()
        }
        
        loadButton.setOnClickListener {
            loadSavedData()
        }
        
        clearButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            nameEditText.text.clear()
            ageEditText.text.clear()
            dataTextView.text = "No data saved"
            Toast.makeText(this, "Data cleared!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadSavedData() {
        val name = sharedPreferences.getString("user_name", "")
        val age = sharedPreferences.getInt("user_age", 0)
        val lastSaved = sharedPreferences.getLong("last_saved", 0)
        
        val dataTextView = findViewById<TextView>(R.id.dataTextView)
        
        if (name.isNotEmpty() && age > 0) {
            val lastSavedDate = if (lastSaved > 0) {
                SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                    .format(Date(lastSaved))
            } else {
                "Unknown"
            }
            
            dataTextView.text = """
                Saved Data:
                Name: $name
                Age: $age
                Last Saved: $lastSavedDate
            """.trimIndent()
        } else {
            dataTextView.text = "No data saved"
        }
    }
}
```

## 🎯 Challenge Exercise: Mini Calculator

### Objective
Create a simple calculator app that demonstrates multiple concepts.

### Requirements
1. **Create a calculator interface** with number buttons and operations
2. **Implement basic arithmetic** (add, subtract, multiply, divide)
3. **Handle edge cases** (division by zero, decimal numbers)
4. **Add a history feature** to show previous calculations
5. **Use proper error handling** and user feedback

### Advanced Features
- **Scientific calculator** functions (square root, power, etc.)
- **Memory functions** (M+, M-, MR, MC)
- **Theme switching** (light/dark mode)
- **Landscape layout** with additional functions

## 📊 Assessment Rubric

### Beginner Level (1-3 points)
- ✅ Basic functionality works
- ✅ Code compiles without errors
- ✅ Simple UI implementation

### Intermediate Level (4-6 points)
- ✅ All requirements implemented
- ✅ Good code organization
- ✅ Proper error handling
- ✅ User-friendly interface

### Advanced Level (7-10 points)
- ✅ All requirements + bonus features
- ✅ Excellent code quality
- ✅ Comprehensive error handling
- ✅ Professional UI/UX
- ✅ Performance optimization
- ✅ Documentation and comments

## 🚀 Submission Guidelines

1. **Create a new branch** for each exercise
2. **Follow Git best practices** with meaningful commits
3. **Include README files** explaining your implementation
4. **Test thoroughly** on different screen sizes
5. **Document any issues** or challenges encountered

## 📚 Additional Resources

- [Android UI Components](https://developer.android.com/guide/topics/ui)
- [SharedPreferences Guide](https://developer.android.com/training/data-storage/shared-preferences)
- [Material Design Components](https://material.io/develop/android)
- [Android Input Events](https://developer.android.com/guide/topics/ui/ui-events)

---

**Next**: Continue with Module 3 - UI Fundamentals and Layouts to learn more advanced UI concepts!
