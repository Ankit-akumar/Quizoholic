package com.example.quizoholic.models

class Question(
    val question: String,
    val difficulty: String,
    val correct_answer: String,
    val optionsList: ArrayList<String>
)