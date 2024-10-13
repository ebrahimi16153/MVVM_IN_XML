package com.github.ebrahimi16153.mvvminxml.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.ebrahimi16153.mvvminxml.R
import com.github.ebrahimi16153.mvvminxml.databinding.ActivityMainBinding
import com.github.ebrahimi16153.mvvminxml.util.connection.CheckNetwork
import com.github.ebrahimi16153.mvvminxml.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //binding
    private var _binding: ActivityMainBinding? = null
    private val binding get()= _binding
    private lateinit var navController: NavController
    //viewModels
    private val mainViewModel:MainViewModel by viewModels()



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            // navHost
            val navHost =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            // navController
            navController = navHost.navController

            // fill bottomNav
            bottomNav.setupWithNavController(navController)

            // hide bottomNav in detailScreen
            navController.addOnDestinationChangedListener { _, destination, _ ->

                if (destination.id == R.id.detailFragment) binding?.bottomNav?.isVisible = false
            }



            mainViewModel.networkStatus.observe(this@MainActivity) { itNetWorkStatus ->

                when (itNetWorkStatus) {
                    CheckNetwork.Status.Available -> {

                        fragmentContainerView.isVisible = true
                         bottomNav.isVisible = true
//                        navController.navigate(resId = R.id.homeFragment)
                        disconnectedLay.isVisible = false
                    }

                    CheckNetwork.Status.Unavailable -> {
                        fragmentContainerView.isVisible = false
                        bottomNav.isVisible = false
                        disconnected.disconnectedText.text = "Network Unavailable"
                        disconnected.disconnectedIcon.setImageResource(R.drawable.disconnect)
                        disconnectedLay.isVisible = true
                    }
                    CheckNetwork.Status.Losing -> {
                        fragmentContainerView.isVisible = false
                        bottomNav.isVisible = false
                        disconnected.disconnectedText.text = "Network is Losing..."
                        disconnected.disconnectedIcon.setImageResource(R.drawable.disconnect)
                        disconnectedLay.isVisible = true

                    }
                    CheckNetwork.Status.Lost -> {
                        fragmentContainerView.isVisible = false
                        bottomNav.isVisible = false
                        disconnected.disconnectedText.text = "Network is Lost"
                        disconnected.disconnectedIcon.setImageResource(R.drawable.disconnect)
                        disconnectedLay.isVisible = true

                    }
                    else -> {}
                }

            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}