package com.example.layoutmake.creator

import com.example.layoutmake.data.OnCompletePlaying
import com.example.layoutmake.data.OnPreparePlayer
import com.example.layoutmake.domain.api.PlayerRepository
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.presenters.player.PlayerPresenter
import com.example.layoutmake.presentation.presenters.player.PlayerView

object Creator {

    fun providePresenter(view: PlayerView, track: Track, playerRepository: PlayerRepository, onComplete: OnCompletePlaying, onPreparePlayer: OnPreparePlayer): PlayerPresenter {

        return PlayerPresenter(view, track, playerRepository, onComplete, onPreparePlayer)
    }
}