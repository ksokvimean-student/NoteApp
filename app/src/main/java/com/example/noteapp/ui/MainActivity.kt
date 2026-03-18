package com.example.noteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_NAME", sharedPref.getString("current_user_name", ""))
                putExtra("USER_EMAIL", sharedPref.getString("current_user_email", ""))
                putExtra("USER_ID", sharedPref.getInt("current_user_id", -1))
            }
            startActivity(intent)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}