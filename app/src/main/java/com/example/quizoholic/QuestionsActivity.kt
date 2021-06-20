package com.example.quizoholic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.quizoholic.databinding.ActivityQuestionsBinding
import com.example.quizoholic.models.Question
import org.json.JSONObject

class QuestionsActivity : AppCompatActivity() {
    private lateinit var selectedQuiz: String
    private lateinit var binding: ActivityQuestionsBinding
    private val listOfQuestions = ArrayList<Question>()
    private lateinit var selectedQuizURL: String
    private val urlMap = mapOf(
        "Mathematics" to "https://opentdb.com/api.php?amount=10&category=19&type=multiple",
        "Science and Nature" to "https://opentdb.com/api.php?amount=10&category=17&type=multiple",
        "Computers" to "https://opentdb.com/api.php?amount=10&category=18&type=multiple",
        "Gadgets" to "https://opentdb.com/api.php?amount=10&category=30&type=multiple",
        "History" to "https://opentdb.com/api.php?amount=10&category=23&type=multiple",
        "Sports" to "https://opentdb.com/api.php?amount=10&category=21&type=multiple"
    )

    companion object {
        const val NAME_EXTRA = "Selected Quiz"
    }

    //********************************************//
    private var currentPosition = 0
    private var selectedOption = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedQuiz = intent.getStringExtra(NAME_EXTRA).toString()

        selectedQuizURL = urlMap[selectedQuiz].toString()

        // prepare a list of all questions from api
        prepareQuizQuestions()

        // show first question
//        updateQuestion()

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
                selectedOption = -1
                updateQuestion()
                setOptionsDefaultBackground()
                binding.btnSubmit.text = getString(R.string.submit)
                binding.progressBar.progress += 10
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

    private fun prepareQuizQuestions() {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, selectedQuizURL, {
//            run { binding.loadingProgressBar.visibility = View.VISIBLE }
            try {
                println("Response is $it")
                val jsonObject = JSONObject(it)
                val jsonArray = jsonObject.getJSONArray("results")
                for (i in 0 until jsonArray.length()) {
                    val currentObject = jsonArray.getJSONObject(i)

                    // shuffle options to set correct option at random position
                    val optionNumbersShuffled = arrayListOf(0, 1, 2, 3)
                    optionNumbersShuffled.shuffle()

                    // stores corresponding options of optionNumbersShuffled list
                    val optionsList = arrayListOf<String>()
                    var correctOptionNumber = 0
                    for (j in 0 until optionNumbersShuffled.size) {
                        if (j == 3) {
                            optionsList.add(currentObject.getString("correct_answer"))
                            correctOptionNumber = j
                        } else optionsList.add(
                            currentObject.getJSONArray("incorrect_answers").get(j).toString()
                        )
                    }

                    // create new question and add to list of questions
                    val question = Question(
                        question = currentObject.getString("question"),
                        difficulty = currentObject.getString("difficulty"),
                        correct_answer = correctOptionNumber,
                        option1 = optionsList[0],
                        option2 = optionsList[1],
                        option3 = optionsList[2],
                        option4 = optionsList[3]
                    )
                    listOfQuestions.add(question)
                }
                println("Length of listOfQuestions is ${listOfQuestions.size}")
//                run{ binding.loadingProgressBar.visibility = View.INVISIBLE }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }, {
            Toast.makeText(
                this@QuestionsActivity,
                getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            ).show()
        })
        requestQueue.add(stringRequest)
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