package com.example.lab1
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.lab1.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity as AppCompatActivity1
private const val REQUEST_CODE_CHEAT = 0
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity1() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Did the user see the answer?
            val cheated = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (cheated) {
                val cheatedIndex = result.data?.getIntExtra(EXTRA_QUESTION_INDEX, -1) ?: -1
                if (cheatedIndex >= 0) {
                    quizViewModel.setQuestionCheated(cheatedIndex)
                }
            }
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val index = quizViewModel.getCurrentIndex()
            val intent = CheatActivity.newIntent(this, answerIsTrue, index)
            cheatLauncher.launch(intent)
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
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val currentIndex = quizViewModel.getCurrentIndex()

        val messageResId = when {
            quizViewModel.isQuestionCheated(currentIndex) ->
                R.string.judgment_toast  // Only show judgment if user cheated on THIS question
            userAnswer == correctAnswer ->
                R.string.correct_toast
            else ->
                R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }


}
