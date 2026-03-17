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

    private fun validateForm(): Boolean {
        val name = binding.userName.text.toString().trim()
        val email = binding.userEmail.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()
        val confirmPass = binding.userConfirmPassword.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            isValid = false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Valid email is required"
            isValid = false
        }
        if (password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }
        if (password != confirmPass) {
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

        // Load existing list
        val json = sharedPref.getString("user_list", null)
        val type = object : TypeToken<MutableList<User>>() {}.type
        val userList: MutableList<User> = if (json == null) mutableListOf() else gson.fromJson(json, type)

        // Check if email exists
        if (userList.any { it.email.equals(email, ignoreCase = true) }) {
            binding.tilEmail.error = "This email is already registered"
            return
        }

        // Add and Save
        val userId = System.currentTimeMillis().toInt()
        userList.add(User(userId, name, email, password))
        sharedPref.edit().putString("user_list", gson.toJson(userList)).apply()

        Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, HomePage::class.java).apply {
            putExtra("USER_ID", userId)
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