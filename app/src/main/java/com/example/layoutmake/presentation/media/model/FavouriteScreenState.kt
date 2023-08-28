package com.example.layoutmake.presentation.media.model

import com.example.layoutmake.domain.models.Track

sealed interface FavouriteScreenState{

    data class Content(val tracks: List<Track>): FavouriteScreenState
    object Empty: FavouriteScreenState

}