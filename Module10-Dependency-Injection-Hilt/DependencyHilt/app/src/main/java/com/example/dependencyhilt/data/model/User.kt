package com.example.dependencyhilt.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a User from the API.
 * 
 * This class is used for JSON serialization/deserialization with Retrofit.
 * The @SerializedName annotations map JSON field names to Kotlin property names.
 */
data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("website")
    val website: String? = null,
    
    @SerializedName("company")
    val company: Company? = null,
    
    @SerializedName("address")
    val address: Address? = null
) {
    /**
     * Data class for company information
     */
    data class Company(
        @SerializedName("name")
        val name: String,
        
        @SerializedName("catchPhrase")
        val catchPhrase: String,
        
        @SerializedName("bs")
        val bs: String
    )
    
    /**
     * Data class for address information
     */
    data class Address(
        @SerializedName("street")
        val street: String,
        
        @SerializedName("suite")
        val suite: String,
        
        @SerializedName("city")
        val city: String,
        
        @SerializedName("zipcode")
        val zipcode: String,
        
        @SerializedName("geo")
        val geo: Geo
    ) {
        /**
         * Data class for geographical coordinates
         */
        data class Geo(
            @SerializedName("lat")
            val lat: String,
            
            @SerializedName("lng")
            val lng: String
        )
    }
}
