package com.example.noteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.noteapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reset errors when the user starts typing to provide a smooth UI
        setupErrorResetting()

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val inputEmail = binding.etEmail.text.toString().trim()
        val inputPassword = binding.etPassword.text.toString().trim()

        // Access the "Cache" (SharedPreferences)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("saved_email", null)
        val savedPassword = sharedPref.getString("saved_password", null)
        val savedName = sharedPref.getString("saved_name", "User")

        // Validation Logic
        var isValid = true

        if (inputEmail.isEmpty()) {
            binding.tilEmail.error = "Please enter your email"
            isValid = false
        }

        if (inputPassword.isEmpty()) {
            binding.tilPassword.error = "Please enter your password"
            isValid = false
        }

        if (isValid) {
            if (inputEmail == savedEmail && inputPassword == savedPassword) {
                val intent = Intent(this, HomePage::class.java).apply {
                    putExtra("USER_NAME", savedName)
                    putExtra("USER_EMAIL", savedEmail)
                }
                startActivity(intent)
                finish()
            } else {
                binding.tilEmail.error = "Invalid email or password"
                binding.tilPassword.error = " "
            }
        }
    }

    private fun setupErrorResetting() {
        // This removes the red error labels as soon as the user types
        binding.etEmail.addTextChangedListener {
            binding.tilEmail.error = null
        }
        binding.etPassword.addTextChangedListener {
            binding.tilPassword.error = null
        }
    }
}