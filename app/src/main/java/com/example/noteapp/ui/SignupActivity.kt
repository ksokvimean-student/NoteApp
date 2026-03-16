package com.example.noteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.noteapp.databinding.ActivitySignupBinding
import com.example.noteapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupErrorResetting()

        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                saveUserAndNavigate()
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    /**
     * Handles all the UI validation logic.
     * Returns true if all inputs are valid.
     */
    private fun validateForm(): Boolean {
        val name = binding.userName.text.toString().trim()
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()
        val confirmPass = binding.userConfirmPassword.text.toString().trim()

        var isValid = true

        // Name Check
        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            isValid = false
        }

        // Email Check (Empty and Format)
        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Please enter a valid email address"
            isValid = false
        }

        // Password Check (Empty and Length)
        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        // Confirm Password Check
        if (confirmPass.isEmpty()) {
            binding.tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPass) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun saveUserAndNavigate() {
        val name = binding.userName.text.toString().trim()
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        // Get and check existing users
        val json = sharedPref.getString("user_list", null)
        val type = object : TypeToken<MutableList<User>>() {}.type
        val userList: MutableList<User> = if (json == null) mutableListOf() else gson.fromJson(json, type)

        if (userList.any { it.email.equals(email, ignoreCase = true) }) {
            binding.tilEmail.error = "This email is already registered"
            return
        }

        // Save new user
        userList.add(User(name, email, password))
        sharedPref.edit().apply {
            putString("user_list", gson.toJson(userList))
            putString("saved_email", email)
            putString("saved_name", name)
            apply()
        }

        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, HomePage::class.java).apply {
            putExtra("USER_NAME", name)
            putExtra("USER_EMAIL", email)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun setupErrorResetting() {
        binding.userName.addTextChangedListener { binding.tilName.error = null }
        binding.userEmail.addTextChangedListener { binding.tilEmail.error = null }
        binding.userPassword.addTextChangedListener { binding.tilPassword.error = null }
        binding.userConfirmPassword.addTextChangedListener { binding.tilConfirmPassword.error = null }
    }
}