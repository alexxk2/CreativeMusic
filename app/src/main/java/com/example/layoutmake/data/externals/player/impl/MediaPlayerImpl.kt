package com.example.layoutmake.data.externals.player.impl

import android.media.MediaPlayer
import com.example.layoutmake.data.externals.player.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MediaPlayerImpl : Player {


    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var mediaPlayerState = MutableStateFlow(PLAYER_STATE_LOADING)

    override fun prepareMediaPlayer(path: String) {
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayerState.value = PLAYER_STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayerState.value = PLAYER_STATE_COMPLETED
        }
    }

    override fun startMediaPlayer() {
        mediaPlayer.start()
        mediaPlayerState.value = PLAYER_STATE_PLAYING
    }

    override fun pauseMediaPlayer() {
        mediaPlayer.pause()
        mediaPlayerState.value = PLAYER_STATE_PAUSED
    }

    override fun releaseMediaPlayer() {
        mediaPlayer.reset()
    }

    override fun getTrackCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun getPlayerState(): Flow<Int> = mediaPlayerState

    companion object {
        const val PLAYER_STATE_LOADING = 1
        const val PLAYER_STATE_PREPARED = 2
        const val PLAYER_STATE_PLAYING = 3
        const val PLAYER_STATE_COMPLETED = 4
        const val PLAYER_STATE_PAUSED = 5
    }
}