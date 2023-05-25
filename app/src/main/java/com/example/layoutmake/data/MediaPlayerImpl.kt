package com.example.layoutmake.data

import android.app.usage.NetworkStats.Bucket.STATE_DEFAULT
import android.media.MediaPlayer
import android.print.PrintJobInfo.STATE_COMPLETED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.layoutmake.data.player.Player

class MediaPlayerImpl: Player {


    private val mediaPlayer = MediaPlayer()

    private val _state = MutableLiveData(STATE_DEFAULT)
    private val state: LiveData<Int> = _state

    override fun prepareMediaPlayer(path: String) {
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _state.value = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            _state.value = STATE_COMPLETED
        }
    }

    override fun startMediaPlayer() {
        mediaPlayer.start()
        _state.value = STATE_PLAYING
    }

    override fun pauseMediaPlayer() {
        mediaPlayer.pause()
        _state.value = STATE_PAUSED
    }

    override fun releaseMediaPlayer() {
        mediaPlayer.release()
        _state.value = STATE_DEFAULT

    }

    override fun getTrackCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun getPlayerState(): LiveData<Int>  = state

    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_COMPLETED = 2
        private const val STATE_PLAYING = 3
        private const val STATE_PAUSED = 4
    }
}