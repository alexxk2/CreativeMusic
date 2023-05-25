package com.example.layoutmake.presentation.ui

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.R
import com.example.layoutmake.creator.Creator
import com.example.layoutmake.data.MediaPlayerImpl
import com.example.layoutmake.databinding.ActivityPlayerBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.presenters.player.PlayerPresenter
import com.example.layoutmake.presentation.presenters.player.PlayerView
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track

    private lateinit var presenter: PlayerPresenter
    private val mediaPlayerImpl = MediaPlayerImpl()
    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT

    private val timerRunn = object : Runnable{
        override fun run() {
            binding.timer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(presenter.getTrackCurrentPosition())
            handler.postDelayed(this, TIMER_UPDATE_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater).also { setContentView(it.root) }

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track.DEFAULT
        } else intent.getParcelableExtra(TRACK) ?: Track.DEFAULT

        presenter = Creator.providePresenter(this,track,mediaPlayerImpl)

        presenter.preparePlayer()

        binding.playButton.setOnClickListener {
            presenter.playSong()
            manageControlButtonsVisibility()

            handler.postDelayed(
                timerRunn,
                TIMER_UPDATE_INTERVAL_MS
            )
        }

        binding.pauseButton.setOnClickListener {
            presenter.pauseSong()
            manageControlButtonsVisibility()
        }

        presenter.getPlayerState().observe(this){state->
            playerState = state
            when(state){
                STATE_PREPARED -> binding.playButton.isClickable = true
                STATE_COMPLETED -> {
                    handler.removeCallbacks(timerRunn)
                    binding.timer.text = getString(R.string.default_timer_text)
                    manageControlButtonsVisibility()
                }
            }
        }

        binding.arrowBackButton.setOnClickListener { finish() }
    }


    private fun getCoverArtwork(oldUrl: String) = oldUrl.replaceAfterLast('/', "512x512bb.jpg")

    override fun drawPlayer(track: Track) {

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

    private fun manageControlButtonsVisibility(){
        when(playerState){
            STATE_PLAYING -> {
                makePauseButtonVisible()
            }
            else -> {
                makePlayButtonVisible()
            }
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

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(timerRunn)
        presenter.pauseSong()
        manageControlButtonsVisibility()
    }

    override fun onResume() {
        super.onResume()
        manageControlButtonsVisibility()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunn)
        presenter.pauseSong()
        presenter.releasePlayer()
        presenter.onViewDestroyed()
    }


    companion object {
        private const val TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_COMPLETED = 2
        private const val STATE_PLAYING = 3
        private const val STATE_PAUSED = 4
        private const val TIMER_UPDATE_INTERVAL_MS = 300L
    }

}