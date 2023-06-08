package com.example.layoutmake.data.externals.player

import kotlinx.coroutines.flow.Flow


interface Player {

    fun prepareMediaPlayer(path: String)
    fun startMediaPlayer()
    fun pauseMediaPlayer()
    fun releaseMediaPlayer()
    fun getTrackCurrentPosition():Int
    fun getPlayerState(): Flow<Int>
}