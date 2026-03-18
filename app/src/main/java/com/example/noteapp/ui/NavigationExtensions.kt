package com.example.noteapp.ui

import android.content.Intent
import com.example.noteapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.setupNavigation(activity: android.app.Activity, currentItemId: Int) {
    this.setOnItemSelectedListener { item ->
        if (item.itemId == currentItemId) return@setOnItemSelectedListener true

        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
                activity.startActivity(intent)
                // Remove the sliding animation
                activity.overridePendingTransition(0, 0)
                true
            }
            R.id.nav_settings -> {
                val intent = Intent(activity, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION
                activity.startActivity(intent)
                activity.overridePendingTransition(0, 0)
                true
            }
            else -> false
        }
    }
}