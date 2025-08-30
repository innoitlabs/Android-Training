package com.example.architecturepatterns.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.architecturepatterns.domain.model.User
import com.example.architecturepatterns.presentation.viewmodel.UserUiState
import com.example.architecturepatterns.presentation.viewmodel.UserViewModel

/**
 * Main screen for displaying user data.
 * 
 * This composable displays a list of users and handles different UI states
 * including loading, success, and error states.
 * 
 * @param viewModel The ViewModel for managing user data
 * @param onUserClick Callback when a user is clicked
 */
@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onUserClick: (User) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val users by viewModel.users.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Users",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Content based on UI state
        when (uiState) {
            is UserUiState.Loading -> {
                LoadingIndicator()
            }
            is UserUiState.Success -> {
                if (users.isEmpty()) {
                    EmptyState()
                } else {
                    UserList(
                        users = users,
                        onUserClick = onUserClick
                    )
                }
            }
            is UserUiState.Error -> {
                ErrorState(
                    message = (uiState as UserUiState.Error).message,
                    onRetry = { viewModel.refresh() }
                )
            }
        }
    }
}

/**
 * Displays a loading indicator.
 */
@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Displays an empty state when no users are available.
 */
@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No users found",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try refreshing to load users",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Displays an error state with retry functionality.
 * 
 * @param message The error message to display
 * @param onRetry Callback when retry is clicked
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

/**
 * Displays a list of users.
 * 
 * @param users The list of users to display
 * @param onUserClick Callback when a user is clicked
 */
@Composable
private fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(
                user = user,
                onClick = { onUserClick(user) }
            )
        }
    }
}
