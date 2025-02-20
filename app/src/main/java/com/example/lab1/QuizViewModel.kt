package com.example.lab1

import Question
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
private const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
private const val CHEATED_ARRAY_KEY = "CHEATED_ARRAY_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // The question bank
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    // Keep track of the current index in SavedStateHandle
    private var _currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    // Keep track of cheated questions in SavedStateHandle as a BooleanArray
    // If there's nothing saved yet, default to a new array sized to questionBank.size
    private var cheatedArray: BooleanArray
        get() = savedStateHandle.get<BooleanArray>(CHEATED_ARRAY_KEY)
            ?: BooleanArray(questionBank.size) { false }
        set(value) = savedStateHandle.set(CHEATED_ARRAY_KEY, value)

    // Public read-only access to the current question’s data
    val currentQuestionAnswer: Boolean
        get() = questionBank[_currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[_currentIndex].textResId

    // Moves to the next question (wrap around with modulo)
    fun moveToNext() {
        _currentIndex = (_currentIndex + 1) % questionBank.size
    }

    // Expose current index to other classes if needed
    fun getCurrentIndex(): Int = _currentIndex

    // Mark a question as cheated
    fun setQuestionCheated(index: Int) {
        val updatedArray = cheatedArray.clone()    // or cheatedArray.copyOf()
        updatedArray[index] = true
        cheatedArray = updatedArray
    }

    // Check if a particular question was cheated on
    fun isQuestionCheated(index: Int): Boolean {
        return cheatedArray.getOrNull(index) ?: false
    }
}
