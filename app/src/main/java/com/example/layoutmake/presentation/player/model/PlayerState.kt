package com.example.layoutmake.presentation.player.model

import com.example.layoutmake.domain.models.Track

sealed interface PlayerState {

    data class Draw(val track: Track): PlayerState

    object Loading : PlayerState

    object Prepared : PlayerState

    object Playing : PlayerState

    object Paused: PlayerState

    object Completed: PlayerState
}