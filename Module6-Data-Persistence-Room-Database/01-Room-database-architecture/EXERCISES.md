# Room Database Exercises

## Exercise 1: Basic CRUD Operations (Easy)

### Task
Enhance the basic User management app with the following features:

1. **Add email field to User entity**
   - Add an `email` field to the User data class
   - Update the UI to include an email input field
   - Validate that email is not empty before saving

2. **Add validation**
   - Ensure age is positive (greater than 0)
   - Ensure name is not empty
   - Show error messages for invalid input

3. **Display user count**
   - Add a TextView to show the total number of users
   - Update the count whenever users are added or deleted

### Expected Output
```
User Count: 3

ID: 1 | Name: John Doe | Age: 25 | Email: john@example.com
ID: 2 | Name: Jane Smith | Age: 30 | Email: jane@example.com
ID: 3 | Name: Bob Johnson | Age: 28 | Email: bob@example.com
```

### Hints
- Use `@ColumnInfo` annotation for custom column names
- Add validation in the ViewModel or Repository
- Use LiveData to observe user count changes

---

## Exercise 2: Advanced Queries (Intermediate)

### Task
Implement advanced querying capabilities:

1. **Search by name**
   - Add a search input field
   - Implement partial name search (LIKE query)
   - Update results in real-time as user types

2. **Filter by age range**
   - Add min and max age input fields
   - Implement age range filtering
   - Allow clearing filters

3. **Sorting options**
   - Add a spinner/dropdown for sort options
   - Implement sorting by: name (A-Z), age (ascending), ID (descending)
   - Apply sorting to search results

### New DAO Methods Needed
```kotlin
@Query("SELECT * FROM users WHERE name LIKE '%' || :searchQuery || '%'")
fun searchUsersByName(searchQuery: String): Flow<List<User>>

@Query("SELECT * FROM users WHERE age BETWEEN :minAge AND :maxAge")
fun getUsersByAgeRange(minAge: Int, maxAge: Int): Flow<List<User>>

@Query("SELECT * FROM users ORDER BY name ASC")
fun getAllUsersSortedByName(): Flow<List<User>>

@Query("SELECT * FROM users ORDER BY age ASC")
fun getAllUsersSortedByAge(): Flow<List<User>>
```

### Expected Output
```
Search: "john"
Age Range: 20-30
Sort by: Name

Results:
ID: 2 | Name: Jane Smith | Age: 30 | Email: jane@example.com
ID: 1 | Name: John Doe | Age: 25 | Email: john@example.com
```

---

## Exercise 3: Relationships (Advanced)

### Task
Create a Task management system with User-Task relationships:

1. **Create Task Entity**
   ```kotlin
   @Entity(tableName = "tasks")
   data class Task(
       @PrimaryKey(autoGenerate = true) val id: Int = 0,
       val title: String,
       val description: String,
       val isCompleted: Boolean = false,
       val userId: Int  // Foreign key to User
   )
   ```

2. **Implement One-to-Many Relationship**
   - One User can have many Tasks
   - Create a relationship class:
   ```kotlin
   data class UserWithTasks(
       @Embedded val user: User,
       @Relation(
           parentColumn = "id",
           entityColumn = "userId"
       )
       val tasks: List<Task>
   )
   ```

3. **Add Task Management Features**
   - Add tasks to specific users
   - Mark tasks as completed
   - Delete tasks
   - View all tasks for a user

4. **Update UI**
   - Show users with their task counts
   - Allow expanding users to see their tasks
   - Add task creation interface

### New DAO Methods
```kotlin
@Transaction
@Query("SELECT * FROM users")
fun getUsersWithTasks(): Flow<List<UserWithTasks>>

@Query("SELECT * FROM tasks WHERE userId = :userId")
fun getTasksForUser(userId: Int): Flow<List<Task>>

@Insert
suspend fun insertTask(task: Task)

@Update
suspend fun updateTask(task: Task)

@Delete
suspend fun deleteTask(task: Task)
```

### Expected Output
```
Users:
John Doe (2 tasks)
├─ Complete project documentation
└─ Review code changes

Jane Smith (1 task)
└─ Prepare presentation

Bob Johnson (0 tasks)
```

---

## Exercise 4: Database Migration (Advanced)

### Task
Implement database schema changes:

1. **Version 1**: Basic User table (current)
2. **Version 2**: Add phone number field to User
3. **Version 3**: Add created_at timestamp to User

### Migration Strategy
```kotlin
@Database(
    entities = [User::class], 
    version = 3, 
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN phone TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN created_at INTEGER")
            }
        }
    }
}
```

---

## Exercise 5: Testing (Intermediate)

### Task
Write unit tests for your Room database:

1. **Test UserDao**
   ```kotlin
   @RunWith(AndroidJUnit4::class)
   class UserDaoTest {
       private lateinit var database: AppDatabase
       private lateinit var userDao: UserDao

       @Before
       fun createDb() {
           val context = ApplicationProvider.getApplicationContext<Context>()
           database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
               .allowMainThreadQueries()
               .build()
           userDao = database.userDao()
       }

       @After
       fun closeDb() {
           database.close()
       }

       @Test
       fun insertAndReadUser() = runTest {
           val user = User(name = "Test User", age = 25)
           userDao.insertUser(user)
           
           val users = userDao.getAllUsers().first()
           assertThat(users).hasSize(1)
           assertThat(users[0].name).isEqualTo("Test User")
       }
   }
   ```

2. **Test Repository**
   - Test all CRUD operations
   - Test error handling
   - Test data transformations

3. **Test ViewModel**
   - Test LiveData emissions
   - Test coroutine operations
   - Test error scenarios

---

## Exercise 6: Performance Optimization (Advanced)

### Task
Optimize database performance:

1. **Add Indexes**
   ```kotlin
   @Entity(
       tableName = "users",
       indices = [
           Index(value = ["name"]),
           Index(value = ["age"])
       ]
   )
   data class User(
       @PrimaryKey(autoGenerate = true) val id: Int = 0,
       val name: String,
       val age: Int
   )
   ```

2. **Implement Pagination**
   - Use Room with Paging 3 library
   - Load users in pages of 20
   - Implement infinite scrolling

3. **Add Database Callbacks**
   - Log database operations
   - Pre-populate with sample data
   - Handle database corruption

### Paging Implementation
```kotlin
@Query("SELECT * FROM users ORDER BY name ASC")
fun getUsersPaged(): PagingSource<Int, User>

class UserPagingSource(private val userDao: UserDao) : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        // Implementation here
    }
}
```

---

## Submission Guidelines

For each exercise:

1. **Code Quality**
   - Follow Kotlin coding conventions
   - Use meaningful variable names
   - Add appropriate comments

2. **Documentation**
   - Document your approach
   - Explain any design decisions
   - Include screenshots of the UI

3. **Testing**
   - Test all functionality
   - Handle edge cases
   - Ensure no crashes

4. **Bonus Points**
   - Clean, modern UI design
   - Error handling and user feedback
   - Performance optimizations
   - Accessibility features

## Resources

- [Room Testing Guide](https://developer.android.com/training/data-storage/room/testing-db)
- [Paging 3 Documentation](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Database Migrations](https://developer.android.com/training/data-storage/room/migrating-db)
- [Kotlin Coroutines Testing](https://kotlinlang.org/docs/coroutines-test.html)

