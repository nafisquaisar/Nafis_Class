package com.example.nafisquaisarcoachingcenter

//import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
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
                    loadfragment(Note(), 1)
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