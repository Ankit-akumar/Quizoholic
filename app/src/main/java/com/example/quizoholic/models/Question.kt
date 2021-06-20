package com.example.quizoholic.models

import org.json.JSONArray

class Question(
    val question: String,
    val difficulty: String,
    val correct_answer: Int,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String
)