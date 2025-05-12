package com.klu.grocerygenie

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnUpdate: Button

    private var originalEmail: String? = null
    private var originalPassword: String? = null // Store original password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        editUsername = findViewById(R.id.etUsername)
        editEmail = findViewById(R.id.etEmail)
        editPassword = findViewById(R.id.etPassword)
        btnUpdate = findViewById(R.id.btnUpdate)

        originalEmail = intent.getStringExtra("email")
        originalPassword = intent.getStringExtra("password") // Get password from intent

        editUsername.setText(intent.getStringExtra("username"))
        editEmail.setText(originalEmail)
        editPassword.setText(originalPassword) // Set default password

        btnUpdate.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser() {
        val updatedUsername = editUsername.text.toString()
        val updatedEmail = editEmail.text.toString()
        val updatedPassword = editPassword.text.toString()

        if (originalEmail.isNullOrEmpty() || updatedUsername.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val finalPassword = if (updatedPassword.isEmpty()) originalPassword else updatedPassword

        val updatedUser = User(updatedUsername, updatedEmail, finalPassword!!)

        RetrofitClient.instance.updateUser(originalEmail!!, updatedUser).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateUserActivity, "User updated successfully!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@UpdateUserActivity, "Failed to update user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateUserActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
