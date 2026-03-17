package com.example.noteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.noteapp.databinding.ActivityLoginBinding
import com.example.noteapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupErrorResetting()

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val gson = Gson()

        // Get the list of all registered users
        val json = sharedPref.getString("user_list", null)
        val type = object : TypeToken<MutableList<User>>() {}.type
        val userList: MutableList<User> = if (json == null) mutableListOf() else gson.fromJson(json, type)

        if (email.isEmpty()) {
            binding.tilEmail.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = "Enter password"
            return
        }

        // Find the list for a match
        val user = userList.find { it.email.equals(email, ignoreCase = true) && it.password == password }

        if (user != null) {
            val intent = Intent(this, HomePage::class.java).apply {
                putExtra("USER_ID", user.id)
                putExtra("USER_NAME", user.name)
                putExtra("USER_EMAIL", email)
            }
            startActivity(intent)
            finish()
        } else {
            binding.tilEmail.error = "Invalid email or password"
            binding.tilPassword.error = " "
        }
    }

    private fun setupErrorResetting() {
        binding.etEmail.addTextChangedListener { binding.tilEmail.error = null }
        binding.etPassword.addTextChangedListener { binding.tilPassword.error = null }
    }
}