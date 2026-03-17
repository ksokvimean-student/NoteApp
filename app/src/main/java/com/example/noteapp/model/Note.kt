package com.example.noteapp.model

data class Note(
    val id: Int = 0,
    val userId: Int = 0,
    val title: String = "",
    val content: String = "",
    val filePath: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)