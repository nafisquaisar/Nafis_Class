package com.nafis.organizerclasses.model

data class QuizResult(
    val answerStatusMap: Map<String, String> = emptyMap(), // Changed Int to String
    val total: Int = 0,
    val percentage: Int = 0,
    val className: String = "",
    val subjectName: String = "",
    val chapter: String = "",
    val testId: String = "",
    val testTitle: String = "",
    val courseId: String? = null,
    val correctAnswers: Map<String, Int> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
)
