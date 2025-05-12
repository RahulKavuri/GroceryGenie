package com.klu.grocerygenie

import ProductAdapter
import android.view.inputmethod.EditorInfo
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        // Search functionality
        val searchView: EditText = findViewById(R.id.searchView)

        // Listen for search query submission
        searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchView.text.toString().trim()
                if (query.isNotEmpty()) {
                    val intent = Intent(this@HomeActivity, SearchResultsActivity::class.java)
                    intent.putExtra("SEARCH_QUERY", query)
                    startActivity(intent)
                }
                true  // Consumes the action
            } else {
                false // Don't consume other actions
            }
        }

        // Recently Viewed Products
        val recyclerView1 = findViewById<RecyclerView>(R.id.recyclerRecentlyViewed)
        val productList1 = ProductStorage.getRecentlyViewed(this)
        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView1.adapter = ProductAdapter(productList1) { product ->
            ProductStorage.saveRecentlyViewed(this, product)
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }

        // Categories
        val recyclerView3 = findViewById<RecyclerView>(R.id.category)
        val categoryList = listOf(
            Category(R.drawable.rice, "Fruits"),
            Category(R.drawable.vegetables, "Vegetables"),
            Category(R.drawable.dairy, "Dairy"),
            Category(R.drawable.cookingoil, "Oils and Ghees"),
            Category(R.drawable.masala, "Masalas"),
            Category(R.drawable.cookies, "Biscuits and Cakes"),
            Category(R.drawable.app, "See more")
        )
        recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.adapter = CategoryAdapter(categoryList) { category ->
            if (category.title == "See more") {
                startActivity(Intent(this, CategoryActivity::class.java))
            } else {
                // Optional: Handle other category clicks
            }
        }

        // Top Discounts Products
        val recyclerView2 = findViewById<RecyclerView>(R.id.recyclerTopDiscounts)
        val productList2 = listOf(
            Product(1, "https://res.cloudinary.com/dvchshflj/image/upload/v1744682019/815V9EawbJL._SL1500_rfjib2.png", "Milk packet", "1", "28"),
            Product(1, "https://res.cloudinary.com/dvchshflj/image/upload/v1744682227/OIP_1_luu0t9.jpg", "Britania Bread", "500gm", "50")
        )
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = ProductAdapter(productList2) { product ->
            ProductStorage.saveRecentlyViewed(this, product)
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }

        // Bottom Navigation Setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.nav_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_products -> {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, UserListActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
