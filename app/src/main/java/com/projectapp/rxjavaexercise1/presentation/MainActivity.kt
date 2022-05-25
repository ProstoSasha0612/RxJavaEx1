package com.projectapp.rxjavaexercise1.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectapp.rxjavaexercise1.R
import com.projectapp.rxjavaexercise1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openMainFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun openMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment.newInstance())
            .commit()
    }
}