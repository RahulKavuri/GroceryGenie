package com.klu.grocerygenie

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIservice {
    @POST("api/users")
    fun registerUser(@Body users: User): Call<User>

    @GET("api/users")
    fun getUsers(): Call<List<User>>

    @DELETE("api/users/{email}")
    fun deleteUserByEmail(@Path("email") email: String): Call<Void>

    @PUT("api/users/{email}")
    fun updateUser(@Path("email") id: String, @Body user: User): Call<Void>

    @POST("api/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>



}