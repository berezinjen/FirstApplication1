package com.example.firstapplication

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int
)