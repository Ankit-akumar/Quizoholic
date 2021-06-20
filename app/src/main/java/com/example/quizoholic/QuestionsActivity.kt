package com.example.quizoholic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quizoholic.databinding.ActivityQuestionsBinding

class QuestionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionsBinding
    private val listOfQuestions = Constants.questionsList

    //********************************************//
    private var currentPosition = 0
    private var selectedOption = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // show first question
        updateQuestion()

        // all on click events of this activity
        binding.btnSubmit.setOnClickListener {
            if (binding.btnSubmit.text == getString(R.string.submit)) { // in submit mode
                setCorrectOptionBackground()
                if (selectedOption != -1 && listOfQuestions[currentPosition].correct_answer != selectedOption) {
                    setInCorrectOptionSelectedBackground()
                }
                binding.btnSubmit.text = getString(R.string.next)
            } else { // if already submitted
                currentPosition++
                if (currentPosition < 10) {
                    selectedOption = -1
                    updateQuestion()
                    setOptionsDefaultBackground()
                    binding.btnSubmit.text = getString(R.string.submit)
                    binding.progressBar.progress += 10
                } else {
                    Constants.questionsList.clear()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }

        binding.tvOption1.setOnClickListener {
            setOptionSelected(binding.tvOption1)
            selectedOption = 0
        }
        binding.tvOption2.setOnClickListener {
            setOptionSelected(binding.tvOption2)
            selectedOption = 1
        }
        binding.tvOption3.setOnClickListener {
            setOptionSelected(binding.tvOption3)
            selectedOption = 2
        }
        binding.tvOption4.setOnClickListener {
            setOptionSelected(binding.tvOption4)
            selectedOption = 3
        }

        binding.tvClearResponse.setOnClickListener {
            setOptionsDefaultBackground()
            selectedOption = -1
        }
    }

    private fun updateQuestion() {
        setOptionsDefaultBackground()
        binding.tvQuestion.text = listOfQuestions[currentPosition].question
        binding.tvOption1.text = listOfQuestions[currentPosition].option1
        binding.tvOption2.text = listOfQuestions[currentPosition].option2
        binding.tvOption3.text = listOfQuestions[currentPosition].option3
        binding.tvOption4.text = listOfQuestions[currentPosition].option4
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setOptionsDefaultBackground() {
        binding.tvOption1.background = getDrawable(R.drawable.custom_card_view)
        binding.tvOption2.background = getDrawable(R.drawable.custom_card_view)
        binding.tvOption3.background = getDrawable(R.drawable.custom_card_view)
        binding.tvOption4.background = getDrawable(R.drawable.custom_card_view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setOptionSelected(view: View) {
        setOptionsDefaultBackground()
        view.background = getDrawable(R.drawable.option_selected_background)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setCorrectOptionBackground() {
        when (listOfQuestions[currentPosition].correct_answer) {
            0 -> binding.tvOption1.background = getDrawable(R.drawable.correct_option_background)
            1 -> binding.tvOption2.background = getDrawable(R.drawable.correct_option_background)
            2 -> binding.tvOption3.background = getDrawable(R.drawable.correct_option_background)
            3 -> binding.tvOption4.background = getDrawable(R.drawable.correct_option_background)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setInCorrectOptionSelectedBackground() {
        when (selectedOption) {
            0 -> binding.tvOption1.background = getDrawable(R.drawable.wrong_option_background)
            1 -> binding.tvOption2.background = getDrawable(R.drawable.wrong_option_background)
            2 -> binding.tvOption3.background = getDrawable(R.drawable.wrong_option_background)
            else -> binding.tvOption4.background = getDrawable(R.drawable.wrong_option_background)
        }
    }
}