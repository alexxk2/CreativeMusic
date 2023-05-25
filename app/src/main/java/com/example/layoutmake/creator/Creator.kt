package com.example.layoutmake.creator

import com.example.layoutmake.data.MediaPlayerImpl
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.presenters.player.PlayerPresenter
import com.example.layoutmake.presentation.presenters.player.PlayerView

object Creator {

    fun providePresenter(view: PlayerView, track: Track, mediaPlayerImpl: MediaPlayerImpl): PlayerPresenter {
        return PlayerPresenter(view, track, mediaPlayerImpl)
    }
}