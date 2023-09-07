package com.example.layoutmake.presentation.media.model

import com.example.layoutmake.domain.models.Track

sealed interface PlaylistsScreenState{

    object Content: PlaylistsScreenState
    object Empty: PlaylistsScreenState

}