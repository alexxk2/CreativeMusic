package com.example.layoutmake.presentation.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentMediaBinding
import com.example.layoutmake.presentation.media.adapters.MediaViewPagerAdapter
import com.example.layoutmake.presentation.media.view_model.FavouriteTrackViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator
    private val viewModel: FavouriteTrackViewModel by viewModel()

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

        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager,lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favourite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }

        }
        tabMediator.attach()

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(3000L)
                    viewModel.getAllFavouriteTracks()

                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        tabMediator.detach()
        _binding = null
    }

}