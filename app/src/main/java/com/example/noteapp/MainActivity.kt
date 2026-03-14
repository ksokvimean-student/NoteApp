package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noteapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnSignIn.setOnClickListener {
            val isLoggedin = onLogin()
            if(isLoggedin){
                startActivity(Intent(this, HomePage::class.java))
                finish()

            }
        }

    }
    private fun onLogin(): Boolean{
        var EMAIL = "admin@gmail.com";
        var PASS = "123";
        var email = binding.inpEmail.text.toString();
        var pass = binding.inpPass.text.toString();
        if(email.isNullOrBlank() || pass.isNullOrBlank()){
            Snackbar.make(
                binding.main,   // root view
                "Email and Password cannot be empty",
                Snackbar.LENGTH_SHORT
            ).show()
            return false;
        }
        if(email != EMAIL || pass != PASS){
            Snackbar.make(
                binding.main,   // root view
                "Credential not found!",
                Snackbar.LENGTH_SHORT
            ).show()
            return false;
        }
        Snackbar.make(
            binding.main,   // root view
            "Welcome Back",
            Snackbar.LENGTH_SHORT
        ).show()
        return true;
    }
}