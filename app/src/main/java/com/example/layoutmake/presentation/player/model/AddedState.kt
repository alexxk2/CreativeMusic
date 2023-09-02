package com.example.layoutmake.presentation.player.model

import com.example.layoutmake.domain.models.Playlist

sealed interface AddedState{
    object Ready: AddedState

    data class Done(val playlist: Playlist): AddedState

    data class NotDone(val playlist: Playlist): AddedState
}