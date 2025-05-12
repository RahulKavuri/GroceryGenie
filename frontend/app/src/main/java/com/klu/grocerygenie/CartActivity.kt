package com.klu.grocerygenie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRecyclerView)
        totalTextView = findViewById(R.id.totalTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val cartItems = CartManager.cartItems.toMutableList()

        adapter = CartAdapter(cartItems) { product ->
            cartItems.remove(product)
            CartManager.cartItems.remove(product)
            adapter.notifyDataSetChanged()
            updateTotal(cartItems)
        }

        recyclerView.adapter = adapter
        updateTotal(cartItems)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish() // closes current activity and goes back
        }

        findViewById<Button>(R.id.next).setOnClickListener {
            val total = cartItems.sumOf { it.cost.toDouble() }
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("TOTAL_AMOUNT", total)
            startActivity(intent)
        }

    }



    private fun updateTotal(items: List<Product>) {
        val total = items.sumOf { it.cost.toDouble() }
        totalTextView.text = "TOTAL     ₹$total"
    }




}
