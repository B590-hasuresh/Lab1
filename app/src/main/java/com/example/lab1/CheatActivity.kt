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

/** Key for storing whether the answer has been shown */
private const val ANSWER_SHOWN_KEY = "ANSWER_SHOWN_KEY"

/** Key for the question index extra in intents */
const val EXTRA_QUESTION_INDEX = "com.example.lab1.question_index"

/**
 * ViewModel for the CheatActivity that maintains the state of whether
 * the answer has been shown across configuration changes
 *
 * @property savedStateHandle Handle for saving and retrieving activity state
 */
class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    /**
     * Property that tracks whether the answer has been shown to the user
     * Getter retrieves from savedStateHandle, defaulting to false if not set
     * Setter stores the value in savedStateHandle
     */
    var answerShown: Boolean
        get() = savedStateHandle.get(ANSWER_SHOWN_KEY) ?: false
        set(value) = savedStateHandle.set(ANSWER_SHOWN_KEY, value)
}

/** Key for the answer truth value extra in intents */
const val EXTRA_ANSWER_IS_TRUE = "answer_is_true"

/** Key for the answer shown state extra in intents */
const val EXTRA_ANSWER_SHOWN = "answer_shown"

/**
 * Activity that shows the answer to a quiz question when the user chooses to cheat
 */
class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    private val cheatViewModel: CheatViewModel by viewModels()
    private var questionIndex = 0

    /**
     * Initializes the activity, sets up view binding and click listeners
     * @param savedInstanceState Bundle containing the saved state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        questionIndex = intent.getIntExtra(EXTRA_QUESTION_INDEX, 0)

        if (cheatViewModel.answerShown) {
            showAnswer()
        }

        binding.showAnswerButton.setOnClickListener {
            cheatViewModel.answerShown = true
            showAnswer()
        }
    }

    /**
     * Displays the answer in the UI and marks the question as shown
     */
    private fun showAnswer() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        binding.answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }

    /**
     * Sets the result data to be returned to the calling activity
     * @param isAnswerShown Boolean indicating if the answer was revealed
     */
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_QUESTION_INDEX, questionIndex)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        /**
         * Creates an intent to start CheatActivity
         * @param packageContext Context to create the intent from
         * @param answerIsTrue The correct answer to the question
         * @param questionIndex The index of the current question
         * @return Intent configured to start CheatActivity
         */
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