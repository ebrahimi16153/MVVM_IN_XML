package com.github.ebrahimi16153.mvvminxml.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.ebrahimi16153.mvvminxml.R
import com.github.ebrahimi16153.mvvminxml.databinding.ActivityMainBinding
import com.github.ebrahimi16153.mvvminxml.util.CheckConnection
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get()= _binding
    private lateinit var navController: NavController

    @Inject
    lateinit var connection:CheckConnection


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
            navController.addOnDestinationChangedListener{ _,destination,_ ->

                if (destination.id == R.id.detailFragment) binding?.bottomNav?.isVisible = false
            }
        }





    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}