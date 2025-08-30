package com.example.architecturepatterns.domain.model

/**
 * A sealed class representing the result of an operation.
 * 
 * This class is used throughout the application to handle success and error states
 * in a type-safe manner. It provides utility methods for working with results.
 * 
 * @param T The type of data contained in the success case
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data.
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents a failed operation with an exception.
     */
    data class Error(val exception: Exception) : Result<Nothing>()
    
    /**
     * Represents an operation that is currently loading.
     */
    object Loading : Result<Nothing>()
    
    /**
     * Returns true if this result represents a successful operation.
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Returns true if this result represents a failed operation.
     */
    fun isError(): Boolean = this is Error
    
    /**
     * Returns true if this result represents a loading operation.
     */
    fun isLoading(): Boolean = this is Loading
    
    /**
     * Returns the data if this is a success result, null otherwise.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Returns the data if this is a success result, throws the exception otherwise.
     * 
     * @throws Exception if this is an error result
     * @throws IllegalStateException if this is a loading result
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Result is still loading")
    }
    
    /**
     * Executes the given function if this is a success result.
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }
    
    /**
     * Executes the given function if this is an error result.
     */
    inline fun onError(action: (Exception) -> Unit): Result<T> {
        if (this is Error) {
            action(exception)
        }
        return this
    }
    
    /**
     * Executes the given function if this is a loading result.
     */
    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) {
            action()
        }
        return this
    }
    
    /**
     * Maps the data if this is a success result.
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(exception)
        is Loading -> Loading
    }
    
    /**
     * Maps the data if this is a success result, or returns the default value.
     */
    inline fun <R> mapOr(defaultValue: R, transform: (T) -> R): R = when (this) {
        is Success -> transform(data)
        else -> defaultValue
    }
}
