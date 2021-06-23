package com.example.quizoholic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quizoholic.databinding.ActivityResultBinding
import com.example.quizoholic.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var correctResponses = 0
    private var incorrectResponses = 0
    private var noResponse = 0
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

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

        mDatabase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        binding.btnPlayAgain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val currentUser = mAuth.currentUser
        // to avoid recursion
        var isDone = false

        mDatabase.child("Users").child(currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)

                    if (!isDone) {
                        // increment quizzes
                        mDatabase.child("Users").child(currentUser.uid)
                            .child("totalQuizzes").setValue((user!!.totalQuizzes + 1))

                        // update total score
                        mDatabase.child("Users").child(currentUser.uid)
                            .child("totalScore").setValue(user.totalScore + correctResponses)

                        // update best score if current score is better
                        if (user.bestScore < correctResponses) {
                            mDatabase.child("Users").child(currentUser.uid).child("bestScore")
                                .setValue(correctResponses)
                        }

                        isDone = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("LoadUserError", "loadUser:onCancelled", error.toException())
                }
            })
    }
}