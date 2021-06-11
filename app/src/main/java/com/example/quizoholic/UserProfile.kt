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
        this.database = FirebaseDatabase.getInstance().reference

        val firebaseUser = mAuth.currentUser

        val user = User(
            username = firebaseUser!!.displayName.toString(),
            bestScore = database.child("Users").child(firebaseUser.uid)
                .child("bestScore").toString(),
            totalScore = this.database.child("Users").child(firebaseUser.uid)
                .child("totalScore").toString(),
            profilePic = firebaseUser.photoUrl.toString()
        )

        binding.tvUsername.text = user.username
        Picasso.get().load(user.profilePic)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(binding.profileImage)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get User object and use the values to update the UI
                val userT: User? = dataSnapshot.getValue(User::class.java)
                binding.tvBestScore.text = userT!!.bestScore
                binding.tvTotalScore.text = userT.totalScore
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
    }
}
