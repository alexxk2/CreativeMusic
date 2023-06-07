package com.example.layoutmake.presentation.presenters.player

import com.example.layoutmake.data.OnCompletePlaying
import com.example.layoutmake.data.OnPreparePlayer
import com.example.layoutmake.domain.api.PlayerRepository
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.usecases.GetTrackCurrentPositionUseCase
import com.example.layoutmake.domain.usecases.PauseSongUseCase
import com.example.layoutmake.domain.usecases.PlaySongUseCase
import com.example.layoutmake.domain.usecases.PreparePlayerUseCase
import com.example.layoutmake.domain.usecases.ReleasePlayerUseCase

class PlayerPresenter(
    private var view: PlayerView?,
    private val track: Track,
    playerRepository: PlayerRepository,
    private val onComplete: OnCompletePlaying,
    private val onPreparePlayer: OnPreparePlayer
) {

    init {
        view?.drawPlayer(track)

    }

    private val playSongUseCase = PlaySongUseCase(playerRepository)
    private val pauseSongUseCase = PauseSongUseCase(playerRepository)
    private val preparePlayerUseCase = PreparePlayerUseCase(playerRepository)
    private val releasePlayerUseCase = ReleasePlayerUseCase(playerRepository)
    private val getTrackCurrentPositionUseCase =
        GetTrackCurrentPositionUseCase(playerRepository)

    fun playSong() {
        playSongUseCase.execute()
    }

    fun pauseSong() {
        pauseSongUseCase.execute()
    }

    fun preparePlayer() {
        preparePlayerUseCase.execute(
            path = track.previewUrl!!,
            onComplete = onComplete,
            onPreparePlayer = onPreparePlayer
        )
    }

    fun releasePlayer() {
        releasePlayerUseCase.execute()
    }

    fun getTrackCurrentPosition(): Int = getTrackCurrentPositionUseCase.execute()


    fun onViewDestroyed() {
        view = null
    }

}