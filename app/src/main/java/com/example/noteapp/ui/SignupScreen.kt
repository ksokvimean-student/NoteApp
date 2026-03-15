package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivitySignupBinding
import com.example.noteapp.model.User
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class SignupScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val email = binding.userEmail.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()
            val confirmPass = binding.userConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPass) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val gson = Gson()

                // 1. Get the current list from SharedPreferences
                val json = sharedPref.getString("user_list", null)
                val type = object : TypeToken<MutableList<User>>() {}.type
                val userList: MutableList<User> = if (json == null) {
                    mutableListOf()
                } else {
                    gson.fromJson(json, type)
                }

                // 2. Check if email already exists
                if (userList.any { it.email == email }) {
                    Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(name, email, password)
                    userList.add(newUser)

                    // 4. Save the updated list back to memory
                    val updatedJson = gson.toJson(userList)
                    sharedPref.edit().putString("user_list", updatedJson).apply()

                    Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()

                    // 5. Navigate to Home
                    val intent = Intent(this, HomePage::class.java)
                    intent.putExtra("USER_NAME", name)
                    intent.putExtra("USER_EMAIL", email)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}