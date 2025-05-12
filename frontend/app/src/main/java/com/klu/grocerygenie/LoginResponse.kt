package com.klu.grocerygenie



data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User,
    val token: String
)
