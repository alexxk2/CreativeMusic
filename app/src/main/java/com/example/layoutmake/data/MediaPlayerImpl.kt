package com.example.layoutmake.data

import android.media.MediaPlayer
import com.example.layoutmake.data.player.Player

class MediaPlayerImpl: Player {


    private val mediaPlayer = MediaPlayer()


    override fun prepareMediaPlayer(path: String, onComplete: OnCompletePlaying, onPreparePlayer: OnPreparePlayer) {
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
           onPreparePlayer.doOnPreparedPlayer()
        }
        mediaPlayer.setOnCompletionListener {
            onComplete.doOnCompletePlaying()
        }
    }

    override fun startMediaPlayer() {
        mediaPlayer.start()
    }

    override fun pauseMediaPlayer() {
        mediaPlayer.pause()
    }

    override fun releaseMediaPlayer() {
        mediaPlayer.release()
    }

    override fun getTrackCurrentPosition(): Int = mediaPlayer.currentPosition

}