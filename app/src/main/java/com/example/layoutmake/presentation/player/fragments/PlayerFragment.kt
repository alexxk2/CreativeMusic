package com.example.layoutmake.presentation.player.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentPlayerBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.player.model.PlayerState
import com.example.layoutmake.presentation.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel{ parametersOf(track) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(TRACK,Track::class.java)!!
            } else{
                it.getParcelable(TRACK)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModel.playerTime.observe(viewLifecycleOwner){
            binding.timer.text = it
        }

        viewModel.preparePlayer()

        binding.playButton.setOnClickListener {
            viewModel.playSong()
        }

        binding.pauseButton.setOnClickListener {
            viewModel.pauseSong()
        }

        binding.arrowBackButton.setOnClickListener { findNavController().navigateUp() }

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

    private fun makePlayButtonVisible(){
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.INVISIBLE
    }

    private fun makePauseButtonVisible(){
        binding.playButton.visibility = View.INVISIBLE
        binding.pauseButton.visibility = View.VISIBLE
    }

    private fun showLoading(){
        binding.playButton.isClickable = false
    }

    private fun showPrepared(){
        binding.playButton.isClickable = true
    }

    private fun showPlaying(){
        makePauseButtonVisible()
    }

    private fun showPaused(){
        makePlayButtonVisible()
    }

    private fun showCompleted(){
        binding.timer.text = getString(R.string.default_timer_text)
        makePlayButtonVisible()
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