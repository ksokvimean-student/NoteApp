package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.adapters.UserAdapter
import com.example.noteapp.databinding.ActivityHomePageBinding
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.models.User

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        var userList = mutableListOf<User>();
        for( i in 1..100){
            userList.add(User(i,"Mean $i"))
        }

        val userAdapter = UserAdapter(userList)

        binding.userRecycleView.layoutManager = LinearLayoutManager(this)
        binding.userRecycleView.adapter = userAdapter




    }
}