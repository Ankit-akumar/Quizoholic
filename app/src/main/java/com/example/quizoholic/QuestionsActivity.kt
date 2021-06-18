package com.example.quizoholic

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.quizoholic.databinding.ActivityQuestionsBinding
import com.example.quizoholic.models.Question
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

class QuestionsActivity : AppCompatActivity() {
    private lateinit var selectedQuiz: String
    private val NAME_EXTRA = "Selected Quiz"
    private lateinit var binding: ActivityQuestionsBinding
    private val listOfQuestions = ArrayList<Question>()
    private val URLMap = HashMap<String, String>()

    //********************************************//
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedQuiz = intent.getStringExtra(NAME_EXTRA).toString()

        makeURLMap()
        binding.loadingProgressBar.visibility = View.VISIBLE
        FetchData().start()
    }

    inner class FetchData : Thread() {
        var data = String()

        override fun run() {

            val url: URL
            try {
                url = URL(URLMap[selectedQuiz])
                val httpURLConnection = url.openConnection()
                val inputStream: InputStream = httpURLConnection.getInputStream()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (true) {
                    line = bufferedReader.readLine()
                    if (line == null) break
                    data += line
                }
                if (data.isNotEmpty()) {
                    val jsonObject = JSONObject(data)
                    val questions = jsonObject.getJSONArray("results")
                    for (i in 0 until questions.length()) {
                        val jsonQuestion = questions.getJSONObject(i)
                        val incorrectAnswers = jsonQuestion.getJSONArray("incorrect_answers")
                        val optionsList: ArrayList<String> = ArrayList()
                        for (j in 0..2) {
                            optionsList.add(incorrectAnswers[j].toString())
                        }

                        val question = Question(
                            question = jsonQuestion.getString("question"),
                            difficulty = jsonQuestion.getString("difficulty"),
                            correct_answer = jsonQuestion.getString("correct_answer"),
                            optionsList = optionsList
                        )

                        listOfQuestions.add(question)
                    }
                }
            } catch (ex: MalformedURLException) {
                ex.printStackTrace()
            }
            super.run()
            binding.loadingProgressBar.visibility = View.INVISIBLE
            println(listOfQuestions[currentPosition].question)
            println(listOfQuestions[currentPosition].difficulty)
        }
    }

    private fun makeURLMap() {
        URLMap["Mathematics"] = "https://opentdb.com/api.php?amount=10&category=19&type=multiple"
        URLMap["Science and Nature"] =
            "https://opentdb.com/api.php?amount=10&category=17&type=multiple"
        URLMap["Computers"] = "https://opentdb.com/api.php?amount=10&category=18&type=multiple"
        URLMap["Gadgets"] = "https://opentdb.com/api.php?amount=10&category=30&type=multiple"
        URLMap["History"] = "https://opentdb.com/api.php?amount=10&category=23&type=multiple"
        URLMap["Sports"] = "https://opentdb.com/api.php?amount=10&category=21&type=multiple"
    }

    private fun updateUI() {
        setOptionsDefaultBackground()
        binding.tvQuestion.text = listOfQuestions[currentPosition].question
        binding.tvOption1.text = listOfQuestions[currentPosition].optionsList[0]
        binding.tvOption2.text = listOfQuestions[currentPosition].optionsList[1]
        binding.tvOption3.text = listOfQuestions[currentPosition].optionsList[2]
        binding.tvOption4.text = listOfQuestions[currentPosition].correct_answer
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setOptionsDefaultBackground() {
        binding.tvOption1.background = getDrawable(R.drawable.custom_card_view)
        binding.tvOption2.background = getDrawable(R.drawable.custom_card_view)
        binding.tvOption3.background = getDrawable(R.drawable.custom_card_view)
        binding.tvOption4.background = getDrawable(R.drawable.custom_card_view)
    }
}