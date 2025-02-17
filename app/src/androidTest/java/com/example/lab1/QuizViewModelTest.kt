package com.example.lab1

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class QuizViewModelTest {
    @Test
    fun providesExpectedQuestionText() {
        val savedStateHandle = SavedStateHandle()
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertFalse(quizViewModel.currentQuestionText != R.string.question_australia)
    }

    @Test
    fun wrapsAroundQuestionBank() {
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 5))
        val quizViewModel = QuizViewModel(savedStateHandle)
        assertTrue(quizViewModel.currentQuestionText == R.string.question_asia)
        quizViewModel.moveToNext()
        assertTrue(quizViewModel.currentQuestionText == R.string.question_australia)
    }
}
