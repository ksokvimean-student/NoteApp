package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.databinding.ActivityHomeBinding
import com.example.noteapp.databinding.UserProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    // Variables to hold the data coming from Signup
    private var name: String? = ""
    private var email: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Retrieve the data from the Intent
        name = intent.getStringExtra("USER_NAME")
        email = intent.getStringExtra("USER_EMAIL")

        binding.ivUserProfile.setOnClickListener {
            showUserProfileSheet()
        }
    }

    private fun showUserProfileSheet() {
        val dialog = BottomSheetDialog(this)
        val sheetBinding = UserProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)

        // 2. Display the real user data in the Bottom Sheet
        sheetBinding.tvSheetName.text = if (name.isNullOrEmpty()) "User" else name
        sheetBinding.tvSheetEmail.text = if (email.isNullOrEmpty()) "No Email" else email

        sheetBinding.btnLogout.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, LoginScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dialog.show()
    }
}