package com.klu.grocerygenie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.klu.grocerygenie.CartManager.cartItems

class CartAdapter(
    private val items: MutableList<Product>,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)
        val nameTextView: TextView = itemView.findViewById(R.id.productTitle)
        val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        // Add more views if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.nameTextView.text = item.title
        holder.priceTextView.text = "₹${item.cost}"
        Glide.with(holder.itemView.context)
            .load(item.imageUrl) // your image URL string
            .into(holder.productImage)
        // Make sure imageResId exists in Product class

        val resId = holder.itemView.context.resources.getIdentifier(item.imageUrl, "drawable", holder.itemView.context.packageName)
        holder.productImage.setImageResource(resId)

        holder.deleteIcon.setOnClickListener {
            onDeleteClick(item)  // Notify CartActivity
        }

    }



    override fun getItemCount(): Int {
        return cartItems.size
    }
}
