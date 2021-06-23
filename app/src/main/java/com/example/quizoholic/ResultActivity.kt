package com.example.quizoholic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizoholic.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var correctResponses = 0
    private var incorrectResponses = 0
    private var noResponse = 0
//    private lateinit var mDatabase: FirebaseDatabase
//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        correctResponses = intent.getIntExtra(QuestionsActivity.CORRECT_RESPONSE, 0)
        noResponse = intent.getIntExtra(QuestionsActivity.NO_RESPONSE, 0)
        incorrectResponses = 10 - (correctResponses + noResponse)

        binding.progressBarResult.progress = correctResponses * 10
        binding.tvCorrect.text = correctResponses.toString()
        binding.tvIncorrect.text = incorrectResponses.toString()
        binding.tvUnattempted.text = noResponse.toString()

//        mDatabase = FirebaseDatabase.getInstance()
//        mAuth = FirebaseAuth.getInstance()
//        setUserScore()

        binding.btnPlayAgain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

//    private fun setUserScore() {
//        val currentUser = mAuth.currentUser
//        val oldScore =
//            mDatabase.reference.child("Users").child(currentUser!!.uid).child("totalScore")
//                .toString()
//        val newScore = (oldScore.toInt() + correctResponses).toString()
//        mDatabase.reference.child("Users").child(currentUser.uid).child("totalScore")
//            .setValue(newScore)
//    }
}