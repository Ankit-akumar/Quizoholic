package com.example.quizoholic.models

class User(
    val userid: String = "0",
    val username: String = "username",
    val email: String = "email",
    val password: String = "password",
    val totalScore: Long = 0,
    val bestScore: Long = 0,
    val profilePic: String = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcatking.in%2Fwp-content%2Fuploads%2F2017%2F02%2Fdefault-profile-1.png&f=1&nofb=1",
    val totalQuizzes: Long = 0
) {
    fun User() {}
}