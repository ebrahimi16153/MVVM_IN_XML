package com.github.ebrahimi16153.mvvminxml.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.ebrahimi16153.mvvminxml.R
import com.github.ebrahimi16153.mvvminxml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get()= _binding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //nav host
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // navController
        navController = navHost.navController



            binding?.bottomNav?.setupWithNavController(navController)



    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}