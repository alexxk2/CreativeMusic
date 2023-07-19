package com.example.layoutmake.presentation.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentMediaBinding
import com.example.layoutmake.presentation.media.adapters.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaViewPagerAdapter(activity?.supportFragmentManager!!,lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favourite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }

        }
        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }
}