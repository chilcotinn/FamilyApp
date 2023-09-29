package com.chilcotin.familyapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chilcotin.familyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomMenuClick()

    }

    private fun bottomMenuClick() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.todo -> {
                    Toast.makeText(this, "todo", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.todoShare -> {
                    Toast.makeText(this, "todoShare", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.shopList -> {
                    Toast.makeText(this, "shopList", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.chat -> {
                    Toast.makeText(this, "chat", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.settings -> {
                    Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }
}