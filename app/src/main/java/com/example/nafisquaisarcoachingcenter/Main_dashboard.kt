package com.example.nafisquaisarcoachingcenter

//import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.databinding.ActivityMainDashboardBinding
import com.example.nafisquaisarcoachingcenter.fragment.Home
import com.example.nafisquaisarcoachingcenter.fragment.Note
import com.example.nafisquaisarcoachingcenter.fragment.Profile
import com.example.nafisquaisarcoachingcenter.fragment.Test
import com.example.nafisquaisarcoachingcenter.fragment.Video
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class Main_dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityMainDashboardBinding
    private val drawer by lazy { binding.drawerlayout }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.welcome_green));
        }

        setPorfile()
        // find id of drawer
//    *************************** drawer start ******************************************

        val toolbar= binding.toolbarIn.toolbar
        setSupportActionBar(toolbar)

        val navigation=binding.navigationView

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.OpenDrawer, R.string.CloseDrawer
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        navigation.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            if (id == R.id.Video_menu_drawer) {
                var intent=Intent(this,Video_Home_Activity::class.java)
                startActivity(intent)

            } else if (id == R.id.Note_menu_drawer) {
                var intent=Intent(this,Note_home_activity::class.java)
                startActivity(intent)

            } else if (id == R.id.Pyq_menu_drawer) {
              var intent=Intent(this,PYQActivity::class.java)
                startActivity(intent)
            } else if (id == R.id.Logout_menu_drawer) {
                //  ******************** Logout through using Google Start ********************************
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
                googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.signOut().addOnCompleteListener(OnCompleteListener<Void?> {
                    finish()
                    startActivity(Intent(this, FrontVIew::class.java))
                })
                //   ******************** Logout through using Google End ********************************


                //        ******************** Logout through using Email and password Start ********************************
                if (auth.getCurrentUser() != null) {
                    auth.signOut()
                    val intent = Intent(this, FrontVIew::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    Toast.makeText(this,"LogOut Successfully", Toast.LENGTH_SHORT).show()
                }
                //        ******************** Logout through using Email and password End ********************************
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        })


//   ********************* BUTTOM NAVIGATION START****************************************

//        bottomNavigation=binding.toolbarIn.mainContent.bottomNavigation
//        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.baseline_home_24))
//        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.video_logo1))
//        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.baseline_menu_book_24))
//        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.test_logo_1))
//        bottomNavigation.add(MeowBottomNavigation.Model(5, R.drawable.profile_dashboard))
//
//

        binding.toolbarIn.mainContent.BottomNavigationBar.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val id = item.itemId
            when (id)
            {
                R.id.Home -> {
                    loadfragment(Home(), 0)
                }
                 R.id.Video-> {
                    loadfragment(Video(), 1)
                }

                R.id.Note-> {
                    loadfragment(Note(""), 1)
                }

                R.id.Test-> {
                    loadfragment(Test(), 1)
                }

                R.id.Profile -> {
                    loadfragment(Profile(), 1)
                }
            }
            true
        })
        //   ********************* BUTTOM NAVIGATION End****************************************


        binding.toolbarIn.mainContent.BottomNavigationBar.setSelectedItemId(R.id.Home)


    }

    private fun setPorfile() {
        // Access the NavigationView's header view
        val headerView = binding.navigationView.getHeaderView(0)

        // Find views by ID
        val profilePhoto: ImageView = headerView.findViewById(R.id.drawer_header_profile)
        val personNameText: TextView = headerView.findViewById(R.id.drawer_header_Name)
        val headerEmail: TextView = headerView.findViewById(R.id.drawer_header_email)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email

            headerEmail.setText(personEmail)

            // List of titles to check for
            val titles = listOf("MD", "Mr.", "Dr.", "Ms.", "Mrs.", "Prof.", "Sr.", "Jr.")

            if (personName?.length ?: 0 > 13) {
                val nameParts = personName?.split(" ") ?: listOf("")
                var firstName = ""

                if (nameParts.size >= 3) {
                    // Combine the first two parts and check their length
                    val combinedFirstTwo = "${nameParts[0]} ${nameParts[1]}"
                    if (combinedFirstTwo.length <= 13) {
                        firstName = combinedFirstTwo // Use first two parts
                    } else {
                        // Handle the title case
                        if (titles.contains(nameParts[0])) {
                            firstName = nameParts.getOrElse(1) { "" } // Get the second part if it exists
                        } else {
                            firstName = nameParts.firstOrNull() ?: "" // Use the first part if no title
                        }
                    }
                } else if (nameParts.size == 2) {
                    // If only two parts, use both as long as they are <= 13
                    firstName = nameParts.joinToString(" ")
                } else {
                    firstName = nameParts.firstOrNull() ?: "" // Use the first part if only one part
                }

                personNameText.setText(firstName) // Set the extracted firstName
            } else {
                personNameText.setText(personName) // Set the full name if it's short
            }

            // Get the profile image URL
            val profileImageUrl = acct.photoUrl

            // Load the profile image into an ImageView using Glide
            if (profileImageUrl != null) {
                Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile) // Optional placeholder image
                    .into(profilePhoto) // Assuming you have an ImageView in your layout
            } else {
                // Handle the case where there is no profile image
                profilePhoto.setImageResource(R.drawable.profile) // Set a default image
            }
        }

    }


    // ********************** On back press Function **********************
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    //  ***************** Fragment Load Function *****************************
     fun loadfragment(fragment: Fragment, flag:Int){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (flag == 0) {
            fragmentTransaction.add(R.id.container, fragment)
        } else {
            fragmentTransaction.replace(R.id.container, fragment)
        }
        fragmentTransaction.commit()
    }



}