package com.example.architecturepatterns.data.mapper

import com.example.architecturepatterns.data.db.entity.UserEntity
import com.example.architecturepatterns.domain.model.User
import java.util.Date
import javax.inject.Inject

/**
 * Mapper for converting between User domain model and UserEntity.
 * 
 * This class handles the conversion between the domain User model and the
 * database UserEntity, ensuring proper data transformation.
 */
class UserMapper @Inject constructor() {
    
    /**
     * Converts a UserEntity to a User domain model.
     * 
     * @param entity The database entity to convert
     * @return The domain model
     */
    fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            avatar = entity.avatar,
            createdAt = Date(entity.createdAt),
            isActive = entity.isActive,
            isVerified = entity.isVerified
        )
    }
    
    /**
     * Converts a User domain model to a UserEntity.
     * 
     * @param domain The domain model to convert
     * @return The database entity
     */
    fun mapToEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            avatar = domain.avatar,
            createdAt = domain.createdAt.time,
            isActive = domain.isActive,
            isVerified = domain.isVerified
        )
    }
    
    /**
     * Converts a list of UserEntity objects to a list of User domain models.
     * 
     * @param entities The list of database entities to convert
     * @return The list of domain models
     */
    fun mapToDomainList(entities: List<UserEntity>): List<User> {
        return entities.map { mapToDomain(it) }
    }
    
    /**
     * Converts a list of User domain models to a list of UserEntity objects.
     * 
     * @param domains The list of domain models to convert
     * @return The list of database entities
     */
    fun mapToEntityList(domains: List<User>): List<UserEntity> {
        return domains.map { mapToEntity(it) }
    }
}
