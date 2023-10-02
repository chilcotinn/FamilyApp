package com.chilcotin.familyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.chilcotin.familyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomMenuClick(navController)
    }

    private fun bottomMenuClick(navController: NavController) {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.todo -> {
                    navController.navigate(R.id.todoFragment)
                    true
                }

                R.id.todoShare -> {
                    navController.navigate(R.id.shareTodoFragment)
                    true
                }

                R.id.shopList -> {
                    navController.navigate(R.id.shopListFragment)
                    true
                }

                R.id.chat -> {
                    navController.navigate(R.id.chatFragment)
                    true
                }

                R.id.settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                else -> false
            }
        }
    }
}