package com.example.nafisquaisarcoachingcenter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.nafisquaisarcoachingcenter.R
import com.example.nafisquaisarcoachingcenter.adapter.ChapterAdapter
import com.example.nafisquaisarcoachingcenter.databinding.FragmentChapterBinding
import com.example.nafisquaisarcoachingcenter.model.ChapterModel

class ChapterFragment (private val subject: String?, private val clas:String?) : Fragment() {
    private lateinit var binding: FragmentChapterBinding
    private lateinit var list:ArrayList<ChapterModel>
    private lateinit var adapter: ChapterAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentChapterBinding.inflate(inflater,container,false)



        LoadData()

        return binding.root
    }

    private fun LoadData() {
        list =ArrayList()

        when (subject) {
            "Physics" -> {
                when (clas) {
                    "Class 12" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Electrostatics", "Test your static skills", clas, subject))
                        list.add(ChapterModel("Current Flow", "Master electricity today", clas, subject))
                        list.add(ChapterModel("Magnetism", "Unveil magnetic mysteries", clas, subject))
                        list.add(ChapterModel("Induction", "Induce your success", clas, subject))
                        list.add(ChapterModel("AC Circuits", "Navigate AC wonders", clas, subject))
                        list.add(ChapterModel("EM Waves", "Ride the EM waves", clas, subject))
                        list.add(ChapterModel("Optics", "Explore light's secrets", clas, subject))
                        list.add(ChapterModel("Dual Nature", "Quantum meets reality", clas, subject))
                        list.add(ChapterModel("Atoms", "Atomic discoveries await", clas, subject))
                        list.add(ChapterModel("Nuclei", "Unlock nuclear power", clas, subject))
                        list.add(ChapterModel("Semiconductors", "Crack semiconductor codes", clas, subject))
                    }
                    "Class 11" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Physical World", "Explore physics universe", clas, subject))
                        list.add(ChapterModel("Vectors", "Vector your victory", clas, subject))
                        list.add(ChapterModel("Measurements", "Precision meets practice", clas, subject))
                        list.add(ChapterModel("Straight Line", "Linear motion mastery", clas, subject))
                        list.add(ChapterModel("Motion Plane", "Plane motion precision", clas, subject))
                        list.add(ChapterModel("Motion Laws", "Rule the motion laws", clas, subject))
                        list.add(ChapterModel("Energy & Power", "Harness power & energy", clas, subject))
                        list.add(ChapterModel("Rotation", "Conquer rotational forces", clas, subject))
                        list.add(ChapterModel("Gravitation", "Gravity in focus", clas, subject))
                        list.add(ChapterModel("Solid Mechanics", "Solid strength tests", clas, subject))
                        list.add(ChapterModel("Fluid Dynamics", "Flow with the fluids", clas, subject))
                        list.add(ChapterModel("Thermal Props", "Heat up your knowledge", clas, subject))
                        list.add(ChapterModel("Thermodynamics", "Thermal energy tactics", clas, subject))
                        list.add(ChapterModel("Kinetic Theory", "Master particle motion", clas, subject))
                        list.add(ChapterModel("Oscillations", "Vibrate to success", clas, subject))
                        list.add(ChapterModel("Waves", "Wave through challenges", clas, subject))
                    }
                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Electricity", "Power up your knowledge", clas, subject))
                        list.add(ChapterModel("Magnetism", "Attract magnetic insights", clas, subject))
                        list.add(ChapterModel("Light", "Shine with optics", clas, subject))
                        list.add(ChapterModel("Human Eye", "See the science", clas, subject))
                        list.add(ChapterModel("Energy", "Explore energy sources", clas, subject))
                    }
                    "Class 9" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Motion", "Move ahead in motion", clas, subject))
                        list.add(ChapterModel("Force & Motion", "Force your success", clas, subject))
                        list.add(ChapterModel("Gravitation", "Gravity takes center stage", clas, subject))
                        list.add(ChapterModel("Energy", "Energy exploration starts", clas, subject))
                        list.add(ChapterModel("Sound", "Tune into sound", clas, subject))
                    }
                    "Class 8" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Force & Pressure", "Push through challenges", clas, subject))
                        list.add(ChapterModel("Friction", "Friction brings action", clas, subject))
                        list.add(ChapterModel("Sound", "Echo your success", clas, subject))
                        list.add(ChapterModel("Electric Effects", "Spark your curiosity", clas, subject))
                        list.add(ChapterModel("Light", "Light your path", clas, subject))
                        list.add(ChapterModel("Stars & Solar", "Reach for the stars", clas, subject))
                    }
                    "Class 7" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Heat", "Feel the heat", clas, subject))
                        list.add(ChapterModel("Winds & Cyclones", "Weather the storm", clas, subject))
                        list.add(ChapterModel("Motion & Time", "Time your success", clas, subject))
                        list.add(ChapterModel("Electric Effects", "Power up your knowledge", clas, subject))
                        list.add(ChapterModel("Light", "Illuminate your understanding", clas, subject))
                    }
                    "Class 6" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Motion & Distance", "Measure your motion", clas, subject))
                        list.add(ChapterModel("Light & Shadows", "Cast light on learning", clas, subject))
                        list.add(ChapterModel("Electricity & Circuits", "Get charged with circuits", clas, subject))
                        list.add(ChapterModel("Fun Magnets", "Magnetize your curiosity", clas, subject))
                    }
                }
            }


            "Chemistry" -> {
                when (clas) {
                    "Class 12" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Solid State", "Crystallize your knowledge", clas, subject))
                        list.add(ChapterModel("Solutions", "Mix it up!", clas, subject))
                        list.add(ChapterModel("Electrochemistry", "Charge up your skills", clas, subject))
                        list.add(ChapterModel("Chem Kinetics", "Speed through reactions", clas, subject))
                        list.add(ChapterModel("Surface Chem", "Surface-level science", clas, subject))
                        list.add(ChapterModel("Metal Extraction", "Dig into elements", clas, subject))
                        list.add(ChapterModel("p-Block", "Unlock p-block secrets", clas, subject))
                        list.add(ChapterModel("d/f-Block", "Discover transition elements", clas, subject))
                        list.add(ChapterModel("Coord Compounds", "Complex yet fun!", clas, subject))
                        list.add(ChapterModel("Haloalkanes", "React with halogens", clas, subject))
                        list.add(ChapterModel("Alcohols", "Sip on chemistry", clas, subject))
                        list.add(ChapterModel("Aldehydes", "Aldehydes unlocked!", clas, subject))
                        list.add(ChapterModel("Nitro Compounds", "Nail nitrogen knowledge", clas, subject))
                        list.add(ChapterModel("Biomolecules", "Biology meets chemistry", clas, subject))
                        list.add(ChapterModel("Polymers", "Link the chains!", clas, subject))
                        list.add(ChapterModel("Daily Chem", "Chemistry in life", clas, subject))
                    }
                    "Class 11" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Chem Basics", "Foundation of reactions", clas, subject))
                        list.add(ChapterModel("Atom Structure", "Dive into atoms", clas, subject))
                        list.add(ChapterModel("Element Trends", "Periodic table fun", clas, subject))
                        list.add(ChapterModel("Chem Bonds", "Molecules bond better", clas, subject))
                        list.add(ChapterModel("Matter States", "Solid, liquid, gas!", clas, subject))
                        list.add(ChapterModel("Thermodynamics", "Energy transformations", clas, subject))
                        list.add(ChapterModel("Equilibrium", "Balance the reactions", clas, subject))
                        list.add(ChapterModel("Redox", "Oxidation meets reduction", clas, subject))
                        list.add(ChapterModel("Hydrogen", "Hydrogen highlights", clas, subject))
                        list.add(ChapterModel("s-Block", "Alkali & Alkaline Earth", clas, subject))
                        list.add(ChapterModel("p-Block", "Learn p-block elements", clas, subject))
                        list.add(ChapterModel("Org Chem Basics", "Organic chemistry start", clas, subject))
                        list.add(ChapterModel("Hydrocarbons", "Fuel your knowledge", clas, subject))
                        list.add(ChapterModel("Env Chem", "Green chemistry insights", clas, subject))
                    }
                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Chem Reactions", "React and learn", clas, subject))
                        list.add(ChapterModel("Acids & Bases", "pH matters!", clas, subject))
                        list.add(ChapterModel("Metals & Non-metals", "Elemental differences", clas, subject))
                        list.add(ChapterModel("Carbon Compounds", "Carbon-based wonders", clas, subject))
                        list.add(ChapterModel("Element Classification", "Periodic table tricks", clas, subject))
                    }
                    "Class 9" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Matter Around", "Explore matter's magic", clas, subject))
                        list.add(ChapterModel("Pure Matter", "Purity in chemistry", clas, subject))
                        list.add(ChapterModel("Atoms & Molecules", "Small but mighty", clas, subject))
                        list.add(ChapterModel("Atom Structure", "Atoms unveiled", clas, subject))
                        list.add(ChapterModel("Life Unit", "Chemistry of life", clas, subject))
                    }
                    "Class 8" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Synthetic Fibres", "Fabric of chemistry", clas, subject))
                        list.add(ChapterModel("Metals & Non-metals", "Material science", clas, subject))
                        list.add(ChapterModel("Coal & Petroleum", "Fossil fuel facts", clas, subject))
                        list.add(ChapterModel("Combustion", "Burn with knowledge", clas, subject))
                    }
                    "Class 7" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Acids & Bases", "Taste chemistry", clas, subject))
                        list.add(ChapterModel("Chem Changes", "Reactions in action", clas, subject))
                        list.add(ChapterModel("Fibre to Fabric", "From thread to textile", clas, subject))
                        list.add(ChapterModel("Water", "Essential chemistry", clas, subject))
                        list.add(ChapterModel("Wastewater", "Clean science", clas, subject))
                    }
                    "Class 6" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Fibre to Fabric", "Weave chemistry", clas, subject))
                        list.add(ChapterModel("Material Sorting", "Group and learn", clas, subject))
                        list.add(ChapterModel("Substance Separation", "Separate and explore", clas, subject))
                        list.add(ChapterModel("Changes", "Transforming matter", clas, subject))
                        list.add(ChapterModel("Water", "Liquid learning", clas, subject))
                    }
                }
            }


            "Math" -> {
                when (clas) {
                    "Class 12" -> {
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Relations", "Master Sets & Functions", clas, subject))
                        list.add(ChapterModel("Inverse Trigonometry", "Unlock Trig Secrets", clas, subject))
                        list.add(ChapterModel("Matrices", "Matrix Magic Unveiled", clas, subject))
                        list.add(ChapterModel("Determinants", "Solve Systems Swiftly", clas, subject))
                        list.add(ChapterModel("Continuity", "Smooth Calculus Flow", clas, subject))
                        list.add(ChapterModel("Derivatives", "Find Slopes Fast", clas, subject))
                        list.add(ChapterModel("Integrals", "Area Under Curves", clas, subject))
                        list.add(ChapterModel("Applications of Integrals", "Real-World Integrals", clas, subject))
                        list.add(ChapterModel("Differentials", "Change & Rates", clas, subject))
                        list.add(ChapterModel("Vectors", "Navigate 3D Space", clas, subject))
                        list.add(ChapterModel("3D Geometry", "Shapes in Space", clas, subject))
                        list.add(ChapterModel("Linear Programming", "Optimize & Solve", clas, subject))
                        list.add(ChapterModel("Probability", "Predict the Future", clas, subject))
                    }
                    "Class 11" -> {
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Sets", "Begin with Basics", clas, subject))
                        list.add(ChapterModel("Functions", "Map Inputs to Outputs", clas, subject))
                        list.add(ChapterModel("Trigonometric Functions", "Angles & Ratios", clas, subject))
                        list.add(ChapterModel("Mathematical Induction", "Prove with Logic", clas, subject))
                        list.add(ChapterModel("Complex Numbers", "Imaginary Made Real", clas, subject))
                        list.add(ChapterModel("Inequalities", "Compare & Contrast", clas, subject))
                        list.add(ChapterModel("Permutations", "Count the Ways", clas, subject))
                        list.add(ChapterModel("Binomial Theorem", "Expand with Ease", clas, subject))
                        list.add(ChapterModel("Sequences", "Patterns in Numbers", clas, subject))
                        list.add(ChapterModel("Straight Lines", "Graph Linear Equations", clas, subject))
                        list.add(ChapterModel("Conic Sections", "Explore Curved Paths", clas, subject))
                        list.add(ChapterModel("Introduction to 3D Geometry", "Visualize Geometry", clas, subject))
                        list.add(ChapterModel("Limits", "Approach Infinity", clas, subject))
                        list.add(ChapterModel("Reasoning", "Logical Connections", clas, subject))
                        list.add(ChapterModel("Statistics", "Data Analysis Basics", clas, subject))
                        list.add(ChapterModel("Probability", "Chance & Likelihood", clas, subject))
                    }
                    "Class 10" -> {
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Real Numbers", "Explore Infinite Sets", clas, subject))
                        list.add(ChapterModel("Polynomials", "Polynomial Fun", clas, subject))
                        list.add(ChapterModel("Linear Equations", "Solve Equations Fast", clas, subject))
                        list.add(ChapterModel("Quadratic Equations", "Roots of Equations", clas, subject))
                        list.add(ChapterModel("Progressions", "Sum the Series", clas, subject))
                        list.add(ChapterModel("Triangles", "Prove & Apply", clas, subject))
                        list.add(ChapterModel("Coordinate Geometry", "Map Points Easily", clas, subject))
                        list.add(ChapterModel("Trigonometry", "Angle Relations", clas, subject))
                        list.add(ChapterModel("Applications of Trigonometry", "Real-World Trig", clas, subject))
                        list.add(ChapterModel("Circles", "Master Round Shapes", clas, subject))
                        list.add(ChapterModel("Construction", "Geometric Drawings", clas, subject))
                        list.add(ChapterModel("Areas", "Measure Shapes Precisely", clas, subject))
                        list.add(ChapterModel("Volumes", "Calculate 3D Space", clas, subject))
                        list.add(ChapterModel("Statistics", "Handle Data Smartly", clas, subject))
                        list.add(ChapterModel("Probability", "Predict Outcomes", clas, subject))
                    }
                    "Class 9" -> {
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Number Systems", "Explore Number Sets", clas, subject))
                        list.add(ChapterModel("Polynomials", "Play with Equations", clas, subject))
                        list.add(ChapterModel("Coordinate Geometry", "Graph with Precision", clas, subject))
                        list.add(ChapterModel("Linear Equations", "Equations Made Easy", clas, subject))
                        list.add(ChapterModel("Euclid's Geometry", "Foundations of Geometry", clas, subject))
                        list.add(ChapterModel("Lines and Angles", "Understand Angles", clas, subject))
                        list.add(ChapterModel("Triangles", "Prove with Logic", clas, subject))
                        list.add(ChapterModel("Quadrilaterals", "Explore Four Sides", clas, subject))
                        list.add(ChapterModel("Areas", "Calculate Areas", clas, subject))
                        list.add(ChapterModel("Circles", "Understand Circular Shapes", clas, subject))
                        list.add(ChapterModel("Construction", "Draw with Accuracy", clas, subject))
                        list.add(ChapterModel("Heron's Formula", "Area without Height", clas, subject))
                        list.add(ChapterModel("Surface Area", "3D Shape Areas", clas, subject))
                        list.add(ChapterModel("Statistics", "Analyze Data", clas, subject))
                        list.add(ChapterModel("Probability", "Predict Possibilities", clas, subject))
                    }
                    "Class 8" -> {
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Rational Numbers", "Fractional Numbers", clas, subject))
                        list.add(ChapterModel("Linear Equations", "Solve Simple Equations", clas, subject))
                        list.add(ChapterModel("Quadrilaterals", "Study Four Sides", clas, subject))
                        list.add(ChapterModel("Practical Geometry", "Construct Shapes", clas, subject))
                        list.add(ChapterModel("Data Handling", "Organize & Interpret", clas, subject))
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
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Integers", "Understand Whole Numbers", clas, subject))
                        list.add(ChapterModel("Fractions", "Work with Parts", clas, subject))
                        list.add(ChapterModel("Data Handling", "Analyze & Interpret", clas, subject))
                        list.add(ChapterModel("Simple Equations", "Solve Basic Equations", clas, subject))
                        list.add(ChapterModel("Lines and Angles", "Basic Geometry", clas, subject))
                        list.add(ChapterModel("Triangles", "Properties & Proofs", clas, subject))
                        list.add(ChapterModel("Congruence", "Match Shapes Perfectly", clas, subject))
                        list.add(ChapterModel("Quantities", "Compare and Measure", clas, subject))
                        list.add(ChapterModel("Rational Numbers", "Explore Rationality", clas, subject))
                        list.add(ChapterModel("Practical Geometry", "Draw with Precision", clas, subject))
                        list.add(ChapterModel("Perimeter", "Measure Boundaries", clas, subject))
                        list.add(ChapterModel("Algebraic Expressions", "Simplify Expressions", clas, subject))
                        list.add(ChapterModel("Exponents", "Use Powers Wisely", clas, subject))
                        list.add(ChapterModel("Symmetry", "Mirror Perfection", clas, subject))
                        list.add(ChapterModel("Solid Shapes", "Visualize 3D Objects", clas, subject))
                    }
                    "Class 6" -> {
                        list.add(ChapterModel("All Chapters", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Numbers", "Understand Big Numbers", clas, subject))
                        list.add(ChapterModel("Whole Numbers", "Counting Beyond Zero", clas, subject))
                        list.add(ChapterModel("Play with Numbers", "Explore Number Tricks", clas, subject))
                        list.add(ChapterModel("Geometry", "Understand Shapes", clas, subject))
                        list.add(ChapterModel("Shapes", "Recognize Basic Shapes", clas, subject))
                        list.add(ChapterModel("Integers", "Learn About Negative Numbers", clas, subject))
                        list.add(ChapterModel("Fractions", "Divide the Whole", clas, subject))
                        list.add(ChapterModel("Decimals", "Handle Decimal Points", clas, subject))
                        list.add(ChapterModel("Data Handling", "Organize & Read Data", clas, subject))
                        list.add(ChapterModel("Mensuration", "Measure Lengths & Areas", clas, subject))
                        list.add(ChapterModel("Ratio", "Compare Two Values", clas, subject))
                        list.add(ChapterModel("Symmetry", "Understand Reflection", clas, subject))
                        list.add(ChapterModel("Geometry Basics", "Draw Simple Shapes", clas, subject))
                    }
                }
            }


            "Bio" -> {
                when (clas) {
                    "Class 10" -> {
                        list.add(ChapterModel("All Chapter", "Improve Your Ability", clas, subject))
                        list.add(ChapterModel("Life Cycle", "Vital processes explained", clas, subject))
                        list.add(ChapterModel("Coordination", "How bodies respond", clas, subject))
                        list.add(ChapterModel("Reproduction", "Creation of new life", clas, subject))
                        list.add(ChapterModel("Heredity", "Traits across generations", clas, subject))
                        list.add(ChapterModel("Environment", "Impact on nature", clas, subject))
                        list.add(ChapterModel("Resources", "Managing natural wealth", clas, subject))
                    }
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
                    "Class 12", "Class 11", "Class 10", "Class 9", "Class 8", "Class 7", "Class 6" -> {
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
                }
            }
            "Social Science" -> {
                when (clas) {
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
                }
            }

            "Science" -> {
                when (clas) {
                    "Class 8" -> {
                        list.add(ChapterModel("All Chapters", "Sharpen your skills", clas, subject))
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
                        list.add(ChapterModel("All Chapters", "Boost your learning", clas, subject))
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
                        list.add(ChapterModel("All Chapters", "Kickstart your science journey", clas, subject))
                        list.add(ChapterModel("Food Source", "Where our meals begin", clas, subject))
                        list.add(ChapterModel("Food Components", "What meals are made of", clas, subject))
                        list.add(ChapterModel("Separation", "Purify with techniques", clas, subject))
                        list.add(ChapterModel("Changes", "Explore transformations", clas, subject))
                        list.add(ChapterModel("Plant Survival", "How plants adapt", clas, subject))
                        list.add(ChapterModel("Distance", "Measuring the world", clas, subject))
                        list.add(ChapterModel("Light & Shadows", "Science behind sight", clas, subject))
                        list.add(ChapterModel("Electricity", "Powering our lives", clas, subject))
                    }
                }
            }




        }




        // Initialize the adapter with the list
        adapter = ChapterAdapter(requireContext(), list)
        // Set the adapter to the RecyclerView
        binding.chapterRecyclerview.adapter = adapter
    }
}