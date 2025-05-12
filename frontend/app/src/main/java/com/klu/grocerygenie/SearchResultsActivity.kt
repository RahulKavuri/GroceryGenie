package com.klu.grocerygenie

import ProductAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var productList: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results2)

        val searchQuery = intent.getStringExtra("SEARCH_QUERY") ?: ""

        val searchBar: EditText = findViewById(R.id.searchView)
        searchBar.setText(searchQuery)

        val resultstext:TextView = findViewById(R.id.resultstext)
        resultstext.setText("Search for products $searchQuery")




        searchResultsRecyclerView = findViewById(R.id.recyclerSearchResults)
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Pass click listener to adapter
        productAdapter = ProductAdapter(productList) { product ->
            ProductStorage.saveRecentlyViewed(this, product)
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }

        searchResultsRecyclerView.adapter = productAdapter

        fetchSearchResultsFromApi(searchQuery)
    }

    private fun fetchSearchResultsFromApi(query: String) {
        lifecycleScope.launch {
            try {
                val products = RetrofitClient.productService.searchProducts(query)
                productAdapter.updateList(products)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
