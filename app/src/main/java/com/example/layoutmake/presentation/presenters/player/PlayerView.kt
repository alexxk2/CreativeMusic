package com.example.layoutmake.presentation.presenters.player

import com.example.layoutmake.domain.models.Track

interface PlayerView {
    fun drawPlayer(track: Track)
}