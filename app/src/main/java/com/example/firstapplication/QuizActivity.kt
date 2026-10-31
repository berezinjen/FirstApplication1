package com.example.firstapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestionCount: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var btnOptions: List<Button>

    private val questionList = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvQuestionCount = findViewById(R.id.tvQuestionCount)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        btnOptions = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )

        loadQuestionsFromAssets()
        showNextQuestion()

        btnOptions.forEachIndexed { index, button ->
            button.setOnClickListener {
                checkAnswer(index)
            }
        }
    }

    private fun loadQuestionsFromAssets() {
        try {
            val reader = BufferedReader(InputStreamReader(assets.open("questions.txt")))
            reader.useLines { lines ->
                lines.forEach { line ->
                    val parts = line.split("|")
                    if (parts.size == 6) {
                        val question = Question(
                            parts[0],
                            listOf(parts[1], parts[2], parts[3], parts[4]),
                            parts[5].toInt()
                        )
                        questionList.add(question)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex < questionList.size) {
            val currentQuestion = questionList[currentQuestionIndex]
            tvQuestionCount.text = "Вопрос ${currentQuestionIndex + 1} из ${questionList.size}"
            tvQuestionText.text = currentQuestion.questionText

            btnOptions.forEachIndexed { index, button ->
                button.text = currentQuestion.options[index]
            }
        } else {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("SCORE", score)
            intent.putExtra("TOTAL", questionList.size)
            startActivity(intent)
            finish()
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questionList[currentQuestionIndex]
        if (selectedIndex == currentQuestion.correctIndex) {
            score++
        }
        currentQuestionIndex++
        showNextQuestion()
    }
}