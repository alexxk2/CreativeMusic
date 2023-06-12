package com.example.layoutmake.presentation.player.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.databinding.ActivityPlayerBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.player.model.PlayerState
import com.example.layoutmake.presentation.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel{ parametersOf(track) }

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunn = object : Runnable{
        override fun run() {
            binding.timer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(viewModel.getTrackCurrentPosition())
            handler.postDelayed(this, TIMER_UPDATE_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater).also { setContentView(it.root) }

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track.DEFAULT
        } else intent.getParcelableExtra(TRACK) ?: Track.DEFAULT


        viewModel.playerState.observe(this) { state ->

            when (state) {
                is PlayerState.Draw -> drawPlayer(state.track)
                is PlayerState.Loading -> showLoading()
                is PlayerState.Prepared -> showPrepared()
                is PlayerState.Playing -> showPlaying()
                is PlayerState.Paused -> showPaused()
                is PlayerState.Completed -> showCompleted()
            }
        }

        viewModel.preparePlayer()

        binding.playButton.setOnClickListener {
            viewModel.playSong()

            handler.postDelayed(
                timerRunn,
                TIMER_UPDATE_INTERVAL_MS
            )
        }

        binding.pauseButton.setOnClickListener {
            viewModel.pauseSong()
        }

        binding.arrowBackButton.setOnClickListener { finish() }
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

            Glide.with(this@PlayerActivity)
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
        handler.removeCallbacks(timerRunn)
        binding.timer.text = getString(R.string.default_timer_text)
        makePlayButtonVisible()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(timerRunn)
        viewModel.pauseSong()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunn)
        viewModel.pauseSong()
        viewModel.releasePlayer()
    }

    companion object {
        private const val TRACK = "track"
        private const val TIMER_UPDATE_INTERVAL_MS = 300L
    }


}