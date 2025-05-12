package com.klu.grocerygenie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<User>()

    private var selectedNavItem: Int = R.id.nav_profile // Default to Profile section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        recyclerView = findViewById(R.id.recyclerViewUsers)
        setupRecyclerView()
        fetchUsers()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        selectedNavItem = intent.getIntExtra("selected_nav", R.id.nav_profile)
        bottomNavigationView.selectedItemId = selectedNavItem

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navigateTo(HomeActivity::class.java, item.itemId)
                    true
                }
                R.id.nav_products -> {
                    navigateTo(CategoryActivity::class.java, item.itemId)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateTo(activityClass: Class<*>, selectedItem: Int) {
        val intent = Intent(this, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("selected_nav", selectedItem)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun onUpdate(user: User) {
        val intent = Intent(this@UserListActivity, UpdateUserActivity::class.java)
        intent.putExtra("username", user.username)
        intent.putExtra("email", user.email)
        intent.putExtra("password", user.password)
        startActivityForResult(intent, REQUEST_UPDATE_USER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UPDATE_USER && resultCode == Activity.RESULT_OK) {
            fetchUsers()
        }
    }

    companion object {
        const val REQUEST_UPDATE_USER = 100
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList, object : UserAdapter.OnUserActionListener {
            override fun onUpdate(user: User) {
                val intent = Intent(this@UserListActivity, UpdateUserActivity::class.java)
                intent.putExtra("username", user.username)
                intent.putExtra("email", user.email)
                intent.putExtra("password", user.password) // ✅ Pass password
                startActivityForResult(intent, REQUEST_UPDATE_USER)
            }


            override fun onDelete(user: User) {
                deleteUser(user.email)
            }
        })
        recyclerView.adapter = userAdapter
    }

    private fun fetchUsers() {
        RetrofitClient.instance.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userList.clear()
                    userList.addAll(response.body() ?: emptyList())
                    userAdapter.notifyDataSetChanged()
                } else {
                    Log.e("API_ERROR", "Server error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("API_ERROR", "Fetch users failed: ${t.message}", t)
            }
        })
    }

    private fun deleteUser(email: String) {
        RetrofitClient.instance.deleteUserByEmail(email).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UserListActivity, "User deleted!", Toast.LENGTH_SHORT).show()
                    fetchUsers()
                } else {
                    Toast.makeText(this@UserListActivity, "Failed to delete user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UserListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
