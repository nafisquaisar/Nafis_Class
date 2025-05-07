package com.nafis.organizerclasses.Test

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nafis.nafis.nf2024.organizeradminpanel.Fragment.CourseClassAndTestFragment
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.adapter.QuizResultAdapter
import com.nafis.organizerclasses.databinding.FragmentResultBinding
import com.nafis.organizerclasses.model.QuizResult

class ResultFragment(
    private var answerStatusMap: HashMap<String, String>,
    private var total: Int,
    private var clasname: String? = "",
    private var subname: String? = "",
    private var chap: String? = "",
    private var id: String,
    private var correctans: HashMap<Int, Int>,
    private var courseId: String? = null,
    private var testTitle: String,
    private var countDownTimer: Long,
    private var totalMark: String?,
) : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var animationView: LottieAnimationView
    private lateinit var quizResultAdapter: QuizResultAdapter

    private var attemptsList = mutableListOf<QuizResult>()
    private var right = 0
    private var skipped = 0
    private var incorrectCnt = 0
    private var markObtain = 0
    private var isDataFetched = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        animationView = binding.animationView
        Log.d("courseID Starting" ,courseId.toString())
        quizResultAdapter = QuizResultAdapter { selected ,attemptNumber->
            showSelectedAttemptDetails(selected, attemptNumber)
        }

        calculateScore(answerStatusMap)
        val percentage = ((right.toFloat() / total.toFloat()) * 100).toInt()

        (activity as ClassMainActivity).updateTitle("Result")


        setupUI(percentage)
        fetchData()
        setupPieChart(right, incorrectCnt, skipped, total)

        setupClickListeners()
        handleBackPress()

        return binding.root
    }

    private fun setupUI(percentage: Int) = binding.apply {
        resultscore.text = "You got $markObtain out of $totalMark"
        mockTitle.text = testTitle
        totalScore.text = "$markObtain"
        totalCorrect.text = "$right"
        totalInCorrect.text = incorrectCnt.toString()
        totalSkip.text = "$skipped"

        val minutes = (countDownTimer / 1000) / 60
        val seconds = (countDownTimer / 1000) % 60
        totalTimeTaken.text = String.format("%02d:%02d", minutes, seconds)

        val feedback = when (percentage) {
            in 91..100 -> R.color.Excellent to "Excellent"
            in 80..90 -> R.color.Outstanding to "Outstanding"
            in 70..79 -> R.color.VeryGood to "Very Good"
            in 50..69 -> R.color.Good to "Good"
            else -> R.color.Bad to "Oops! You have failed"
        }

        progresstext.text = feedback.second
        progresstext.setTextColor(ContextCompat.getColor(requireContext(), feedback.first))

        updateAnimation(if (percentage >= 50)
            "https://lottie.host/a8820a63-e95f-4a2e-84af-36d4912c42d2/Xb2VVh8YRT.json"
        else
            "https://lottie.host/811f7a8d-c6f3-43e8-b2b8-9383cd77ef0b/QzA9Q3KgaQ.json")
    }

    private fun setupClickListeners() = binding.apply {
        ExploreMore.setOnClickListener { navigateToExplore() }
        btnReattempt.setOnClickListener { reattemptQuiz() }
        btnViewSolution.setOnClickListener { viewSolutions() }
        dropdownAttempt.setOnClickListener { showBottomDialogList() }
    }

    private fun navigateToExplore() {
        val fm = activity?.supportFragmentManager ?: return
        clearBackStackTo("ResultFragment", fm)

        val fragment = if (courseId != null) CourseClassAndTestFragment(courseId!!,courseName =null) else TotalTestFragment.newInstance(clasname!!, subname!!, chap!!)
        fm.beginTransaction().replace(R.id.wrapper, fragment).commit()
    }

    private fun reattemptQuiz() {
        val fm = activity?.supportFragmentManager ?: return
        clearBackStackTo("ResultFragment", fm)
        fm.beginTransaction()
            .replace(R.id.wrapper, QuizFragment(clasname, subname, chap, id, testTitle = testTitle, courseId = courseId,totalMark=totalMark))
            .addToBackStack("QuizFragmentTag")
            .commitAllowingStateLoss()
    }

    private fun viewSolutions() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.wrapper, AnsShowFragment(clasname, subname, chap, id, correctans, courseId, testTitle))
            ?.addToBackStack("ResultFragment")
            ?.commit()
    }

    private fun clearBackStackTo(tag: String, fm: FragmentManager) {
        val count = fm.backStackEntryCount
        for (i in count - 1 downTo 0) {
            val entry = fm.getBackStackEntryAt(i)
            if (entry.name == tag) {
                fm.popBackStack(entry.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                break
            }
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fm = activity?.supportFragmentManager
                if (fm != null && fm.backStackEntryCount > 1) {
                    fm.popBackStack()
                } else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    private fun showBottomDialogList() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_attempt_list, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerAttempts)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        quizResultAdapter = QuizResultAdapter { selected ,attemptNumber ->
            showSelectedAttemptDetails(selected,attemptNumber)
            dialog.dismiss()
        }

        recyclerView.adapter = quizResultAdapter
        quizResultAdapter.submitList(attemptsList)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showSelectedAttemptDetails(result: QuizResult, attemptNumber: Int) {

        binding.dropdownAttempt.text = "Attempt $attemptNumber"


        val (correct, incorrect, skipped) = calculateScore(result.answerStatusMap)
        val percentage = result.percentage
        val markPerQuestion = (totalMark?.toIntOrNull() ?: 0) / total
        val obtained = correct * markPerQuestion
        correctans.clear()
        correctans.putAll(result.correctAnswers.mapKeys { it.key.toInt() })
        val totaltimetaken=result.timestamp

        binding.apply {
            resultscore.text = "You got $obtained out of $totalMark"
            mockTitle.text = result.testTitle
            totalScore.text = "$obtained"
            totalCorrect.text = "$correct"
            totalInCorrect.text = incorrect.toString()
            totalSkip.text = "$skipped"
            val minutes = (totaltimetaken / 1000) / 60
            val seconds = (totaltimetaken / 1000) % 60
            totalTimeTaken.text = String.format("%02d:%02d", minutes, seconds)

            val feedback = when (percentage) {
                in 91..100 -> R.color.Excellent to "Excellent"
                in 80..90 -> R.color.Outstanding to "Outstanding"
                in 70..79 -> R.color.VeryGood to "Very Good"
                in 50..69 -> R.color.Good to "Good"
                else -> R.color.Bad to "Oops! You have failed"
            }

            progresstext.text = feedback.second
            progresstext.setTextColor(ContextCompat.getColor(requireContext(), feedback.first))

            updateAnimation(if (percentage >= 50)
                "https://lottie.host/a8820a63-e95f-4a2e-84af-36d4912c42d2/Xb2VVh8YRT.json"
            else
                "https://lottie.host/811f7a8d-c6f3-43e8-b2b8-9383cd77ef0b/QzA9Q3KgaQ.json")

            setupPieChart(correct, incorrect, skipped, total)
        }
    }

    private fun fetchData() {
        if (isDataFetched) return

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        attemptsList.clear()

        // Determine the base path based on whether courseId is null or not
        Log.d("courseId",courseId.toString())
        val baseRef = if (courseId != null) {
            db.collection("Course")
                .document(courseId!!)
                .collection("User")
                .document(userId)
                .collection("quiz_results")
                .document(id) // or remove this duplicate segment if not needed
                .collection("attempts")
        } else {
            db.collection("users")
                .document(userId)
                .collection("quiz_results")
                .document(id)
                .collection("attempts")
        }

        baseRef.orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!isAdded) return@addOnSuccessListener
                snapshot.documents.forEach {
                    val obj = it.toObject(QuizResult::class.java)
                    Log.d("QuizResult", "Parsed: $obj")
                }

                attemptsList = snapshot.documents.mapNotNull { doc ->
                    val result = doc.toObject(QuizResult::class.java)
                    result?.let {
                        Log.d("AttemptID", "Document ID: ${doc.id}")
                        it // return the result
                    }
                }.toMutableList()
                quizResultAdapter.submitList(attemptsList)

                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    setupLineChart(attemptsList)
                }

                isDataFetched = true
            }
            .addOnFailureListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Failed to fetch results", Toast.LENGTH_SHORT).show()
                }
            }
    }




    private fun updateAnimation(url: String) {
        animationView.setAnimationFromUrl(url)
    }

    private fun setupLineChart(results: List<QuizResult>) {
        val entries = results.mapIndexed { index, result -> Entry(index.toFloat(), result.percentage.toFloat()) }

        val dataSet = LineDataSet(entries, "Score %").apply {
            color = ContextCompat.getColor(requireContext(), R.color.welcome_blue)
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(Color.BLACK)
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(requireContext(), R.color.welcome_blue)
        }

        binding.scoreTrendChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false
            invalidate()
        }
    }

    private fun setupPieChart(correct: Int, incorrect: Int, skipped: Int, total: Int) {
        val entries = listOf(
            PieEntry(correct.toFloat(), "Correct"),
            PieEntry(incorrect.toFloat(), "Incorrect"),
            PieEntry(skipped.toFloat(), "Skipped")
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.welcome_green),
                ContextCompat.getColor(requireContext(), R.color.holo_red_dark),
                ContextCompat.getColor(requireContext(), R.color.grey)
            )
            valueTextSize = 14f
        }

        binding.accuracyPieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            centerText = String.format("Correct: %.1f%%", (correct.toFloat() / total) * 100)
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            invalidate()
        }
    }

    private fun calculateScore(statusMap: Map<String, String>): List<Int> {
        right = statusMap.values.count { it == "correct" }
        incorrectCnt = statusMap.values.count { it == "incorrect" }
        skipped = statusMap.values.count { it == "skipped" }

        val markPerQuestion = (totalMark?.toIntOrNull() ?: 0).let { if (total > 0) it / total else 0 }
        markObtain = right * markPerQuestion

        return listOf(right, incorrectCnt, skipped)
    }


    override fun onStop() {
        super.onStop()
        isDataFetched = false // Reset flag when the fragment is stopped
    }

}