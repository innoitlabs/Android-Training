package com.example.architecturepatterns.domain.usecase

import com.example.architecturepatterns.domain.model.Result
import com.example.architecturepatterns.domain.model.User
import com.example.architecturepatterns.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use case for retrieving a user by ID.
 * 
 * This use case encapsulates the business logic for fetching a user.
 * It validates the input and delegates to the repository for data access.
 * 
 * @param userRepository The repository for user data operations
 */
class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    /**
     * Retrieves a user by their unique identifier.
     * 
     * @param id The unique identifier of the user to retrieve
     * @return A [Result] containing the [User] or an error
     * 
     * @throws IllegalArgumentException if the provided ID is less than or equal to 0
     */
    suspend operator fun invoke(id: Int): Result<User> {
        return if (id > 0) {
            userRepository.getUser(id)
        } else {
            Result.Error(IllegalArgumentException("User ID must be positive"))
        }
    }
}
