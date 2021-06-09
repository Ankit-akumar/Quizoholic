package com.example.quizoholic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.quizoholic.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.tvAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignup.setOnClickListener { view ->
            if (isDataValid(view)) {
                binding.progressBar.visibility = View.VISIBLE

                mAuth.createUserWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener { task ->
                    binding.progressBar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Snackbar.make(
                            view,
                            task.exception!!.localizedMessage!!.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun isDataValid(view: View): Boolean {
        if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
            Snackbar.make(
                view,
                getString(R.string.password_confirm_password_not_same),
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }
}