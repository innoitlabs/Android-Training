# Jetpack Compose Basics

## Overview

Jetpack Compose is Android's modern toolkit for building native UI. It simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs.

## Why Jetpack Compose?

### Problems with Traditional Views
- ❌ **Verbose XML**: Lots of boilerplate code
- ❌ **State Management**: Manual state synchronization
- ❌ **Performance**: Complex view hierarchies
- ❌ **Testing**: Difficult to test UI logic
- ❌ **Reusability**: Hard to create reusable components

### Benefits of Compose
- ✅ **Declarative**: Describe UI as a function of state
- ✅ **Less Code**: Concise and readable
- ✅ **Reactive**: Automatic UI updates
- ✅ **Composable**: Reusable UI components
- ✅ **Kotlin-First**: Native Kotlin APIs
- ✅ **Better Testing**: Easier to test UI logic

## Basic Concepts

### 1. Composable Functions

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

### 2. State Management

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { count++ }) {
                Text("Increment")
            }
            
            Button(onClick = { count-- }) {
                Text("Decrement")
            }
        }
    }
}
```

### 3. State Hoisting

```kotlin
@Composable
fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit,
    onRefresh: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    
    Column {
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        LazyColumn {
            items(users) { user ->
                UserCard(
                    user = user,
                    onClick = { onUserClick(user) }
                )
            }
        }
        
        Button(
            onClick = {
                isLoading = true
                onRefresh()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Refresh")
        }
    }
}
```

## Layout Components

### 1. Basic Layouts

```kotlin
@Composable
fun LayoutExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Row with items
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Left")
            Text("Right")
        }
        
        // Box with overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = "Centered Text",
                modifier = Modifier.align(Alignment.Center)
            )
            
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }
        
        // LazyColumn for lists
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(10) { index ->
                Text("Item $index")
            }
        }
    }
}
```

### 2. Custom Layouts

```kotlin
@Composable
fun CustomLayout() {
    Layout(
        content = {
            Text("First")
            Text("Second")
            Text("Third")
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        
        val maxHeight = placeables.maxOf { it.height }
        val totalWidth = placeables.sumOf { it.width }
        
        layout(totalWidth, maxHeight) { offset ->
            var xPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(xPosition, 0)
                xPosition += placeable.width
            }
        }
    }
}
```

## Material Design 3

### 1. Material Components

```kotlin
@Composable
fun MaterialComponentsExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Buttons
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Primary Button")
        }
        
        OutlinedButton(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Outlined Button")
        }
        
        TextButton(
            onClick = { /* Handle click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Text Button")
        }
        
        // Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Card Title",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Card content goes here",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Text Fields
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

### 2. Theming

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)
```

## State Management with ViewModel

### 1. ViewModel Integration

```kotlin
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            getUserListUseCase()
                .onSuccess { users ->
                    _uiState.value = UserListUiState.Success(users)
                }
                .onFailure { error ->
                    _uiState.value = UserListUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
    
    fun retry() {
        loadUsers()
    }
}

sealed class UserListUiState {
    object Loading : UserListUiState()
    data class Success(val users: List<User>) : UserListUiState()
    data class Error(val message: String) : UserListUiState()
}
```

### 2. UI with ViewModel

```kotlin
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    UserListContent(
        uiState = uiState,
        onRetry = { viewModel.retry() }
    )
}

@Composable
private fun UserListContent(
    uiState: UserListUiState,
    onRetry: () -> Unit
) {
    when (uiState) {
        is UserListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is UserListUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.users) { user ->
                    UserCard(user = user)
                }
            }
        }
        
        is UserListUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${uiState.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
```

## Navigation with Compose

### 1. Navigation Setup

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onUserClick = { userId ->
                    navController.navigate("user/$userId")
                }
            )
        }
        
        composable(
            route = "user/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserDetailScreen(userId = userId)
        }
    }
}
```

### 2. Navigation with Arguments

```kotlin
@Composable
fun UserDetailScreen(userId: Int) {
    val viewModel: UserDetailViewModel = hiltViewModel()
    
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UserDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        is UserDetailUiState.Success -> {
            UserDetailContent(user = uiState.user)
        }
        
        is UserDetailUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetry = { viewModel.loadUser(userId) }
            )
        }
    }
}
```

## Testing Compose

### 1. Unit Testing

```kotlin
@Test
fun testUserCard() {
    val user = User(1, "John Doe", "john@example.com")
    
    composeTestRule.setContent {
        MaterialTheme {
            UserCard(user = user)
        }
    }
    
    composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    composeTestRule.onNodeWithText("john@example.com").assertIsDisplayed()
}
```

### 2. Integration Testing

```kotlin
@HiltAndroidTest
class UserListScreenTest {
    
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun testUserListLoading() {
        composeTestRule.setContent {
            MaterialTheme {
                UserListScreen()
            }
        }
        
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }
}
```

## Best Practices

1. **Composable Functions**: Keep them pure and stateless
2. **State Hoisting**: Lift state up to the nearest common ancestor
3. **Remember**: Use remember for expensive computations
4. **LaunchedEffect**: Use for side effects
5. **Modifiers**: Chain them for better readability
6. **Testing**: Test composables in isolation
7. **Performance**: Use LazyColumn for large lists
8. **Accessibility**: Add content descriptions

## Summary

Jetpack Compose provides:

- ✅ **Declarative UI** development
- ✅ **Less boilerplate** code
- ✅ **Better state management**
- ✅ **Reusable components**
- ✅ **Modern Material Design**
- ✅ **Easier testing**

In the next section, we'll explore Advanced Kotlin Features & Patterns for better code organization.
