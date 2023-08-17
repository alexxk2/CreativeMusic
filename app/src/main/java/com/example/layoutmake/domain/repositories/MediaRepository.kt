package com.example.layoutmake.domain.repositories

import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun addTrackToFavouriteUseCase(track: Track)

    suspend fun removeTrackFromFavourite(track: Track)

    fun getAllFavouriteTracksUseCase(): Flow<List<Track>>

    fun getFavouriteTracksIds(): Flow<List<Int>>
}