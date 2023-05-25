package com.example.layoutmake.domain.api

import androidx.lifecycle.LiveData

interface PlayerRepository {

    fun preparePlayer(path: String)
    fun playSong()
    fun pauseSong()
    fun releasePlayer()
    fun getTrackCurrentPosition(): Int
    fun getPlayerState(): LiveData<Int>
}