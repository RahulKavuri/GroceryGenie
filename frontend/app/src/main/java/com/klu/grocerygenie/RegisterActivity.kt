package com.klu.grocerygenie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private const val DEBUG = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        edtUsername = findViewById(R.id.etUserName)
        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.etSignUp)

        // Remove any age-related view references
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            registerUser()
        }

        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateEmail()
        }

        edtPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validatePassword()
        }

        // Remove age validation listener
    }

    private fun registerUser() {
        val username = edtUsername.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (!validateInputs(username, email, password)) {
            return
        }

        btnRegister.isEnabled = false
        val user = User(
            username = username,
            email = email,
            password = password
            // Age removed from constructor
        )

        RetrofitClient.instance.registerUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                btnRegister.isEnabled = true
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()  // This was misaligned in your original code
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid registration data"
                        409 -> "Email already exists"
                        else -> "Registration failed. Please try again."
                    }
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                    if (DEBUG) {
                        Log.e("RegisterActivity", "Error code: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                btnRegister.isEnabled = true
                Toast.makeText(this@RegisterActivity, "Network error. Please try again.", Toast.LENGTH_LONG).show()
                if (DEBUG) {
                    Log.e("RegisterActivity", "Network error", t)
                }
            }
        })
    }

    private fun validateInputs(username: String, email: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            edtUsername.error = "Please enter a username"
            isValid = false
        }

        if (!validateEmail()) {
            isValid = false
        }

        if (!validatePassword()) {
            isValid = false
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please agree to Terms and Conditions", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    // Keep existing validateEmail() and validatePassword() methods
    private fun validateEmail(): Boolean {
        val email = edtEmail.text.toString().trim()
        return when {
            email.isEmpty() -> {
                edtEmail.error = "Please enter your email"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                edtEmail.error = "Please enter a valid email"
                false
            }
            else -> true
        }
    }

    private fun validatePassword(): Boolean {
        val password = edtPassword.text.toString().trim()
        return when {
            password.isEmpty() -> {
                edtPassword.error = "Please enter a password"
                false
            }
            password.length < MIN_PASSWORD_LENGTH -> {
                edtPassword.error = "Password must be at least $MIN_PASSWORD_LENGTH characters"
                false
            }
            else -> true
        }
    }
}