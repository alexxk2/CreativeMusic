package com.example.layoutmake.data.repositories.player.impl

import com.example.layoutmake.data.externals.player.Player
import com.example.layoutmake.domain.repositories.PlayerRepository
import kotlinx.coroutines.flow.Flow

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

    override fun getPlayerState(): Flow<Int> = mediaPlayer.getPlayerState()
}