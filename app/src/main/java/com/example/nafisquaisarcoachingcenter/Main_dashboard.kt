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
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.databinding.ActivityMainDashboardBinding
import com.example.nafisquaisarcoachingcenter.fragment.CourseTypeFragment
import com.example.nafisquaisarcoachingcenter.fragment.Courses
import com.example.nafisquaisarcoachingcenter.fragment.Home
import com.example.nafisquaisarcoachingcenter.fragment.Note
import com.example.nafisquaisarcoachingcenter.fragment.Profile
import com.example.nafisquaisarcoachingcenter.fragment.Test
import com.example.nafisquaisarcoachingcenter.model.userDetail
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Main_dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityMainDashboardBinding
    private val drawer by lazy { binding.drawerlayout }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var databaseReference: DatabaseReference




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

        // Set the white icon for the toggle
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.defaultwhite)
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
                 R.id.course-> {
                    loadfragment(CourseTypeFragment(),1)
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
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val email = account?.email ?: ""
        val googleDisplayName = account?.displayName ?: ""
        val googleProfileUrl = account?.photoUrl?.toString() ?: ""

        if (account != null) {
            personNameText.setText(googleDisplayName)
            headerEmail.setText(email)
            if (googleProfileUrl != null) {
                Glide.with(this)
                    .load(googleProfileUrl)
                    .placeholder(R.drawable.user_profile_icon) // Optional placeholder image
                    .into(profilePhoto)
            } else {
                // Handle the case where there is no profile image
                profilePhoto.setImageResource(R.drawable.user_profile_icon) // Set a default image
            }
        }

        val uid = auth.currentUser?.uid ?: return
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(userDetail::class.java)
                user?.let {
                    // Check if fragment is still attached before updating UI
                        personNameText.setText(it.Name.ifEmpty { googleDisplayName })
                        headerEmail.setText(it.Email.ifEmpty { email })

                        Glide.with(this@Main_dashboard)
                            .load(it.ProUrl.ifEmpty { googleProfileUrl })
                            .placeholder(R.drawable.user_profile_icon)
                            .into(profilePhoto)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Check if fragment is still attached before showing a toast
                    Toast.makeText(this@Main_dashboard, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()

            }
        })
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
    private fun shortenName(name: String): String {
        return if (name.length > 13) name.split(" ").take(2).joinToString(" ") else name
    }


}