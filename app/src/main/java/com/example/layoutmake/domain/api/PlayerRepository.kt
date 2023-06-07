package com.example.layoutmake.domain.api

import com.example.layoutmake.data.OnCompletePlaying
import com.example.layoutmake.data.OnPreparePlayer

interface PlayerRepository {

    fun preparePlayer(path: String, onComplete: OnCompletePlaying, onPreparePlayer: OnPreparePlayer)
    fun playSong()
    fun pauseSong()
    fun releasePlayer()
    fun getTrackCurrentPosition(): Int

}