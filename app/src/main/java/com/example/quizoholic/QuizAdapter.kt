package com.example.quizoholic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.quizoholic.models.Quiz

class QuizAdapter(private val quizList: ArrayList<Quiz>) :
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
        val button = holder.button
        button.text = quizList[position].quizName
        button.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return quizList.size
    }
}