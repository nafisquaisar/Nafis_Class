package com.nafis.organizerclasses.Object

data class TestObject(
    val id: String = "",
    val title: String = "",
    val noOfQues: String="",
    val totalMark: String="",
    val totalTime: String="",
    val classname: String = "",
    val subname: String = "",
    val chapname: String = "",
    var isCompleted: Boolean? = null,
    val questions: List<QuizModel> = listOf()
)

data class QuizModel(
    val quizId: String = "",
    val quizQues: String = "",
    val correctOp: String = "",
    val imgUrl:String="",
    val options: List<QuizOption> = listOf()
)

data class QuizOption(
    val quizOp1: String = "",
    val quizOp2: String = "",
    val quizOp3: String = "",
    val quizOp4: String = ""
)
