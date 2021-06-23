package com.example.quizoholic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quizoholic.databinding.ActivityUserProfileBinding
import com.example.quizoholic.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        database.child("Users").child(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Get User object and use the values to update the UI
                    val user = snapshot.getValue(User::class.java)

                    binding.tvUsername.text = user!!.username
                    Picasso.get().load(user.profilePic)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(binding.profileImage)
                    binding.tvBestScore.text = user.bestScore.toString()
                    binding.tvTotalScore.text = user.totalScore.toString()
                    binding.tvTotalQuizzes.text = user.totalQuizzes.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("UserProfile", "loadPost:onCancelled", databaseError.toException())
                }
            })
    }
}
