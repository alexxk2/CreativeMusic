package com.example.layoutmake.presentation.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.layoutmake.databinding.FragmentPlaylistsBinding
import com.example.layoutmake.presentation.media.adapters.PlaylistsAdapter
import com.example.layoutmake.presentation.media.model.PlaylistsScreenState
import com.example.layoutmake.presentation.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        viewModel.getAllPlaylists()

        viewModel.playlists.observe(viewLifecycleOwner) { newList ->
            playlistAdapter.submitList(newList)
        }

        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            manageScreenContent(screenState)
        }

        binding.addNewPlaylistButton.setOnClickListener {
            val action = MediaFragmentDirections.actionMediaFragmentToNewPlaylistFragment2()
            findNavController().navigate(action)
        }


    }

    private fun setRecyclerView() {
        playlistAdapter = PlaylistsAdapter { playlist ->
            val action =
                MediaFragmentDirections.actionMediaFragmentToPlaylistFragment(playlist.playlistId)
            findNavController().navigate(action)
        }

        binding.playlistRecyclerView.adapter = playlistAdapter
        binding.playlistRecyclerView.setHasFixedSize(true)
    }

    private fun manageScreenContent(screenState: PlaylistsScreenState) {

        with(binding) {
            when (screenState) {
                PlaylistsScreenState.Content -> {
                    playlistRecyclerView.visibility = View.VISIBLE
                    noPlaylistsLayout.visibility = View.GONE
                }

                PlaylistsScreenState.Empty -> {
                    playlistRecyclerView.visibility = View.GONE
                    noPlaylistsLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }
}