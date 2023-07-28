package com.example.layoutmake.domain.repositories

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun preparePlayer(path: String)
    fun playSong()
    fun pauseSong()
    fun releasePlayer()
    fun getTrackCurrentPosition(): Int
    fun getPlayerState(): Flow<Int>


}