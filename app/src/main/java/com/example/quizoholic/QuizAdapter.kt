package com.example.quizoholic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.quizoholic.models.Quiz

class QuizAdapter(private val quizList: ArrayList<Quiz>, private val context: Context) :
    RecyclerView.Adapter<QuizAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.btnSelectedQuiz)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val quizView = inflater.inflate(R.layout.quiz_row, parent, false)
        return ViewHolder(quizView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentObj = quizList[position]
        val button = holder.button
        button.text = currentObj.quizName
        button.setOnClickListener {
            val intent = Intent(context, QuestionsActivity::class.java)
            intent.putExtra("Selected Quiz", currentObj.quizName)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return quizList.size
    }
}