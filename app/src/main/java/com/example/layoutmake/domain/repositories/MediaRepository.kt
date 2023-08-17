package com.example.layoutmake.domain.repositories

import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun addTrackToFavourite(track: Track)

    suspend fun removeTrackFromFavourite(track: Track)

    fun getAllFavouriteTracks(): Flow<List<Track>>

    fun getFavouriteTracksIds(): Flow<List<Int>>
}