package com.example.noteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityLoginBinding

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val inputEmail = binding.etEmail.text.toString().trim()
            val inputPassword = binding.etPassword.text.toString().trim()

            // 1. Access the "Cache" (SharedPreferences)
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPref.getString("saved_email", null)
            val savedPassword = sharedPref.getString("saved_password", null)
            val savedName = sharedPref.getString("saved_name", "User")

            // 2. Compare input with cached data
            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else if (inputEmail == savedEmail && inputPassword == savedPassword) {
                // Success!
                val intent = Intent(this, HomePage::class.java)
                // Pass the saved name so the Home Screen knows who logged in
                intent.putExtra("USER_NAME", savedName)
                intent.putExtra("USER_EMAIL", savedEmail)

                startActivity(intent)
                finish()
            }
            else {
                binding.etPassword.error = "Invalid email or password"
            }
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
        }
    }
}