package com.klu.grocerygenie

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductApiService {
    @POST("/api/products")
    suspend fun addProduct(@Body product: Product): Response<Product>

    @GET("/api/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("/api/products/search")
    suspend fun searchProducts(@Query("query") query: String): List<Product>
}
