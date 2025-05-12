package com.klu.grocerygenie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.klu.grocerygenie.Product
import com.klu.grocerygenie.R

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val product = intent.getSerializableExtra("product") as? Product


        product?.let {
            val imageView = findViewById<ImageView>(R.id.productImage)
            Glide.with(this)
                .load(it.imageUrl)
                .into(imageView)
            findViewById<TextView>(R.id.productTitle).text = it.title
            findViewById<TextView>(R.id.productQuantity).text = "Quantity: ${it.quantity}"
            findViewById<TextView>(R.id.productPrice).text = "Price: ₹${it.cost}"
        }
        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        addToCartButton.setOnClickListener {
            product?.let {
                CartManager.cartItems.add(it)
                Toast.makeText(this, "${it.title} added to cart", Toast.LENGTH_SHORT).show()
            }
        }

        val cartButton = findViewById<LinearLayout>(R.id.gotocart)
        cartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }




        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish() // closes current activity and goes back
        }

    }
}
