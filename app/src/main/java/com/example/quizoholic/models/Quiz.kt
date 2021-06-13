package com.example.quizoholic.models

class Quiz(
    val quizName: String = "Title",
) {
    fun createQuizList(): ArrayList<Quiz> {
        val listOfQuizNames =
            arrayOf(
                "Mathematics",
                "Science and Nature",
                "Computers",
                "Gadgets",
                "History",
                "Sports"
            )
        val list: ArrayList<Quiz> = ArrayList()
        listOfQuizNames.forEach { list.add(Quiz(quizName = it)) }
        return list
    }
}