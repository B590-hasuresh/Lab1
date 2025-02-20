package com.example.lab1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.lab1.databinding.ActivityCheatBinding

// Constants for the ViewModel
private const val ANSWER_SHOWN_KEY = "ANSWER_SHOWN_KEY"
const val EXTRA_QUESTION_INDEX = "com.example.lab1.question_index"


// ViewModel for CheatActivity
class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var answerShown: Boolean
        get() = savedStateHandle.get(ANSWER_SHOWN_KEY) ?: false
        set(value) = savedStateHandle.set(ANSWER_SHOWN_KEY, value)
}

const val EXTRA_ANSWER_IS_TRUE = "answer_is_true"
const val EXTRA_ANSWER_SHOWN = "answer_shown"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    private val cheatViewModel: CheatViewModel by viewModels()
    private var questionIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        questionIndex = intent.getIntExtra(EXTRA_QUESTION_INDEX, 0)

        // If answer was previously shown, restore the UI state
        if (cheatViewModel.answerShown) {
            showAnswer()
        }

        binding.showAnswerButton.setOnClickListener {
            cheatViewModel.answerShown = true
            showAnswer()
        }
    }




    private fun showAnswer() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        binding.answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_QUESTION_INDEX, questionIndex)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            answerIsTrue: Boolean,
            questionIndex: Int
        ): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_QUESTION_INDEX, questionIndex)
            }
        }
    }

}