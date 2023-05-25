package com.example.layoutmake.data.player

import androidx.lifecycle.LiveData
import com.example.layoutmake.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: Player) : PlayerRepository {

    override fun preparePlayer(path: String) {
        mediaPlayer.prepareMediaPlayer(path)
    }

    override fun playSong() {
        mediaPlayer.startMediaPlayer()
    }

    override fun pauseSong() {
        mediaPlayer.pauseMediaPlayer()
    }

    override fun releasePlayer() {
        mediaPlayer.releaseMediaPlayer()
    }

    override fun getTrackCurrentPosition(): Int = mediaPlayer.getTrackCurrentPosition()

    override fun getPlayerState(): LiveData<Int> = mediaPlayer.getPlayerState()
}