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
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var confirmPassword: String
    private val patternUsername = "^[a-zA-Z0-9_]+\$"
    private val patternEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

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
            // initialize all private data variables
            username = binding.etUsername.text.toString()
            password = binding.etPassword.text.toString()
            email = binding.etEmail.text.toString()
            confirmPassword = binding.etConfirmPassword.text.toString()

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
        val errorMsg: String = when {
            (username.length < 3 || username.length > 30) -> getString(R.string.username_error_invalid_length)
            (username.contains(" ")) -> getString(R.string.username_whitespaces_not_allowed)
            (!username.matches(patternUsername.toRegex())) -> getString(R.string.username_error_invalid_format)
            (!email.matches(patternEmail.toRegex())) -> getString(R.string.invalid_email)
            (password != confirmPassword) -> getString(R.string.password_confirm_password_not_same)
            (password.length < 6 || password.length > 30) -> getString(R.string.password_error_invalid_length)
            (password.contains(" ")) -> getString(R.string.password_whitespaces_not_allowed)
            (!isPasswordValid()) -> getString(R.string.password_error_invalid_format)
            else -> ""
        }
        if (errorMsg.isNotEmpty()) {
            Snackbar.make(
                view,
                errorMsg,
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun isPasswordValid(): Boolean {
        var hasLetter = false
        var hasDigit = false
        var hasSpecialSymbol = false
        for (c in password) {
            if (hasDigit && hasLetter && hasSpecialSymbol) return true
            if (c in 'a'..'z' || c in 'A'..'Z') hasLetter = true
            else if (c in '0'..'9') hasDigit = true
            else hasSpecialSymbol = true
        }
        if (hasDigit && hasLetter && hasSpecialSymbol) return true
        return false
    }
}