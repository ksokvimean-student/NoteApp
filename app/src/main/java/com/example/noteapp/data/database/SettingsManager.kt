package com.example.noteapp.data.database

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "settings_prefs"
        private const val KEY_AUTO_SAVE = "auto_save"
        private const val KEY_CONFIRM_DELETE = "confirm_delete"
        private const val KEY_STORAGE_FOLDER = "storage_folder"

        private const val DEFAULT_AUTO_SAVE = true
        private const val DEFAULT_CONFIRM_DELETE = true
        private const val DEFAULT_STORAGE_FOLDER = "notes"
    }

    var isAutoSaveEnabled: Boolean
        get() = sharedPref.getBoolean(KEY_AUTO_SAVE, DEFAULT_AUTO_SAVE)
        set(value) = sharedPref.edit().putBoolean(KEY_AUTO_SAVE, value).apply()

    var isConfirmDeleteEnabled: Boolean
        get() = sharedPref.getBoolean(KEY_CONFIRM_DELETE, DEFAULT_CONFIRM_DELETE)
        set(value) = sharedPref.edit().putBoolean(KEY_CONFIRM_DELETE, value).apply()

    var storageFolder: String
        get() = sharedPref.getString(KEY_STORAGE_FOLDER, DEFAULT_STORAGE_FOLDER) ?: DEFAULT_STORAGE_FOLDER
        set(value) = sharedPref.edit().putString(KEY_STORAGE_FOLDER, value).apply()
}
