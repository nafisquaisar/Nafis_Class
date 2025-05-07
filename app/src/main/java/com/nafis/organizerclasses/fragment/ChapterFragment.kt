package com.nafis.organizerclasses.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafis.nafis.nf2024.organizeradminpanel.DiffutilCallBack.ChapterCallback
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.adapter.ChapterAdapter
import com.nafis.organizerclasses.adapter.CourseChapterAdapter
import com.nafis.organizerclasses.activity.ClassMainActivity
import com.nafis.organizerclasses.databinding.FragmentChapterBinding
import com.nafis.organizerclasses.model.Chapter
import com.nafis.organizerclasses.model.ChapterModel
import com.google.firebase.firestore.FirebaseFirestore

class ChapterFragment (
    private val subject: String?="",
    private val clas:String?="" ,
    private var testfrag:String?="",
    private var courseId:String?=null,
    private var subjectId:String?=null,
    private var subjectName:String?=null
) : Fragment() {
    private lateinit var binding: FragmentChapterBinding
    private lateinit var list:ArrayList<ChapterModel>
    private lateinit var adapter: ChapterAdapter
    private var db = FirebaseFirestore.getInstance()
    private lateinit var chapterList:ArrayList<Chapter>
    private lateinit var courseChapAdapter:CourseChapterAdapter

    private val callback by lazy {
        object: ChapterCallback {
            override fun onChapterClick(item: Chapter) {
                var fm=(context as AppCompatActivity).supportFragmentManager
                var transaction=fm.beginTransaction()
                var lectureFragment=LectureFragment(courseId =courseId, subjectId = subjectId, chapterId = item.chapId, courseChapterName = item.chapName)
                transaction.replace(R.id.wrapper,lectureFragment)
                transaction.addToBackStack("lectureFragment")
                transaction.commit()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentChapterBinding.inflate(inflater,container,false)


        courseId?.let { Log.d("courseId", it) }
        subjectId?.let { Log.d("subjectId", it) }

       if(courseId!=null && subjectId!=null){
           subjectName?.let { (activity as ClassMainActivity).updateTitle(it) }
           chapterList= ArrayList()
           binding.chapterRecyclerview.layoutManager=LinearLayoutManager(requireContext())
           courseChapAdapter=CourseChapterAdapter(requireContext(),callback,chapterList)
           binding.chapterRecyclerview.adapter=courseChapAdapter
           fetchChapters()

       }else{
           (activity as ClassMainActivity).updateTitle(subject.toString())
           LoadData()
       }

        return binding.root
    }

    private fun fetchChapters() {
        binding.progressbar.visibility = View.VISIBLE
        chapterList.clear()
        db.collection("courses")
            .document(courseId!!)
            .collection("subjects")
            .document(subjectId!!)
            .collection("Chapters")
            .get()
            .addOnSuccessListener {Chapters->
                for(chap in Chapters){
                    val chapter=chap.toObject(Chapter::class.java)
                    chapterList.add(chapter)
                }
                if (chapterList.isEmpty()) {
                    binding.helping.visibility=View.VISIBLE
                } else {
                    courseChapAdapter.submitList(chapterList)
                    courseChapAdapter.notifyDataSetChanged()
                    binding.helping.visibility=View.GONE
                }
                binding.progressbar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch Subject: ${it.message}", Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.GONE
                binding.helping.visibility=View.VISIBLE
            }

    }

    private fun LoadData() {
        list =ArrayList()

        when (subject) {
            "Physics" -> {
                when (clas) {
                    "Class 12" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(
                            ChapterModel(
                                "Electrostatics",
                                "Test your static skills",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Current Flow",
                                "Master electricity today",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Magnetism",
                                "Unveil magnetic mysteries",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Induction", "Induce your success", clas, subject))
                        list.add(ChapterModel("AC Circuits", "Navigate AC wonders", clas, subject))
                        list.add(ChapterModel("EM Waves", "Ride the EM waves", clas, subject))
                        list.add(ChapterModel("Optics", "Explore light's secrets", clas, subject))
                        list.add(
                            ChapterModel(
                                "Dual Nature",
                                "Quantum meets reality",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Atoms", "Atomic discoveries await", clas, subject))
                        list.add(ChapterModel("Nuclei", "Unlock nuclear power", clas, subject))
                        list.add(
                            ChapterModel(
                                "Semiconductors",
                                "Crack semiconductor codes",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 11" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(
                            ChapterModel(
                                "Physical World",
                                "Explore physics universe",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Vectors", "Vector your victory", clas, subject))
                        list.add(
                            ChapterModel(
                                "Measurements",
                                "Precision meets practice",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Straight Line",
                                "Linear motion mastery",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Motion Plane",
                                "Plane motion precision",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Motion Laws", "Rule the motion laws", clas, subject))
                        list.add(
                            ChapterModel(
                                "Energy & Power",
                                "Harness power & energy",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Rotation",
                                "Conquer rotational forces",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Gravitation", "Gravity in focus", clas, subject))
                        list.add(
                            ChapterModel(
                                "Solid Mechanics",
                                "Solid strength tests",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Fluid Dynamics",
                                "Flow with the fluids",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Thermal Props",
                                "Heat up your knowledge",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Thermodynamics",
                                "Thermal energy tactics",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Kinetic Theory",
                                "Master particle motion",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Oscillations", "Vibrate to success", clas, subject))
                        list.add(ChapterModel("Waves", "Wave through challenges", clas, subject))
                    }

                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(
                            ChapterModel(
                                "Electricity",
                                "Power up your knowledge",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Magnetism",
                                "Attract magnetic insights",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Light", "Shine with optics", clas, subject))
                        list.add(ChapterModel("Human Eye", "See the science", clas, subject))
                        list.add(ChapterModel("Energy", "Explore energy sources", clas, subject))
                    }

                    "Class 9" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Motion", "Move ahead in motion", clas, subject))
                        list.add(
                            ChapterModel(
                                "Force & Motion",
                                "Force your success",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Gravitation",
                                "Gravity takes center stage",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Energy", "Energy exploration starts", clas, subject))
                        list.add(ChapterModel("Sound", "Tune into sound", clas, subject))
                    }

                }
            }


            "Chemistry" -> {
                when (clas) {
                    "Class 12" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(
                            ChapterModel(
                                "Solid State",
                                "Crystallize your knowledge",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Solutions", "Mix it up!", clas, subject))
                        list.add(
                            ChapterModel(
                                "Electrochemistry",
                                "Charge up your skills",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Chem Kinetics",
                                "Speed through reactions",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Surface Chem",
                                "Surface-level science",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Metal Extraction",
                                "Dig into elements",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("p-Block", "Unlock p-block secrets", clas, subject))
                        list.add(
                            ChapterModel(
                                "d/f-Block",
                                "Discover transition elements",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Coord Compounds", "Complex yet fun!", clas, subject))
                        list.add(ChapterModel("Haloalkanes", "React with halogens", clas, subject))
                        list.add(ChapterModel("Alcohols", "Sip on chemistry", clas, subject))
                        list.add(ChapterModel("Aldehydes", "Aldehydes unlocked!", clas, subject))
                        list.add(
                            ChapterModel(
                                "Nitro Compounds",
                                "Nail nitrogen knowledge",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Biomolecules",
                                "Biology meets chemistry",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Polymers", "Link the chains!", clas, subject))
                        list.add(ChapterModel("Daily Chem", "Chemistry in life", clas, subject))
                    }

                    "Class 11" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(
                            ChapterModel(
                                "Chem Basics",
                                "Foundation of reactions",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Atom Structure", "Dive into atoms", clas, subject))
                        list.add(
                            ChapterModel(
                                "Element Trends",
                                "Periodic table fun",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Chem Bonds", "Molecules bond better", clas, subject))
                        list.add(
                            ChapterModel(
                                "Matter States",
                                "Solid, liquid, gas!",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Thermodynamics",
                                "Energy transformations",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Equilibrium",
                                "Balance the reactions",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Redox", "Oxidation meets reduction", clas, subject))
                        list.add(ChapterModel("Hydrogen", "Hydrogen highlights", clas, subject))
                        list.add(ChapterModel("s-Block", "Alkali & Alkaline Earth", clas, subject))
                        list.add(ChapterModel("p-Block", "Learn p-block elements", clas, subject))
                        list.add(
                            ChapterModel(
                                "Org Chem Basics",
                                "Organic chemistry start",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Hydrocarbons", "Fuel your knowledge", clas, subject))
                        list.add(
                            ChapterModel(
                                "Env Chem",
                                "Green chemistry insights",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Chem Reactions", "React and learn", clas, subject))
                        list.add(ChapterModel("Acids & Bases", "pH matters!", clas, subject))
                        list.add(
                            ChapterModel(
                                "Metals & Non-metals",
                                "Elemental differences",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Carbon Compounds",
                                "Carbon-based wonders",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Element Classification",
                                "Periodic table tricks",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 9" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(
                            ChapterModel(
                                "Matter Around",
                                "Explore matter's magic",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Pure Matter", "Purity in chemistry", clas, subject))
                        list.add(
                            ChapterModel(
                                "Atoms & Molecules",
                                "Small but mighty",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Atom Structure", "Atoms unveiled", clas, subject))
                        list.add(ChapterModel("Life Unit", "Chemistry of life", clas, subject))
                    }


                }
            }


            "Math" -> {
                when (clas) {
                    "Class 12" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Relations",
                                "Master Sets & Functions",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Inverse Trigonometry",
                                "Unlock Trig Secrets",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Matrices", "Matrix Magic Unveiled", clas, subject))
                        list.add(
                            ChapterModel(
                                "Determinants",
                                "Solve Systems Swiftly",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Continuity", "Smooth Calculus Flow", clas, subject))
                        list.add(ChapterModel("Derivatives", "Find Slopes Fast", clas, subject))
                        list.add(ChapterModel("Integrals", "Area Under Curves", clas, subject))
                        list.add(
                            ChapterModel(
                                "Applications of Integrals",
                                "Real-World Integrals",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Differentials", "Change & Rates", clas, subject))
                        list.add(ChapterModel("Vectors", "Navigate 3D Space", clas, subject))
                        list.add(ChapterModel("3D Geometry", "Shapes in Space", clas, subject))
                        list.add(
                            ChapterModel(
                                "Linear Programming",
                                "Optimize & Solve",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Probability", "Predict the Future", clas, subject))
                    }

                    "Class 11" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Sets", "Begin with Basics", clas, subject))
                        list.add(ChapterModel("Functions", "Map Inputs to Outputs", clas, subject))
                        list.add(
                            ChapterModel(
                                "Trigonometric Functions",
                                "Angles & Ratios",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Mathematical Induction",
                                "Prove with Logic",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Complex Numbers",
                                "Imaginary Made Real",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Inequalities", "Compare & Contrast", clas, subject))
                        list.add(ChapterModel("Permutations", "Count the Ways", clas, subject))
                        list.add(
                            ChapterModel(
                                "Binomial Theorem",
                                "Expand with Ease",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Sequences", "Patterns in Numbers", clas, subject))
                        list.add(
                            ChapterModel(
                                "Straight Lines",
                                "Graph Linear Equations",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Conic Sections",
                                "Explore Curved Paths",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Introduction to 3D Geometry",
                                "Visualize Geometry",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Limits", "Approach Infinity", clas, subject))
                        list.add(ChapterModel("Reasoning", "Logical Connections", clas, subject))
                        list.add(ChapterModel("Statistics", "Data Analysis Basics", clas, subject))
                        list.add(ChapterModel("Probability", "Chance & Likelihood", clas, subject))
                    }

                    "Class 10" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Real Numbers",
                                "Explore Infinite Sets",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Polynomials", "Polynomial Fun", clas, subject))
                        list.add(
                            ChapterModel(
                                "Linear Equations",
                                "Solve Equations Fast",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Quadratic Equations",
                                "Roots of Equations",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Progressions", "Sum the Series", clas, subject))
                        list.add(ChapterModel("Triangles", "Prove & Apply", clas, subject))
                        list.add(
                            ChapterModel(
                                "Coordinate Geometry",
                                "Map Points Easily",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Trigonometry", "Angle Relations", clas, subject))
                        list.add(
                            ChapterModel(
                                "Applications of Trigonometry",
                                "Real-World Trig",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Circles", "Master Round Shapes", clas, subject))
                        list.add(ChapterModel("Construction", "Geometric Drawings", clas, subject))
                        list.add(ChapterModel("Areas", "Measure Shapes Precisely", clas, subject))
                        list.add(ChapterModel("Volumes", "Calculate 3D Space", clas, subject))
                        list.add(ChapterModel("Statistics", "Handle Data Smartly", clas, subject))
                        list.add(ChapterModel("Probability", "Predict Outcomes", clas, subject))
                    }

                    "Class 9" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Number Systems",
                                "Explore Number Sets",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Polynomials", "Play with Equations", clas, subject))
                        list.add(
                            ChapterModel(
                                "Coordinate Geometry",
                                "Graph with Precision",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Linear Equations",
                                "Equations Made Easy",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Euclid's Geometry",
                                "Foundations of Geometry",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Lines and Angles",
                                "Understand Angles",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Triangles", "Prove with Logic", clas, subject))
                        list.add(
                            ChapterModel(
                                "Quadrilaterals",
                                "Explore Four Sides",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Areas", "Calculate Areas", clas, subject))
                        list.add(
                            ChapterModel(
                                "Circles",
                                "Understand Circular Shapes",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Construction", "Draw with Accuracy", clas, subject))
                        list.add(
                            ChapterModel(
                                "Heron's Formula",
                                "Area without Height",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Surface Area", "3D Shape Areas", clas, subject))
                        list.add(ChapterModel("Statistics", "Analyze Data", clas, subject))
                        list.add(
                            ChapterModel(
                                "Probability",
                                "Predict Possibilities",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 8" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Rational Numbers",
                                "Fractional Numbers",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Linear Equations",
                                "Solve Simple Equations",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Quadrilaterals", "Study Four Sides", clas, subject))
                        list.add(
                            ChapterModel(
                                "Practical Geometry",
                                "Construct Shapes",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Data Handling",
                                "Organize & Interpret",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Squares", "Perfect Squares", clas, subject))
                        list.add(ChapterModel("Cubes", "Perfect Cubes", clas, subject))
                        list.add(ChapterModel("Quantities", "Compare Ratios", clas, subject))
                        list.add(ChapterModel("Algebra", "Expressions & Identities", clas, subject))
                        list.add(ChapterModel("Solid Shapes", "Visualize in 3D", clas, subject))
                        list.add(ChapterModel("Mensuration", "Measure Surfaces", clas, subject))
                        list.add(ChapterModel("Exponents", "Power of Numbers", clas, subject))
                        list.add(ChapterModel("Proportions", "Direct & Inverse", clas, subject))
                        list.add(ChapterModel("Factorization", "Break Down Numbers", clas, subject))
                        list.add(ChapterModel("Graphs", "Plot Data Points", clas, subject))
                        list.add(ChapterModel("Numbers", "Play with Patterns", clas, subject))
                    }

                    "Class 7" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Integers",
                                "Understand Whole Numbers",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Fractions", "Work with Parts", clas, subject))
                        list.add(
                            ChapterModel(
                                "Data Handling",
                                "Analyze & Interpret",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Simple Equations",
                                "Solve Basic Equations",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Lines and Angles", "Basic Geometry", clas, subject))
                        list.add(ChapterModel("Triangles", "Properties & Proofs", clas, subject))
                        list.add(
                            ChapterModel(
                                "Congruence",
                                "Match Shapes Perfectly",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Quantities", "Compare and Measure", clas, subject))
                        list.add(
                            ChapterModel(
                                "Rational Numbers",
                                "Explore Rationality",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Practical Geometry",
                                "Draw with Precision",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Perimeter", "Measure Boundaries", clas, subject))
                        list.add(
                            ChapterModel(
                                "Algebraic Expressions",
                                "Simplify Expressions",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Exponents", "Use Powers Wisely", clas, subject))
                        list.add(ChapterModel("Symmetry", "Mirror Perfection", clas, subject))
                        list.add(
                            ChapterModel(
                                "Solid Shapes",
                                "Visualize 3D Objects",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 6" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Numbers", "Understand Big Numbers", clas, subject))
                        list.add(
                            ChapterModel(
                                "Whole Numbers",
                                "Counting Beyond Zero",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Play with Numbers",
                                "Explore Number Tricks",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Geometry", "Understand Shapes", clas, subject))
                        list.add(ChapterModel("Shapes", "Recognize Basic Shapes", clas, subject))
                        list.add(
                            ChapterModel(
                                "Integers",
                                "Learn About Negative Numbers",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Fractions", "Divide the Whole", clas, subject))
                        list.add(ChapterModel("Decimals", "Handle Decimal Points", clas, subject))
                        list.add(
                            ChapterModel(
                                "Data Handling",
                                "Organize & Read Data",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Mensuration",
                                "Measure Lengths & Areas",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Ratio", "Compare Two Values", clas, subject))
                        list.add(ChapterModel("Symmetry", "Understand Reflection", clas, subject))
                        list.add(
                            ChapterModel(
                                "Geometry Basics",
                                "Draw Simple Shapes",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 5" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Numbers", "Learn Number Sense", clas, subject))
                        list.add(
                            ChapterModel(
                                "Addition and Subtraction",
                                "Master Basic Arithmetic",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Multiplication and Division",
                                "Work with Operations",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Fractions",
                                "Understand Part of Wholes",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Decimals", "Learn Decimal Numbers", clas, subject))
                        list.add(ChapterModel("Geometry", "Explore Shapes", clas, subject))
                        list.add(
                            ChapterModel(
                                "Measurements",
                                "Measure Lengths & Weights",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Time", "Understand Time Concepts", clas, subject))
                        list.add(ChapterModel("Data Handling", "Organize Data", clas, subject))
                    }

                    "Class 4" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Numbers", "Work with Bigger Numbers", clas, subject))
                        list.add(
                            ChapterModel(
                                "Addition and Subtraction",
                                "Solve Advanced Problems",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Multiplication and Division",
                                "Practice Operations",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Fractions", "Learn Parts of Whole", clas, subject))
                        list.add(ChapterModel("Geometry", "Basic Shapes & Angles", clas, subject))
                        list.add(
                            ChapterModel(
                                "Measurement",
                                "Length, Weight & Volume",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Time", "Read Clocks Accurately", clas, subject))
                        list.add(ChapterModel("Money", "Learn Money Calculations", clas, subject))
                        list.add(
                            ChapterModel(
                                "Patterns",
                                "Identify Number Patterns",
                                clas,
                                subject
                            )
                        )
                    }

                    "Class 3" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Numbers", "Understand Place Value", clas, subject))
                        list.add(
                            ChapterModel(
                                "Addition and Subtraction",
                                "Refine Basic Skills",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Multiplication and Division",
                                "Work with Tables",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Shapes", "Identify Shapes", clas, subject))
                        list.add(ChapterModel("Time", "Understand Time Concepts", clas, subject))
                        list.add(
                            ChapterModel(
                                "Measurement",
                                "Basics of Measurement",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Money", "Learn Money Basics", clas, subject))
                        list.add(ChapterModel("Patterns", "Explore Number Patterns", clas, subject))
                        list.add(ChapterModel("Data Handling", "Organize Data", clas, subject))
                    }

                    "Class 2" -> {
                        list.add(
                            ChapterModel(
                                "All Chapter",
                                "Improve Your Ability",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Numbers",
                                "Recognize and Write Numbers",
                                clas,
                                subject
                            )
                        )
                        list.add(
                            ChapterModel(
                                "Addition and Subtraction",
                                "Understand Basic Operations",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Shapes", "Learn Basic Shapes", clas, subject))
                        list.add(
                            ChapterModel(
                                "Patterns",
                                "Recognize Simple Patterns",
                                clas,
                                subject
                            )
                        )
                        list.add(ChapterModel("Measurement", "Understand Sizes and Units", clas, subject))
                        list.add(ChapterModel("Time", "Learn Clocks and Time", clas, subject))
                        list.add(ChapterModel("Money", "Basics of Currency", clas, subject))
                        list.add(ChapterModel("Data", "Basics of Data", clas, subject))
                    }

                    "Class 1" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Numbers", "Learn Counting", clas, subject))
                        list.add(ChapterModel("Addition", "Basic Addition Skills", clas, subject))
                        list.add(ChapterModel("Subtraction", "Basic Subtraction Skills", clas, subject))
                        list.add(ChapterModel("Shapes", "Recognize Simple Shapes", clas, subject))
                        list.add(ChapterModel("Patterns", "Identify Basic Patterns", clas, subject))
                        list.add(ChapterModel("Measurement", "Understand Lengths", clas, subject))
                        list.add(ChapterModel("Time", "Learn Days and Hours", clas, subject))
                        list.add(ChapterModel("Money", "Simple Money Recognition", clas, subject))
                    }
                }
            }


            "Bio" -> {
                when (clas) {
                    // For Class 12
                    "Class 12" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Reproduction in Organisms", "Understanding Life Processes", clas, subject))
                        list.add(ChapterModel("Human Reproduction", "How Humans Reproduce", clas, subject))
                        list.add(ChapterModel("Reproductive Health", "Wellness & Awareness", clas, subject))
                        list.add(ChapterModel("Principles of Inheritance", "Traits & Heredity", clas, subject))
                        list.add(ChapterModel("Molecular Basis of Inheritance", "DNA & Genes", clas, subject))
                        list.add(ChapterModel("Evolution", "Journey of Life", clas, subject))
                        list.add(ChapterModel("Human Health & Disease", "Understanding Diseases", clas, subject))
                        list.add(ChapterModel("Strategies for Enhancement in Food Production", "Better Agriculture", clas, subject))
                        list.add(ChapterModel("Microbes in Human Welfare", "Useful Microorganisms", clas, subject))
                        list.add(ChapterModel("Biotechnology: Principles and Processes", "Genes & Technology", clas, subject))
                        list.add(ChapterModel("Biotechnology and Its Applications", "Applications in Daily Life", clas, subject))
                        list.add(ChapterModel("Organisms and Populations", "Ecology Basics", clas, subject))
                        list.add(ChapterModel("Ecosystem", "Interconnected Systems", clas, subject))
                        list.add(ChapterModel("Biodiversity and Conservation", "Protecting Nature", clas, subject))
                        list.add(ChapterModel("Environmental Issues", "Challenges in Nature", clas, subject))
                    }

                    // For Class 11
                    "Class 11" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Diversity in the Living World", "Variety in Organisms", clas, subject))
                        list.add(ChapterModel("Structural Organisation in Animals", "Body Structure of Animals", clas, subject))
                        list.add(ChapterModel("Cell Structure and Function", "Basic Unit of Life", clas, subject))
                        list.add(ChapterModel("Biomolecules", "Molecules in Life", clas, subject))
                        list.add(ChapterModel("Plant Physiology", "Functions in Plants", clas, subject))
                        list.add(ChapterModel("Human Physiology", "How the Human Body Works", clas, subject))
                    }

                    // For Class 10
                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Life Cycle", "Vital processes explained", clas, subject))
                        list.add(ChapterModel("Coordination", "How bodies respond", clas, subject))
                        list.add(ChapterModel("Reproduction", "Creation of new life", clas, subject))
                        list.add(ChapterModel("Heredity", "Traits across generations", clas, subject))
                        list.add(ChapterModel("Environment", "Impact on nature", clas, subject))
                        list.add(ChapterModel("Resources", "Managing natural wealth", clas, subject))
                    }

                    // For Class 9
                    "Class 9" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Cells", "Life's basic unit", clas, subject))
                        list.add(ChapterModel("Tissues", "Building blocks of life", clas, subject))
                        list.add(ChapterModel("Diversity", "Variety in organisms", clas, subject))
                        list.add(ChapterModel("Illness", "Why we get sick", clas, subject))
                        list.add(ChapterModel("Resources", "Nature’s gifts to us", clas, subject))
                        list.add(ChapterModel("Food", "Better crops & yield", clas, subject))
                    }
                }
            }

            "English Grammar" -> {
                when (clas) {
                    // For Classes 12, 11, 10, 9, 8, 7, 6, and 5
                    "Class 12", "Class 11", "Class 10", "Class 9", "Class 8", "Class 7", "Class 6", "Class 5" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Tenses", "Master time forms", clas, subject))
                        list.add(ChapterModel("Verb Agreement", "Match subject & verb", clas, subject))
                        list.add(ChapterModel("Voice", "Active vs. Passive", clas, subject))
                        list.add(ChapterModel("Speech", "Direct to indirect", clas, subject))
                        list.add(ChapterModel("Clauses", "Linking ideas", clas, subject))
                        list.add(ChapterModel("Prepositions", "Perfect position words", clas, subject))
                        list.add(ChapterModel("Determiners", "Specify & quantify", clas, subject))
                        list.add(ChapterModel("Transformation", "Change sentence forms", clas, subject))
                    }

                    // For Classes 4, 3, 2, and 1
                    "Class 4", "Class 3", "Class 2", "Class 1" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Tenses", "Understanding basic time forms", clas, subject))
                        list.add(ChapterModel("Nouns", "Naming words", clas, subject))
                        list.add(ChapterModel("Pronouns", "Replace nouns", clas, subject))
                        list.add(ChapterModel("Verbs", "Action words", clas, subject))
                        list.add(ChapterModel("Adjectives", "Describing words", clas, subject))
                        list.add(ChapterModel("Articles", "A, An, The", clas, subject))
                        list.add(ChapterModel("Prepositions", "Position words", clas, subject))
                        list.add(ChapterModel("Conjunctions", "Joining words", clas, subject))
                    }

                    else -> arrayOf("No Chapter Available")
                }
            }

            "Social Science" -> {
                when (clas) {
                    "Class 10" -> {
                        // History Chapters
                        list.add(ChapterModel("All Chapters", "Understand the past", clas, subject))
                        list.add(ChapterModel("All History Chapters", "Understand the past", clas, subject))
                        list.add(ChapterModel("The Rise of Nationalism in Europe", "Formation of modern nations", clas, subject))
                        list.add(ChapterModel("Nationalism in India", "Struggles for independence", clas, subject))
                        list.add(ChapterModel("The Making of a Global World", "Globalization and its history", clas, subject))
                        list.add(ChapterModel("The Age of Industrialization", "Transformation through industry", clas, subject))
                        list.add(ChapterModel("Print Culture and the Modern World", "Communication revolution", clas, subject))

                        // Geography Chapters
                        list.add(ChapterModel("All Geography Chapters", "Discover the world", clas, subject))
                        list.add(ChapterModel("Resources & Development", "Sustaining resources wisely", clas, subject))
                        list.add(ChapterModel("Forest & Wildlife Resources", "Protecting biodiversity", clas, subject))
                        list.add(ChapterModel("Water Resources", "Harnessing the lifeline", clas, subject))
                        list.add(ChapterModel("Agriculture", "The backbone of our economy", clas, subject))
                        list.add(ChapterModel("Minerals & Energy Resources", "Powering progress", clas, subject))
                        list.add(ChapterModel("Manufacturing Industries", "The wheels of growth", clas, subject))
                        list.add(ChapterModel("Lifelines of National Economy", "Connecting the nation", clas, subject))

                        // Economics Chapters
                        list.add(ChapterModel("All Economics Chapters", "Economic insights", clas, subject))
                        list.add(ChapterModel("Development", "Understanding progress", clas, subject))
                        list.add(ChapterModel("Sectors of the Indian Economy", "Primary, secondary, and tertiary sectors", clas, subject))
                        list.add(ChapterModel("Money and Credit", "Financial systems explained", clas, subject))
                        list.add(ChapterModel("Globalisation and the Indian Economy", "Impact of globalization", clas, subject))
                        list.add(ChapterModel("Consumer Rights", "Awareness for all", clas, subject))

                        // Political Science Chapters
                        list.add(ChapterModel("All Political Science Chapters", "Understand governance", clas, subject))
                        list.add(ChapterModel("Power Sharing", "Sharing responsibilities", clas, subject))
                        list.add(ChapterModel("Federalism", "Division of power", clas, subject))
                        list.add(ChapterModel("Democracy and Diversity", "Unity in diversity", clas, subject))
                        list.add(ChapterModel("Gender, Religion, and Caste", "Societal structures", clas, subject))
                        list.add(ChapterModel("Popular Struggles and Movements", "Democratic processes", clas, subject))
                        list.add(ChapterModel("Political Parties", "Pillars of democracy", clas, subject))
                        list.add(ChapterModel("Outcomes of Democracy", "Democracy in practice", clas, subject))
                        list.add(ChapterModel("Challenges to Democracy", "Overcoming obstacles", clas, subject))
                    }

                    "Class 8" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Resources", "Utilizing earth's wealth", clas, subject))
                        list.add(ChapterModel("Agriculture", "Growing our future", clas, subject))
                        list.add(ChapterModel("Minerals & Power", "Energy & elements", clas, subject))
                        list.add(ChapterModel("Industries", "Shaping the world", clas, subject))
                        list.add(ChapterModel("Human Resources", "People as assets", clas, subject))
                    }
                    "Class 7" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Environment", "Our surroundings", clas, subject))
                        list.add(ChapterModel("Earth's Core", "What's inside?", clas, subject))
                        list.add(ChapterModel("Earth's Changes", "How it evolves", clas, subject))
                        list.add(ChapterModel("Air", "Atmosphere & weather", clas, subject))
                        list.add(ChapterModel("Water", "Oceans & rivers", clas, subject))
                        list.add(ChapterModel("Wildlife", "Nature's inhabitants", clas, subject))
                    }
                    "Class 6" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Solar System", "Exploring space", clas, subject))
                        list.add(ChapterModel("Globe", "Mapping our world", clas, subject))
                        list.add(ChapterModel("Earth's Motion", "Rotations & revolutions", clas, subject))
                        list.add(ChapterModel("Maps", "Reading the world", clas, subject))
                        list.add(ChapterModel("Earth's Domains", "Land, water, air", clas, subject))
                    }
                    "Class 5" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Our Earth", "Basic geography", clas, subject))
                        list.add(ChapterModel("Continents", "Explore the seven", clas, subject))
                        list.add(ChapterModel("Oceans", "Vast water bodies", clas, subject))
                        list.add(ChapterModel("India", "Our country", clas, subject))
                        list.add(ChapterModel("Environment", "Protecting nature", clas, subject))
                    }
                    "Class 4" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Our Surroundings", "Immediate environment", clas, subject))
                        list.add(ChapterModel("Landforms", "Hills, plains, rivers", clas, subject))
                        list.add(ChapterModel("Weather", "Daily atmospheric changes", clas, subject))
                        list.add(ChapterModel("Seasons", "Understanding time cycles", clas, subject))
                        list.add(ChapterModel("Community Helpers", "People who serve", clas, subject))
                    }
                    "Class 3" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("My Family", "Importance of family", clas, subject))
                        list.add(ChapterModel("My Neighborhood", "Local community", clas, subject))
                        list.add(ChapterModel("Transport", "Moving people & goods", clas, subject))
                        list.add(ChapterModel("Communication", "Sharing information", clas, subject))
                        list.add(ChapterModel("Maps", "Finding directions", clas, subject))
                    }
                    "Class 2" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Our Home", "Where we live", clas, subject))
                        list.add(ChapterModel("Places Around Us", "Local landmarks", clas, subject))
                        list.add(ChapterModel("Work and Play", "Daily activities", clas, subject))
                        list.add(ChapterModel("Weather", "Hot, cold, rainy", clas, subject))
                        list.add(ChapterModel("Safety Rules", "Staying safe", clas, subject))
                    }
                    "Class 1" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Myself", "Knowing about me", clas, subject))
                        list.add(ChapterModel("My School", "Learning space", clas, subject))
                        list.add(ChapterModel("My Friends", "Social connections", clas, subject))
                        list.add(ChapterModel("My Neighborhood", "Nearby places", clas, subject))
                        list.add(ChapterModel("Good Manners", "Behavioral values", clas, subject))
                    }
                }
            }


            "Science" -> {
                when (clas) {
                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Sharpen your skills", clas, subject))
                        list.add(ChapterModel("Chemical Reactions", "Transformations in chemistry", clas, subject))
                        list.add(ChapterModel("Acids and Bases", "Discover their nature", clas, subject))
                        list.add(ChapterModel("Metals and Non-metals", "Explore their properties", clas, subject))
                        list.add(ChapterModel("Carbon Compounds", "Organic chemistry basics", clas, subject))
                        list.add(ChapterModel("Periodic Table", "Organizing elements", clas, subject))
                        list.add(ChapterModel("Life Processes", "Understand vital functions", clas, subject))
                        list.add(ChapterModel("Control & Coordination", "Nervous and hormonal systems", clas, subject))
                        list.add(ChapterModel("Reproduction", "Reproduction in detail", clas, subject))
                        list.add(ChapterModel("Heredity & Evolution", "Genetics and change over time", clas, subject))
                        list.add(ChapterModel("Light", "Reflection and refraction", clas, subject))
                        list.add(ChapterModel("Human Eye", "Vision and dispersion", clas, subject))
                        list.add(ChapterModel("Electricity", "Flow of charges", clas, subject))
                        list.add(ChapterModel("Magnetic Effects", "Electromagnetic concepts", clas, subject))
                        list.add(ChapterModel("Energy Sources", "Renewable and non-renewable", clas, subject))
                    }

                    "Class 9" -> {
                        list.add(ChapterModel("All Chapter", "Sharpen your skills", clas, subject))
                        list.add(ChapterModel("Matter", "Understand physical states", clas, subject))
                        list.add(ChapterModel("Atoms", "Basic building blocks", clas, subject))
                        list.add(ChapterModel("Atomic Structure", "Discover atomic models", clas, subject))
                        list.add(ChapterModel("Cell", "Explore cells", clas, subject))
                        list.add(ChapterModel("Tissues", "Different cell groups", clas, subject))
                        list.add(ChapterModel("Diversity", "Variety of life forms", clas, subject))
                        list.add(ChapterModel("Motion", "Study of movement", clas, subject))
                        list.add(ChapterModel("Force", "Physics in action", clas, subject))
                        list.add(ChapterModel("Gravitation", "Universal force", clas, subject))
                        list.add(ChapterModel("Work & Energy", "Concepts of physics", clas, subject))
                        list.add(ChapterModel("Sound", "Waves, vibrations, and energy", clas, subject))
                        list.add(ChapterModel("Health", "Learn about diseases", clas, subject))
                        list.add(ChapterModel("Resources", "Earth's treasures", clas, subject))
                    }


                    "Class 8" -> {
                        list.add(ChapterModel("All Chapter", "Sharpen your skills", clas, subject))
                        list.add(ChapterModel("Crop Production", "Modern farming techniques", clas, subject))
                        list.add(ChapterModel("Microorganisms", "Explore tiny life forms", clas, subject))
                        list.add(ChapterModel("Fibers & Plastics", "Everyday materials explained", clas, subject))
                        list.add(ChapterModel("Metals & Non-metals", "Discover their properties", clas, subject))
                        list.add(ChapterModel("Cell Structure", "Life's building blocks", clas, subject))
                        list.add(ChapterModel("Animal Reproduction", "How species continue", clas, subject))
                        list.add(ChapterModel("Force & Pressure", "Power behind motion", clas, subject))
                        list.add(ChapterModel("Sound", "Waves, vibrations, and energy", clas, subject))
                    }
                    "Class 7" -> {
                        list.add(ChapterModel("All Chapter", "Boost your learning", clas, subject))
                        list.add(ChapterModel("Plant Nutrition", "How plants nourish themselves", clas, subject))
                        list.add(ChapterModel("Animal Nutrition", "Feeding for survival", clas, subject))
                        list.add(ChapterModel("Fiber to Fabric", "Journey from threads", clas, subject))
                        list.add(ChapterModel("Heat", "Understand temperature changes", clas, subject))
                        list.add(ChapterModel("Acids & Bases", "Chemicals in daily life", clas, subject))
                        list.add(ChapterModel("Respiration", "Energy from breathing", clas, subject))
                        list.add(ChapterModel("Transportation", "Life's internal movement", clas, subject))
                        list.add(ChapterModel("Motion & Time", "Speed and its measure", clas, subject))
                    }
                    "Class 6" -> {
                        list.add(ChapterModel("All Chapter", "Kickstart your science journey", clas, subject))
                        list.add(ChapterModel("Food Source", "Where our meals begin", clas, subject))
                        list.add(ChapterModel("Food Components", "What meals are made of", clas, subject))
                        list.add(ChapterModel("Separation", "Purify with techniques", clas, subject))
                        list.add(ChapterModel("Changes", "Explore transformations", clas, subject))
                        list.add(ChapterModel("Plant Survival", "How plants adapt", clas, subject))
                        list.add(ChapterModel("Distance", "Measuring the world", clas, subject))
                        list.add(ChapterModel("Light & Shadows", "Science behind sight", clas, subject))
                        list.add(ChapterModel("Electricity", "Powering our lives", clas, subject))
                    }
                    "Class 5" -> {
                        list.add(ChapterModel("All Chapter", "Explore science basics", clas, subject))
                        list.add(ChapterModel("Living Things", "Plants and animals", clas, subject))
                        list.add(ChapterModel("Plant Parts", "Understanding the structure", clas, subject))
                        list.add(ChapterModel("Animal Homes", "Habitats and shelters", clas, subject))
                        list.add(ChapterModel("Materials & Their Uses", "Exploring everyday materials", clas, subject))
                        list.add(ChapterModel("States of Matter", "Solid, liquid, and gas", clas, subject))
                        list.add(ChapterModel("Water & Air", "Life's essential resources", clas, subject))
                        list.add(ChapterModel("Soil", "Earth's surface", clas, subject))
                        list.add(ChapterModel("Force & Energy", "Understanding power", clas, subject))
                    }
                    "Class 4" -> {
                        list.add(ChapterModel("All Chapter", "Begin your science adventure", clas, subject))
                        list.add(ChapterModel("Plants Around Us", "Nature's green wonder", clas, subject))
                        list.add(ChapterModel("Animals Around Us", "Diverse creatures", clas, subject))
                        list.add(ChapterModel("Water Cycle", "Nature's recycling", clas, subject))
                        list.add(ChapterModel("Air & Breathing", "The role of air in life", clas, subject))
                        list.add(ChapterModel("Earth & Sky", "Exploring the world above", clas, subject))
                        list.add(ChapterModel("Weather", "Understanding the environment", clas, subject))
                        list.add(ChapterModel("Magnets", "Force of attraction", clas, subject))
                        list.add(ChapterModel("Simple Machines", "Everyday tools explained", clas, subject))
                    }
                    "Class 3" -> {
                        list.add(ChapterModel("All Chapter", "Start exploring science", clas, subject))
                        list.add(ChapterModel("Living & Non-Living", "Understanding life", clas, subject))
                        list.add(ChapterModel("Plants & Trees", "Life forms around us", clas, subject))
                        list.add(ChapterModel("Animals & Insects", "Diversity of life", clas, subject))
                        list.add(ChapterModel("Water & Weather", "Nature’s cycles", clas, subject))
                        list.add(ChapterModel("Sky & Stars", "Explore the universe", clas, subject))
                        list.add(ChapterModel("Rocks & Minerals", "Earth’s building blocks", clas, subject))
                        list.add(ChapterModel("Electricity Basics", "Fundamentals of power", clas, subject))
                        list.add(ChapterModel("Forces Around Us", "Exploring force", clas, subject))
                    }
                    "Class 2" -> {
                        list.add(ChapterModel("All Chapter", "Science for young minds", clas, subject))
                        list.add(ChapterModel("Our Body", "Basic parts and senses", clas, subject))
                        list.add(ChapterModel("Plants & Animals", "Understanding life forms", clas, subject))
                        list.add(ChapterModel("Weather & Seasons", "Nature’s patterns", clas, subject))
                        list.add(ChapterModel("Air & Water", "Life's essentials", clas, subject))
                        list.add(ChapterModel("Types of Materials", "Understanding matter", clas, subject))
                        list.add(ChapterModel("Light & Shadows", "Exploring light", clas, subject))
                        list.add(ChapterModel("Magnets", "Magnetic forces", clas, subject))
                        list.add(ChapterModel("Simple Experiments", "Hands-on learning", clas, subject))
                    }
                    "Class 1" -> {
                        list.add(ChapterModel("All Chapter", "Fun with science", clas, subject))
                        list.add(ChapterModel("My Body", "Simple body parts", clas, subject))
                        list.add(ChapterModel("Plants & Trees", "Life around us", clas, subject))
                        list.add(ChapterModel("Animals & Birds", "Understanding animals", clas, subject))
                        list.add(ChapterModel("Water", "Life’s necessity", clas, subject))
                        list.add(ChapterModel("Sky & Earth", "The world around us", clas, subject))
                        list.add(ChapterModel("Weather", "Day and night", clas, subject))
                        list.add(ChapterModel("Basic Forces", "Introduction to forces", clas, subject))
                        list.add(ChapterModel("Simple Machines", "Tools we use", clas, subject))
                    }
                }
            }

            "Urdu"->{
               when(clas){
                   "Class 9" -> {
                       list.add(ChapterModel("All Chapter", "اپنی اردو مہارت کو بہتر بنائیں", clas, subject))
                       list.add(ChapterModel("قاضی کی کہانی", "دانائی اور انصاف کی کہانی", clas, subject))
                       list.add(ChapterModel("شہیدوں کی دنیا", "شہداء سے حوصلہ افزائی", clas, subject))
                       list.add(ChapterModel("مٹی کی محبت", "اپنی زمین سے تعلق", clas, subject))
                       list.add(ChapterModel("خدا کی کرشمہ", "زندگی میں الہی برکتیں", clas, subject))
                       list.add(ChapterModel("نظامِ طبیعت", "فطرت کے نظام کو سمجھنا", clas, subject))
                       list.add(ChapterModel("علم کی برکت", "علم اور سیکھنے کی اہمیت", clas, subject))
                       list.add(ChapterModel("حمد", "اللہ کی تعریف", clas, subject))
                       list.add(ChapterModel("نعت", "نبی کی شان میں اشعار", clas, subject))
                       list.add(ChapterModel("غزل", "روایتی اردو شاعری", clas, subject))
                       list.add(ChapterModel("نظم", "قومی اور اخلاقی موضوعات", clas, subject))
                   }
                   "Class 10" -> {
                       list.add(ChapterModel("All Chapter", "اپنی اردو مہارت کو بہتر بنائیں", clas, subject))
                       list.add(ChapterModel("تہذیب اور تعمیر", "ثقافت سے معاشرے کی ترقی", clas, subject))
                       list.add(ChapterModel("سوانح سر سید", "سر سید احمد خان کی زندگی", clas, subject))
                       list.add(ChapterModel("مضامین اور رسائل", "مشہور اردو مصنفین کے مضامین", clas, subject))
                       list.add(ChapterModel("سائنس اور اسلام", "اسلام میں سائنسی تعلیم کی اہمیت", clas, subject))
                       list.add(ChapterModel("زبان اور تعلیم", "تعلیم میں اردو کا کردار", clas, subject))
                       list.add(ChapterModel("تصورِ حیات", "زندگی کے فلسفے پر روشنی", clas, subject))
                       list.add(ChapterModel("مرثیہ", "قربانی اور غم کے موضوع پر", clas, subject))
                       list.add(ChapterModel("نظم", "سماجی اصلاح اور حب الوطنی پر اشعار", clas, subject))
                       list.add(ChapterModel("غزل", "علامہ اقبال اور دیگر شعرا کے کلام", clas, subject))
                       list.add(ChapterModel("رباعیات", "اخلاقی اور روحانی سبق", clas, subject))
                   }

               }

            }

        }




        // Initialize the adapter with the list
        adapter = ChapterAdapter(requireContext(), list,testfrag)
        // Set the adapter to the RecyclerView
        binding.chapterRecyclerview.adapter = adapter
    }
}