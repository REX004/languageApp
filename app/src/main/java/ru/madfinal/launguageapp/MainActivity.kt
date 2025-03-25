package ru.madfinal.launguageapp
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.madfinal.launguageapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var navController: NavController = NavController(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

//        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
//        bottomNav.setupWithNavController(navController)
//
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.homeFragment, R.id.profileFragment, R.id.messageFragment, R.id.favoritesFragment -> {
//                    bottomNav.visibility = View.VISIBLE
//                    binding.mainBt.visibility = View.VISIBLE
//                }
//
//                else -> {
//                    bottomNav.visibility = View.GONE
//                    binding.mainBt.visibility = View.GONE
//                }
//            }
//        }
//        binding.mainBt.setOnClickListener {
//            navController.navigate(R.id.cartFragment)
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


}