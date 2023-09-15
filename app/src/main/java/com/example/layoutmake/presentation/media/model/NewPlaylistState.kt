package com.example.layoutmake.presentation.media.model

import com.example.layoutmake.domain.models.Playlist

sealed interface NewPlaylistState{

    object CreateNewState: NewPlaylistState
    data class UpdateOldState(val playlist: Playlist): NewPlaylistState
}