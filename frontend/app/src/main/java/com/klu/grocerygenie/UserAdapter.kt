package com.klu.grocerygenie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klu.grocerygenie.R
import com.klu.grocerygenie.User

class UserAdapter(
    private val userList: MutableList<User>,
    private val listener: OnUserActionListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnUserActionListener {
        fun onUpdate(user: User)
        fun onDelete(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUsername.text = user.username
        holder.txtEmail.text = user.email

        holder.btnUpdate.setOnClickListener { listener.onUpdate(user) }
        holder.btnDelete.setOnClickListener { listener.onDelete(user) }
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtUsername: TextView = itemView.findViewById(R.id.txtUsername)
        val txtEmail: TextView = itemView.findViewById(R.id.txtEmail)
        val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}
