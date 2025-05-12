package com.klu.grocerygenie

import java.io.Serializable

data class Product(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val quantity: String,
    val cost: String

): Serializable

