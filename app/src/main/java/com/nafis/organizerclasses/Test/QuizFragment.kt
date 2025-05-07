package com.nafis.organizerclasses.Test

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.nafis.organizerclasses.Object.QuizModel
import com.nafis.organizerclasses.Object.TestObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentQuizBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.nafis.organizerclasses.model.QuizResult

class QuizFragment(private val clasname: String?,
                   private val subname: String?
                   , private val chap: String?,
                   private val id: String,
                   private val testTitle: String,
                   private val courseId :String?=null,
                   private val totalMark :String?=null

    ) : Fragment() {
   private lateinit var binding: FragmentQuizBinding
    private var list = ArrayList<QuizModel>()
    private var totalQes = 0
    private var position = 0
    private var right = 0
    private var skipped = 0
    private var incorrect = 0
    private lateinit var quizModel: QuizModel
    private lateinit var countDownTimer: CountDownTimer
    private var totalTimeInMillis: Long = 60 * 1000 // 60 seconds for demo; adjust as needed
    private val selectedOptionsMap = HashMap<Int, Int>() // Store selected options
    private val correctAnswersMap = HashMap<Int, Boolean>() // Store correctness of each answer
    private val answerStatusMap = HashMap<String, String>()
    private var timeTakenMillis: Long = 0L
    private var startTime: Long = 0L




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentQuizBinding.inflate(inflater,container,false)

        Log.d("testId",id)
        (activity as ClassMainActivity).updateTitle(testTitle)
        binding.quizchapName.text=chap

        loadDataFromFirebase()
        setupQuiz()

        return binding.root
    }


    //==============1. Load Data===================
    private fun loadDataFromFirebase() {
        notVisibile()
        list = ArrayList()
        try {

            val dbReference= if(courseId!=null){
                FirebaseDatabase.getInstance().getReference("CourseTest").child(courseId!!)
           }else{
                 FirebaseDatabase.getInstance().getReference("Class")
                   .child(clasname ?: "")
                   .child(subname ?: "")
                   .child(chap ?: "")
           }

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var foundTestPaper = false
                    for (testPaperSnapshot in snapshot.children) {
                        val testPaper = testPaperSnapshot.getValue(TestObject::class.java)
                        if (testPaper != null && testPaper.id.equals( id)) {
                            list.addAll(testPaper.questions)
                            totalTimeInMillis = (testPaper.totalTime).toLong()* 60 * 1000 // Convert minutes to milliseconds
                            foundTestPaper = true
                            break
                        }
                    }

                    if (foundTestPaper) {
                        totalQes = list.size
                        if (totalQes > 0) {
                            position = 0
                            SetQuiz() // Only call SetQuiz if there are questions to display
                            visibile()
                            startTimer(totalTimeInMillis)
                        } else {
                            // Handle the case when there are no questions
                            binding.progredssll.visibility=View.GONE
                            binding.notQuestionFound.visibility=View.VISIBLE
                        }
                    } else {
                        // Handle the case when the test paper ID is not found
                        binding.progredssll.visibility=View.GONE
                        binding.notQuestionFound.visibility=View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progredssll.visibility=View.GONE
                    binding.notQuestionFound.visibility=View.VISIBLE
                }
            })
        } catch (e: Exception) {
            binding.progredssll.visibility=View.GONE
            binding.notQuestionFound.visibility=View.VISIBLE
        }
    }

    //========= 2. Visibility And Not Visiblity SetUp Start =================
    fun visibile(){
        binding.apply {
            progressbar.visibility=View.GONE
            headerTitle.visibility=View.VISIBLE
            progredssll.visibility=View.VISIBLE
            Question.visibility=View.VISIBLE
            optionsRadioGroup.visibility=View.VISIBLE
            btnll.visibility=View.VISIBLE
            notQuestionFound.visibility=View.GONE
        }
    }
    fun notVisibile(){
        binding.apply {
            progressbar.visibility=View.VISIBLE
            headerTitle.visibility=View.GONE
            progredssll.visibility=View.GONE
            Question.visibility=View.GONE
            optionsRadioGroup.visibility=View.GONE
            btnll.visibility=View.GONE
            notQuestionFound.visibility=View.GONE
        }
    }
    //=========Visibility And Not Visiblity SetUp End =================

    //============ 3. All the Button of the Quiz Start ======================
    private fun setupQuiz() {
        binding.questionPreviousbtn.setOnClickListener {
            saveSelectedOption()
            position--
            SetQuiz()
        }

        binding.questionNextSkipbtn.setOnClickListener {
            saveSelectedOption()
            position++
            SetQuiz()
        }

        binding.questionSubmitbtn.setOnClickListener {
            saveSelectedOption()
            calculateScore()

            val percentage = ((right.toFloat() / totalQes.toFloat()) * 100).toInt()
            saveResult(percentage)

            // Navigate to ResultFragment
           if(courseId!=null){
               val fm = activity?.supportFragmentManager
               fm?.let { fragmentManager ->
                   // Check if QuizFragment exists in the back stack and pop it
                   while (fragmentManager.backStackEntryCount > 0) {
                       fragmentManager.popBackStackImmediate()
                   }

                   // Replace current fragment with ResultFragment and add the transaction to back stack
                   fragmentManager.beginTransaction().apply {
                       replace(
                           R.id.wrapper, ResultFragment(
                               answerStatusMap,
                               totalQes,
                               clasname,
                               subname,
                               chap,
                               id,
                               selectedOptionsMap,
                               courseId,
                               testTitle,
                               timeTakenMillis,
                               totalMark
                           )
                       )
                       addToBackStack("ResultFragment") // Add ResultFragment to back stack
                       commit()
                   }
               }
           }else{
               val fm = activity?.supportFragmentManager
               fm?.let { fragmentManager ->
                   // Check if QuizFragment exists in the back stack and pop it
                   if (fragmentManager.backStackEntryCount > 1) {
                       fragmentManager.popBackStack() // Pops QuizFragment
                   }

                   // Replace current fragment with ResultFragment and add the transaction to back stack
                   fragmentManager.beginTransaction().apply {
                       replace(
                           R.id.wrapper, ResultFragment(
                               answerStatusMap,
                               totalQes,
                               clasname,
                               subname,
                               chap,
                               id,
                               selectedOptionsMap,
                               courseId,
                               testTitle,
                               timeTakenMillis,
                               totalMark
                           )
                       )
                       addToBackStack("ResultFragment") // Add ResultFragment to back stack
                       commit()
                   }
               }
           }

            // Clear data after the transaction
            list.clear()
            position = 0
            countDownTimer.cancel()
        }
    }
    //============All the Button of the Quiz End ======================

    //============set Quiz ==========================
    private fun SetQuiz() {
        totalQes = list.size // Ensure totalQes is set
        position = Math.max(0, Math.min(position, totalQes - 1)) // Clamp position

        quizModel = list[position] // Get the current quiz model

        // Set the question and options
        binding.Question.text = "Q ${position+1}) ${quizModel.quizQues}"
        binding.option1radio.text = quizModel.options[0].quizOp1 // Access option 1
        binding.option2radio.text = quizModel.options[0].quizOp2 // Access option 2
        binding.option3radio.text = quizModel.options[0].quizOp3 // Access option 3
        binding.option4radio.text = quizModel.options[0].quizOp4 // Access option 4

        if(quizModel.imgUrl.isNotEmpty()){
            binding.questionImg.visibility=View.VISIBLE
            Glide.with(requireContext())
                .load(quizModel.imgUrl)
                .into(binding.questionImg)
            binding.questionImg.setOnClickListener {
                showImageDialog(quizModel.imgUrl)
            }
        }else{
            binding.questionImg.visibility=View.GONE
        }


        // Set the total number of questions and the current question number
        binding.totalQuestion.text = "/${totalQes}"
        binding.currQuestionNo.text = (position + 1).toString()

        // Set the progress indicator
        binding.questionProgressIndicator.progress =
            ((position + 1).toFloat() / totalQes.toFloat() * 100).toInt()


        // Restore previously selected option, if any
        restoreSelectedOption()

        // Add functionality for when options are checked
        checkOption()

        // Update navigation buttons (next, previous, etc.)
        updateNavigationButtons()
    }
    //============set Quiz ==========================

    //============ 4. Calculate Mark Start =====================
    private fun calculateScore() {
        for (i in 0 until totalQes) {
            if (!answerStatusMap.containsKey(i.toString())) {
                answerStatusMap[i.toString()] = "skipped"
            }
        }

        right = answerStatusMap.values.count { it == "correct" }
        incorrect = answerStatusMap.values.count { it == "incorrect" }
        skipped = answerStatusMap.values.count { it == "skipped" }

        Log.d("ScoreInfo", "Correct: $right, Incorrect: $incorrect, Skipped: $skipped")
    }


    //============Calculate Mark Start =====================

    //============ 5. Check Correct Option Start =====================
    private fun checkCorrectOption(ans: String): Boolean {
        return ans.equals(quizModel.correctOp, ignoreCase = true)
    }
    //============Check Correct Option end =====================

    //============6. Update Visibilty OF the next and previous btn start =====================
    private fun updateNavigationButtons() {
        binding.questionPreviousbtn.visibility = if (position <= 0) View.GONE else View.VISIBLE
        binding.questionNextSkipbtn.visibility = if (position >= totalQes - 1) View.GONE else View.VISIBLE
    }
    //============Update Visibilty OF the next and previous btn end =====================

    //============  7. check Option start   =====================
    private fun checkOption() {
        binding.optionsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton?.text.toString()
            correctAnswersMap[position] = checkCorrectOption(selectedText)
        }
    }

    //============ check Option end   =====================

    //============  7.Save Selected option start =====================
    private fun saveSelectedOption() {
        val selectedId = binding.optionsRadioGroup.checkedRadioButtonId
        if (selectedId != -1) {
            selectedOptionsMap[position] = selectedId

            // Check if selected answer is correct or not
            val selectedRadioButton = binding.optionsRadioGroup.findViewById<RadioButton>(selectedId)
            val selectedText = selectedRadioButton?.text.toString()

            val isCorrect = checkCorrectOption(selectedText)
            correctAnswersMap[position] = isCorrect

            // Save status
            answerStatusMap[position.toString()] = if (isCorrect) "correct" else "incorrect"
        } else {
            // If no option selected, mark as skipped
            answerStatusMap[position.toString()] = "skipped"
        }
    }

    //============ Save Selected option end =====================


    //============ 8. Restore the selected btn start  =====================
    private fun restoreSelectedOption() {
        val savedOptionId = selectedOptionsMap[position]
        if (savedOptionId != null) {
            binding.optionsRadioGroup.check(savedOptionId)
        } else {
            binding.optionsRadioGroup.clearCheck()
        }
    }
   //============ Restore the selected btn end  =====================

    //============ 9. Timer Funtion setUp start  =====================


    private fun startTimer(millisInFuture: Long) {
        startTime = System.currentTimeMillis()

        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate and display remaining time
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timer.text = String.format("%02d:%02d", minutes, seconds)

                // Update time taken so far
                timeTakenMillis = System.currentTimeMillis() - startTime
            }

            override fun onFinish() {
                // Final time taken
                timeTakenMillis = System.currentTimeMillis() - startTime

                // Move to ResultFragment with all data
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(
                        R.id.wrapper,
                        ResultFragment(
                            answerStatusMap,
                            totalQes,
                            clasname,
                            subname,
                            chap,
                            id,
                            selectedOptionsMap,
                            courseId,
                            testTitle,
                            timeTakenMillis,
                            totalMark
                        )
                    )
                    ?.addToBackStack(null)
                    ?.commit()

                // Reset values
                list.clear()
                position = 0
            }
        }

        countDownTimer.start()
    }
    //============ Counter Funtion setUp end  =====================


    //============ 10. Make the Timer funtion end after end the test start  =====================
    override fun onDestroy() {
        super.onDestroy()
        try {
            if (::countDownTimer.isInitialized) {
                countDownTimer.cancel() // Cancel only if initialized
            }
        } catch (e: UninitializedPropertyAccessException) {
            // Handle the case where the timer wasn't initialized (this should not happen with isInitialized check)
            Toast.makeText(context, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    //============ Make the Timer funtion end after end the test end =====================


    //===============show Image Dialog Image in full size if we want then click on the photo ==================
    private fun showImageDialog(QuesImgUrl: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.image_view_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

        // Set the image in the dialog
        val fullImageView = dialog.findViewById<ImageView>(R.id.fullImageView)
        Glide.with(requireContext())
            .load(QuesImgUrl)
            .placeholder(R.drawable.pyq_icon)
            .into(fullImageView)
        // Dismiss the dialog when the image is clicked
        fullImageView.setOnClickListener {
            dialog.dismiss()
        }
    }


    private fun saveResult(percentage: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val result = QuizResult(
            answerStatusMap = answerStatusMap.mapKeys { it.key.toString() },
            total = totalQes,
            percentage = percentage,
            className = clasname ?: "",
            subjectName = subname ?: "",
            chapter = chap ?: "",
            testId = id,
            testTitle = testTitle,
            courseId = courseId,
            correctAnswers = selectedOptionsMap.mapKeys { it.key.toString() },
            timestamp = timeTakenMillis
        )

        if (courseId != null) {
            // 🔹 Save under Course path
            db.collection("Course")
                .document(courseId!!)
                .collection("User")
                .document(userId)
                .collection("quiz_results")
                .document(id) // or remove this duplicate segment if not needed
                .collection("attempts")
                .add(result)
                .addOnSuccessListener { Log.d("Firestore", "Result saved in Course") }
                .addOnFailureListener { Log.e("Firestore", "Failed to save result in Course", it) }
        } else {
            // 🔹 Save under default user path
            db.collection("users")
                .document(userId)
                .collection("quiz_results")
                .document(id)
                .collection("attempts")
                .add(result)
                .addOnSuccessListener { Log.d("Firestore", "Result saved under User") }
                .addOnFailureListener { Log.e("Firestore", "Failed to save result under User", it) }
        }
    }



}
