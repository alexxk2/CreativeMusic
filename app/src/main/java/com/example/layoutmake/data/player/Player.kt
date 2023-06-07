package com.example.layoutmake.data.player

import com.example.layoutmake.data.OnCompletePlaying
import com.example.layoutmake.data.OnPreparePlayer

interface Player {

    fun prepareMediaPlayer(path: String, onComplete: OnCompletePlaying, onPreparePlayer: OnPreparePlayer)
    fun startMediaPlayer()
    fun pauseMediaPlayer()
    fun releaseMediaPlayer()
    fun getTrackCurrentPosition():Int
}