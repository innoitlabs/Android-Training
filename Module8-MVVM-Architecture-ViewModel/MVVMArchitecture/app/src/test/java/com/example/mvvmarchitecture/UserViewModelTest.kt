package com.example.mvvmarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mvvmarchitecture.data.model.UiState
import com.example.mvvmarchitecture.data.model.User
import com.example.mvvmarchitecture.data.repository.UserRepository
import com.example.mvvmarchitecture.ui.viewmodel.UserViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    private val repository: UserRepository = mockk()
    private lateinit var viewModel: UserViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(repository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state should be loading`() = runTest {
        // Given
        val expectedState = UiState.Loading
        
        // When
        val actualState = viewModel.uiState.value
        
        // Then
        assertEquals(expectedState, actualState)
    }
    
    @Test
    fun `loadUsers should emit success state when repository succeeds`() = runTest {
        // Given
        val users = listOf(
            User(1, "John Doe", "john@example.com"),
            User(2, "Jane Smith", "jane@example.com")
        )
        coEvery { repository.getUsers() } returns Result.success(users)
        
        // When
        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val finalState = viewModel.uiState.value
        assertTrue(finalState is UiState.Success)
        assertEquals(users, (finalState as UiState.Success).data)
    }
    
    @Test
    fun `loadUsers should emit error state when repository fails`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { repository.getUsers() } returns Result.failure(error)
        
        // When
        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val finalState = viewModel.uiState.value
        assertTrue(finalState is UiState.Error)
        assertEquals("An unexpected error occurred: Network error", (finalState as UiState.Error).message)
    }
}
