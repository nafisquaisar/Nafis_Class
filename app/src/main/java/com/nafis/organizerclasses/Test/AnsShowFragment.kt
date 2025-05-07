package com.nafis.organizerclasses.Test

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nafis.organizerclasses.Object.QuizModel
import com.nafis.organizerclasses.Object.TestObject
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentAnsShowBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.firebase.database.*

class AnsShowFragment(
    private var clasname: String?,
    private var subname: String?,
    private var chap: String?,
    private var id: String,
    private var selectedOptionsMap: HashMap<Int, Int>,
    var courseId: String?=null,
    var testTitle: String
) : Fragment() {

    private lateinit var binding: FragmentAnsShowBinding
    private lateinit var list: ArrayList<QuizModel>
    private var totalQes = 0
    private var position = 0
    private lateinit var quizModel: QuizModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnsShowBinding.inflate(inflater, container, false)
        list = ArrayList() // Initialize the list before loading data from Firebase
        loadQuestionsFromFirebase() // Load questions from Firebase
        binding.questionBackbtn.setOnClickListener {
            activity?.supportFragmentManager?.let { fm ->
                // Remove the current fragment by popping the top of the back stack
                if (fm.backStackEntryCount > 0) {
                    fm.popBackStack() // Pops the top fragment from the back stack
                }


            }
        }

        (activity as? ClassMainActivity)?.updateTitle(testTitle)
        // Set custom back button behavior
        binding.quizchapName.text=chap



        return binding.root
    }

    private fun loadQuestionsFromFirebase() {
        notVisibile()
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
                if (!isAdded) {
                    return // Fragment is no longer added, so exit the callback
                }
                for (testPaperSnapshot in snapshot.children) {
                    val testPaper = testPaperSnapshot.getValue(TestObject::class.java)
                    if (testPaper != null && testPaper.id.equals( id)) {
                        list.addAll(testPaper.questions)
                    }
                }

                totalQes = list.size

                if (totalQes > 0) {
                    quizModel = list[position]
                    setupQuiz()
                    visibile()
                } else {
                    Log.e("com.nafis.organizerclasses.Test.AnsShowFragment", "No questions found for this test.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("com.nafis.organizerclasses.Test.AnsShowFragment", "Error loading data: ${error.message}")
            }
        })
    }

    fun visibile(){
        binding.apply {
            header.visibility=View.VISIBLE
            Question.visibility=View.VISIBLE
            optionsRadioGroup.visibility=View.VISIBLE
            correctll.visibility=View.VISIBLE
            progressbar.visibility=View.GONE
            btnll.visibility=View.VISIBLE
        }
    }
    fun notVisibile(){
        binding.apply {
            header.visibility=View.GONE
            Question.visibility=View.GONE
            optionsRadioGroup.visibility=View.GONE
            correctll.visibility=View.GONE
            progressbar.visibility=View.VISIBLE
            btnll.visibility=View.GONE
        }
    }

    private fun setupQuiz() {
        binding.questionPreviousbtn.setOnClickListener {
            if (position > 0) {
                position--
                quizModel = list[position] // Update quizModel here
                updateQuiz()
            }
        }

        binding.questionNextSkipbtn.setOnClickListener {
            if (position < totalQes - 1) {
                position++
                quizModel = list[position] // Update quizModel here
                updateQuiz()
            }
        }

        updateQuiz()
    }

    private fun updateQuiz() {
        binding.optionsRadioGroup.clearCheck()

        quizModel = list[position]
        binding.Question.text = "Q ${position+1}) ${quizModel.quizQues}"
        binding.option1radio.text = quizModel.options[0].quizOp1
        binding.option2radio.text = quizModel.options[0].quizOp2
        binding.option3radio.text = quizModel.options[0].quizOp3
        binding.option4radio.text = quizModel.options[0].quizOp4
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
        binding.currQuestionNo.text = (position + 1).toString()
        binding.totalQuestion.text = "/$totalQes"
        binding.questionProgressIndicator.progress=((position+1).toFloat()/totalQes.toFloat()*100).toInt()
        resetOptionStyles()
        setEnableOptions()
        restoreSelectedOption()
        checkButtonVisibility()
    }

    private fun checkButtonVisibility() {
        binding.questionPreviousbtn.visibility = if (position == 0) View.GONE else View.VISIBLE
        binding.questionNextSkipbtn.visibility = if (position == totalQes - 1) View.GONE else View.VISIBLE
    }

    private fun restoreSelectedOption() {
        resetOptionStyles()
        val selectedOptionId = selectedOptionsMap[position]
        if (selectedOptionId != null) {
            val selectedRadioButton = binding.optionsRadioGroup.findViewById<MaterialRadioButton>(selectedOptionId)
            if (selectedRadioButton != null) {
                selectedRadioButton.isChecked = true
                Log.d("Debug", "Selected Radio Button: ${selectedRadioButton.text}")
                if (checkCorrectOption(selectedRadioButton.text.toString())) {
                    selectedRadioButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                    updateDrawableStart(selectedRadioButton, R.drawable.checkboxbg)
                } else {
                    selectedRadioButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                    updateDrawableStart(selectedRadioButton, R.drawable.radiobtn_incorr_checked)
                }
                setDisableOptions()
            } else {
                Log.d("Debug", "Selected Radio Button is null for ID: $selectedOptionId")
            }
            highlightCorrectAnswer(quizModel.correctOp)
        } else {
            Log.d("Debug", "No option selected, highlighting the correct answer.")
            highlightCorrectAnswer(quizModel.correctOp)
        }
    }

    private fun highlightCorrectAnswer(correctAnswer: String?) {
        val radioButtons = listOf(binding.option1radio, binding.option2radio, binding.option3radio, binding.option4radio)
        val correctRadioButton = radioButtons.find { it.text.toString().equals(correctAnswer, ignoreCase = true) }

        if (correctRadioButton != null) {
            correctRadioButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
            correctRadioButton.isChecked = true // Ensure the correct radio button is checked
            Log.d("Debug", "Correct Answer Button: ${correctRadioButton.text}")
            updateDrawableStart(correctRadioButton, R.drawable.checkboxbg)
            setDisableOptions()
        } else {
            setDisableOptions()
            Log.d("Debug", "No matching radio button found for the correct answer: $correctAnswer")
        }

        binding.setAns.text = "Correct Answer: $correctAnswer"
        binding.correctll.visibility = View.VISIBLE
    }


    private fun checkCorrectOption(ans: String): Boolean {
        return ans.equals(quizModel.correctOp, ignoreCase = true)
    }

    private fun resetOptionStyles() {
        val radioButtons = listOf(binding.option1radio, binding.option2radio, binding.option3radio, binding.option4radio)
        radioButtons.forEach {
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            it.isChecked = false
            updateDrawableStart(it, R.drawable.checkboxbg)
        }
        binding.correctll.visibility = View.GONE
    }

    private fun updateDrawableStart(radioButton: MaterialRadioButton, drawableResId: Int) {
        val drawable: Drawable? = ContextCompat.getDrawable(context ?: return, drawableResId)
        radioButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        Log.d("Debug","checked btn")
    }

    private fun setDisableOptions() {
        binding.option1radio.isEnabled = false
        binding.option2radio.isEnabled = false
        binding.option3radio.isEnabled = false
        binding.option4radio.isEnabled = false
    }

    private fun setEnableOptions() {
        binding.option1radio.isEnabled = true
        binding.option2radio.isEnabled = true
        binding.option3radio.isEnabled = true
        binding.option4radio.isEnabled = true
    }

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
}
