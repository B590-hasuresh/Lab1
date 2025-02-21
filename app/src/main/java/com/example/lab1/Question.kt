import androidx.annotation.StringRes

/**
 * Data class representing a quiz question
 * @property textResId Resource ID for the question text
 * @property answer The correct answer to the question (true/false)
 */
data class Question(@StringRes val textResId: Int, val answer: Boolean)