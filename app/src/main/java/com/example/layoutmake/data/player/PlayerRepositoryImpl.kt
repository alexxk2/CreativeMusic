package com.example.layoutmake.data.player

import com.example.layoutmake.data.OnCompletePlaying
import com.example.layoutmake.data.OnPreparePlayer
import com.example.layoutmake.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: Player) : PlayerRepository {

    override fun preparePlayer(path: String, onComplete: OnCompletePlaying, onPreparePlayer: OnPreparePlayer) {
        mediaPlayer.prepareMediaPlayer(path, onComplete, onPreparePlayer)
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

}