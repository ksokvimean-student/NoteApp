package com.example.noteapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.R
import com.example.noteapp.data.database.SettingsManager
import com.example.noteapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsManager = SettingsManager(this)
        // In SettingsActivity.kt
        binding.bottomNavLayout.bottomNavigation.setupNavigation(this, R.id.nav_settings)

        loadSettings()
        setupToolbar()
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavLayout.bottomNavigation.selectedItemId = R.id.nav_settings
    }

    private fun loadSettings() {
        binding.switchAutoSave.isChecked = settingsManager.isAutoSaveEnabled
        binding.switchConfirmDelete.isChecked = settingsManager.isConfirmDeleteEnabled

        binding.switchAutoSave.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.isAutoSaveEnabled = isChecked
        }

        binding.switchConfirmDelete.setOnCheckedChangeListener { _, isChecked ->
            settingsManager.isConfirmDeleteEnabled = isChecked
        }
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}
