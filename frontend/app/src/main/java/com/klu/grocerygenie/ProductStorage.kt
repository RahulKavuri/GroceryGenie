package com.klu.grocerygenie

import android.content.Context
import com.google.gson.Gson

object ProductStorage {
    fun saveRecentlyViewed(context: Context, product: Product) {
        val prefs = context.getSharedPreferences("recently_viewed", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val productJson = Gson().toJson(product)
        // Create a new mutable set based on the stored set, so we don't modify the original one
        val current = prefs.getStringSet("list", emptySet())?.toMutableSet() ?: mutableSetOf()

        // Remove if already exists to re-add at top
        current.remove(productJson)
        current.add(productJson)

        if (current.size > 10) {
            val iterator = current.iterator()
            iterator.next() // This will point to the first element
            iterator.remove() // Remove the first element (to maintain size of 10)
        }

        editor.putStringSet("list", current)
        editor.apply()
    }

    fun getRecentlyViewed(context: Context): List<Product> {
        val prefs = context.getSharedPreferences("recently_viewed", Context.MODE_PRIVATE)
        val jsonSet = prefs.getStringSet("list", emptySet()) ?: emptySet()

        return jsonSet.map { Gson().fromJson(it, Product::class.java) }
    }
}
