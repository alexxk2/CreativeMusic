package com.example.layoutmake

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.layoutmake.databinding.ActivityPlayerBinding
import com.example.layoutmake.models.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT

    private val timerRunn = object : Runnable{
        override fun run() {
            binding.timer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)

            handler.postDelayed(this, TIMER_UPDATE_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater).also { setContentView(it.root) }


        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java) ?: Track.DEFAULT
        } else intent.getParcelableExtra(TRACK) ?: Track.DEFAULT

        inflateViews()
        prepareMediaPlayer()

        binding.arrowBackButton.setOnClickListener { finish() }
        binding.playButton.setOnClickListener {
            startPlayer()

            handler.postDelayed(
                timerRunn,
                TIMER_UPDATE_INTERVAL_MS
            )
        }

        binding.pauseButton.setOnClickListener {
            pausePlayer()
        }
    }

    private fun inflateViews() {
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

    private fun getCoverArtwork(oldUrl: String) = oldUrl.replaceAfterLast('/', "512x512bb.jpg")

    private fun prepareMediaPlayer(){
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isClickable = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            manageControlButtonsVisibility()
            handler.removeCallbacks(timerRunn)
            binding.timer.text = getString(R.string.default_timer_text)
        }
    }

    private fun manageControlButtonsVisibility(){
        when(playerState){
            STATE_PREPARED -> {
                makePlayButtonVisible()
            }
            STATE_PAUSED-> {
                makePlayButtonVisible()
            }
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

    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        manageControlButtonsVisibility()
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        manageControlButtonsVisibility()
        handler.removeCallbacks(timerRunn)
    }

    override fun onStop() {
        super.onStop()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        pausePlayer()
        mediaPlayer.release()
    }

    companion object {
        private const val TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_UPDATE_INTERVAL_MS = 300L
    }

}