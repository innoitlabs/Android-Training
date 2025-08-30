package com.example.dependencyhilt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.dependencyhilt.data.model.User
import com.example.dependencyhilt.domain.repository.UserRepository
import com.example.dependencyhilt.presentation.viewmodel.UserViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

/**
 * Unit tests for UserViewModel.
 * 
 * This test class demonstrates how to test ViewModels with Hilt dependency injection.
 */
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var mockRepository: UserRepository
    
    @Inject
    private lateinit var viewModel: UserViewModel
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadUsers should emit loading then success state`() = runTest {
        // Given
        val testUsers = listOf(
            User(1, "John Doe", "john@example.com", "johndoe"),
            User(2, "Jane Smith", "jane@example.com", "janesmith")
        )
        `when`(mockRepository.getUsers()).thenReturn(testUsers)
        
        val uiStateObserver = Observer<UserViewModel.UiState> {}
        val usersObserver = Observer<List<User>> {}
        
        try {
            viewModel.uiState.observeForever(uiStateObserver)
            viewModel.users.observeForever(usersObserver)
            
            // When
            viewModel.loadUsers()
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Then
            assert(viewModel.uiState.value is UserViewModel.UiState.Success)
            assert(viewModel.users.value == testUsers)
            
        } finally {
            viewModel.uiState.removeObserver(uiStateObserver)
            viewModel.users.removeObserver(usersObserver)
        }
    }
    
    @Test
    fun `loadUsers should emit error state when repository throws exception`() = runTest {
        // Given
        val errorMessage = "Network error"
        `when`(mockRepository.getUsers()).thenThrow(Exception(errorMessage))
        
        val uiStateObserver = Observer<UserViewModel.UiState> {}
        
        try {
            viewModel.uiState.observeForever(uiStateObserver)
            
            // When
            viewModel.loadUsers()
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Then
            val currentState = viewModel.uiState.value
            assert(currentState is UserViewModel.UiState.Error)
            assert((currentState as UserViewModel.UiState.Error).message.contains(errorMessage))
            
        } finally {
            viewModel.uiState.removeObserver(uiStateObserver)
        }
    }
    
    @Test
    fun `getUserById should emit success state with single user`() = runTest {
        // Given
        val testUser = User(1, "John Doe", "john@example.com", "johndoe")
        `when`(mockRepository.getUserById(1)).thenReturn(testUser)
        
        val uiStateObserver = Observer<UserViewModel.UiState> {}
        
        try {
            viewModel.uiState.observeForever(uiStateObserver)
            
            // When
            viewModel.getUserById(1)
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Then
            val currentState = viewModel.uiState.value
            assert(currentState is UserViewModel.UiState.Success)
            assert((currentState as UserViewModel.UiState.Success).users.size == 1)
            assert(currentState.users[0] == testUser)
            
        } finally {
            viewModel.uiState.removeObserver(uiStateObserver)
        }
    }
}
