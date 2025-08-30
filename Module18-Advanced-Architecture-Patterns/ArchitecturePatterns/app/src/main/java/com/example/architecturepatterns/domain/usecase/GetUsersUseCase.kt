package com.example.architecturepatterns.domain.usecase

import com.example.architecturepatterns.domain.model.Result
import com.example.architecturepatterns.domain.model.User
import com.example.architecturepatterns.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use case for retrieving all users.
 * 
 * This use case encapsulates the business logic for fetching all users.
 * It delegates to the repository for data access.
 * 
 * @param userRepository The repository for user data operations
 */
class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    /**
     * Retrieves all users from the data source.
     * 
     * @return A [Result] containing a list of [User] objects
     */
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}
