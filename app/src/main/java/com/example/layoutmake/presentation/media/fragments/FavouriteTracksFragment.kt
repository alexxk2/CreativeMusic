package com.example.layoutmake.presentation.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutmake.databinding.FragmentFavouriteTracksBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.media.model.FavouriteScreenState
import com.example.layoutmake.presentation.media.view_model.FavouriteTrackViewModel
import com.example.layoutmake.presentation.search.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavouriteTracksFragment : Fragment() {

    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavouriteTrackViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter
    private var favouriteTracks = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTracksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllFavouriteTracks()
        setFavouriteRecyclerView()

        viewModel.trackList.observe(viewLifecycleOwner){trackList->
            favouriteTracks.clear()
            favouriteTracks.addAll(trackList)
            trackAdapter.notifyDataSetChanged()
        }

        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            manageScreenContent(screenState)
        }
    }

    private fun setFavouriteRecyclerView() {

        trackAdapter = TrackAdapter(requireContext(), favouriteTracks,
            object : TrackAdapter.TrackActionListener {
                override fun onClickTrack(track: Track) {
                    viewModel.getAllFavouriteTracks()
                    manageClickAction(track)
                }
            })
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = trackAdapter
            recyclerView.setHasFixedSize(true)
        }

    }

    private fun manageClickAction(track: Track) {
        if (viewModel.clickDebounce()) {
            val action = MediaFragmentDirections.actionMediaFragmentToPlayerFragment(track)
            findNavController().navigate(action)
        }
    }

    private fun manageScreenContent(screenState: FavouriteScreenState) {
        when (screenState) {
            is FavouriteScreenState.Content -> {
                binding.noContentLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

            }

            FavouriteScreenState.Empty -> {
                binding.noContentLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavouriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = FavouriteTracksFragment().apply {
            arguments = Bundle().apply {

                putInt(NUMBER, number)
            }
        }
    }


}