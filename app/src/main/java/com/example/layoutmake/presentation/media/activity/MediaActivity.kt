package com.example.layoutmake.presentation.media.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.layoutmake.R
import com.example.layoutmake.databinding.ActivityMediaBinding
import com.example.layoutmake.presentation.media.adapters.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager,lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, position ->
           when(position){
               0 -> tab.text = getString(R.string.favourite_tracks)
               else -> tab.text = getString(R.string.playlists)
           }

        }
        tabMediator.attach()

        binding.arrowBackButton.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}