package com.example.layoutmake.presentation.player.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentPlayerBinding
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.player.adapters.PlaylistsFlatAdapter
import com.example.layoutmake.presentation.player.model.AddedState
import com.example.layoutmake.presentation.player.model.PlayerState
import com.example.layoutmake.presentation.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistFlatAdapter: PlaylistsFlatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(TRACK, Track::class.java)!!
            } else {
                it.getParcelable(TRACK)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        viewModel.getAllPlaylists()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.addedState.observe(viewLifecycleOwner) { state ->
            manageAddingContent(state)
        }

        viewModel.playlists.observe(viewLifecycleOwner) { newList ->
            playlistFlatAdapter.submitList(newList)
        }

        viewModel.playerState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is PlayerState.Draw -> drawPlayer(state.track)
                is PlayerState.Loading -> showLoading()
                is PlayerState.Prepared -> showPrepared()
                is PlayerState.Playing -> showPlaying()
                is PlayerState.Paused -> showPaused()
                is PlayerState.Completed -> showCompleted()
            }
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            manageFavouriteButton(isFavourite)
        }

        viewModel.playerTime.observe(viewLifecycleOwner) {
            binding.timer.text = it
        }

        viewModel.preparePlayer()

        binding.playButton.setOnClickListener {
            viewModel.playSong()
        }

        binding.pauseButton.setOnClickListener {
            viewModel.pauseSong()
        }

        binding.addToFavouriteButton.setOnClickListener {

            val date = Calendar.getInstance().timeInMillis
            viewModel.addTrackToFavourite(track.copy(date = date))
        }

        binding.removeFromFavouriteButton.setOnClickListener {
            viewModel.removeTrackFromFavourite(track)
        }

        binding.arrowBackButton.setOnClickListener { findNavController().navigateUp() }

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.addNewPlaylistButton.setOnClickListener {
            val action = PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment(track)
            findNavController().navigate(action)
        }

        manageBottomSheetShadow()

    }

    private fun getCoverArtwork(oldUrl: String) = oldUrl.replaceAfterLast('/', "512x512bb.jpg")

    private fun drawPlayer(track: Track) {

        with(binding) {

            val convertedTime =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis.toLong())

            val cornerRadiusInPx = resources.getDimensionPixelSize(R.dimen.cover_corner_radius)

            Glide.with(requireContext())
                .load(getCoverArtwork(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(cornerRadiusInPx))
                .into(cover)

            songName.text = track.trackName
            artistName.text = track.artistName
            trackDurationValue.text = convertedTime

            if (track.collectionName != null) {
                albumNameValue.text = track.collectionName
            } else {
                albumName.isVisible = false
                albumNameValue.isVisible = false
            }

            trackYearValue.text = track.releaseDate?.take(4)
            trackGenreValue.text = track.primaryGenreName
            trackCountryValue.text = track.country
        }
    }

    private fun makePlayButtonVisible() {
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.INVISIBLE
    }

    private fun makePauseButtonVisible() {
        binding.playButton.visibility = View.INVISIBLE
        binding.pauseButton.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.playButton.isClickable = false
    }

    private fun showPrepared() {
        binding.playButton.isClickable = true
    }

    private fun showPlaying() {
        makePauseButtonVisible()
    }

    private fun showPaused() {
        makePlayButtonVisible()
    }

    private fun showCompleted() {
        binding.timer.text = getString(R.string.default_timer_text)
        makePlayButtonVisible()
    }

    private fun manageFavouriteButton(isFavourite: Boolean) {

        with(binding) {
            if (isFavourite) {
                addToFavouriteButton.visibility = View.INVISIBLE
                removeFromFavouriteButton.visibility = View.VISIBLE
            } else {
                addToFavouriteButton.visibility = View.VISIBLE
                removeFromFavouriteButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun manageBottomSheetShadow() {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })
    }

    private fun setRecyclerView() {
        playlistFlatAdapter = PlaylistsFlatAdapter(context = requireContext()) { playlist ->
            viewModel.addTrackToExactPlaylist(track,playlist)
        }

        binding.playlistRecyclerViewFlat.adapter = playlistFlatAdapter
        binding.playlistRecyclerViewFlat.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistRecyclerViewFlat.setHasFixedSize(true)
    }

    private fun manageAddingContent(state: AddedState) {

        when (state) {
            is AddedState.Done -> {
                showToastIfAddedOrNot(state.playlist, false)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            is AddedState.NotDone -> {
                showToastIfAddedOrNot(state.playlist, true)
            }

            AddedState.Ready -> {}
        }

    }

    private fun showToastIfAddedOrNot(playlist: Playlist, ifContains: Boolean) {

        if (ifContains) {
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_message_already_exists, playlist.playlistName),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_message_added, playlist.playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.pauseSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pauseSong()
        viewModel.releasePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TRACK = "track"
    }
}