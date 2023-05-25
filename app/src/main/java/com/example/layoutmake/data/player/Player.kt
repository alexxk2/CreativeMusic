package com.example.layoutmake.data.player

import androidx.lifecycle.LiveData

interface Player {

    fun prepareMediaPlayer(path: String)
    fun startMediaPlayer()
    fun pauseMediaPlayer()
    fun releaseMediaPlayer()
    fun getTrackCurrentPosition():Int
    fun getPlayerState():LiveData<Int>
}