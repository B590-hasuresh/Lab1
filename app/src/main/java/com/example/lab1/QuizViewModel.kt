package com.example.lab1

import Question
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/** Tag for logging */
private const val TAG = "QuizViewModel"

/** Key for storing current index in SavedStateHandle */
private const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

/** Key for storing cheated questions array in SavedStateHandle */
private const val CHEATED_ARRAY_KEY = "CHEATED_ARRAY_KEY"

/**
 * ViewModel that manages the quiz state and business logic
 * @property savedStateHandle Handle for saving and retrieving activity state
 */
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    /** List of all questions in the quiz */
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    /**
     * Current index in the question bank
     * Stored in SavedStateHandle to persist across configuration changes
     */
    private var _currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    /**
     * Array tracking which questions have been cheated on
     * Stored in SavedStateHandle to persist across configuration changes
     */
    private var cheatedArray: BooleanArray
        get() = savedStateHandle.get<BooleanArray>(CHEATED_ARRAY_KEY)
            ?: BooleanArray(questionBank.size) { false }
        set(value) = savedStateHandle.set(CHEATED_ARRAY_KEY, value)

    /** The correct answer for the current question */
    val currentQuestionAnswer: Boolean
        get() = questionBank[_currentIndex].answer

    /** The resource ID for the current question's text */
    val currentQuestionText: Int
        get() = questionBank[_currentIndex].textResId

    /**
     * Advances to the next question, wrapping around to the beginning if necessary
     */
    fun moveToNext() {
        _currentIndex = (_currentIndex + 1) % questionBank.size
    }

    /**
     * Gets the current question index
     * @return The current index in the question bank
     */
    fun getCurrentIndex(): Int = _currentIndex

    /**
     * Marks a specific question as cheated
     * @param index The index of the question that was cheated on
     */
    fun setQuestionCheated(index: Int) {
        val updatedArray = cheatedArray.clone()
        updatedArray[index] = true
        cheatedArray = updatedArray
    }

    /**
     * Checks if a specific question was cheated on
     * @param index The index of the question to check
     * @return True if the question was cheated on, false otherwise
     */
    fun isQuestionCheated(index: Int): Boolean {
        return cheatedArray.getOrNull(index) ?: false
    }
}