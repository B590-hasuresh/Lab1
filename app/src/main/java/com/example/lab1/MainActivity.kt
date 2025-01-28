package com.example.lab1

import Question
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.lab1.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity as AppCompatActivity1

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity1() {
    private lateinit var binding: ActivityMainBinding
    private var answeredQuestions = mutableSetOf<Int>()





    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)

        // Enable or disable buttons based on whether the question has been answered
        val alreadyAnswered = currentIndex in answeredQuestions
        binding.trueButton.isEnabled = !alreadyAnswered
        binding.falseButton.isEnabled = !alreadyAnswered
    }

    // Check user's answer and disable buttons to prevent multiple submissions
    private fun checkAnswer(userAnswer: Boolean) {
        // Prevent re-answering the same question
        if (currentIndex in answeredQuestions) return

        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        // Show toast message
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        // Mark question as answered
        answeredQuestions.add(currentIndex)

        // Disable buttons after answering
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
    }


}
