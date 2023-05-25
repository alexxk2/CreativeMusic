package com.example.layoutmake.presentation.presenters.player

import androidx.lifecycle.LiveData
import com.example.layoutmake.data.MediaPlayerImpl
import com.example.layoutmake.data.player.PlayerRepositoryImpl
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.usecases.GetPlayerStateUseCase
import com.example.layoutmake.domain.usecases.GetTrackCurrentPositionUseCase
import com.example.layoutmake.domain.usecases.PauseSongUseCase
import com.example.layoutmake.domain.usecases.PlaySongUseCase
import com.example.layoutmake.domain.usecases.PreparePlayerUseCase
import com.example.layoutmake.domain.usecases.ReleasePlayerUseCase

class PlayerPresenter(
    private var view: PlayerView?,
    private val track: Track,
    mediaPlayerImpl: MediaPlayerImpl
) {

    init {
        view?.drawPlayer(track)

    }

    private val playerRepositoryImpl = PlayerRepositoryImpl(mediaPlayerImpl)

    private val playSongUseCase = PlaySongUseCase(playerRepositoryImpl)
    private val pauseSongUseCase = PauseSongUseCase(playerRepositoryImpl)
    private val preparePlayerUseCase = PreparePlayerUseCase(playerRepositoryImpl)
    private val releasePlayerUseCase = ReleasePlayerUseCase(playerRepositoryImpl)
    private val getTrackCurrentPositionUseCase =
        GetTrackCurrentPositionUseCase(playerRepositoryImpl)
    private val getPlayerStateUseCase = GetPlayerStateUseCase(playerRepositoryImpl)

    fun playSong() {
        playSongUseCase.execute()
    }

    fun pauseSong() {
        pauseSongUseCase.execute()
    }

    fun preparePlayer() {
        preparePlayerUseCase.execute(
            path = track.previewUrl!!
        )
    }

    fun releasePlayer() {
        releasePlayerUseCase.execute()
    }

    fun getTrackCurrentPosition(): Int = getTrackCurrentPositionUseCase.execute()

    fun getPlayerState(): LiveData<Int> = getPlayerStateUseCase.execute()

    fun onViewDestroyed() {
        view = null
    }

}