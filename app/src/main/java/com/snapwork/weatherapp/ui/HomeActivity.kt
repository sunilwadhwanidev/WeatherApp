package com.snapwork.weatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.snapwork.weatherapp.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = HomePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Current"
                else -> "History"
            }
        }.attach()
    }
}
