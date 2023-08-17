package com.example.layoutmake.data.repositories.media

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow

class MediaRepositoryImpl(): MediaRepository {

    override suspend fun addTrackToFavouriteUseCase(track: Track) {
        TODO("Not yet implemented")
    }

    override suspend fun removeTrackFromFavourite(track: Track) {
        TODO("Not yet implemented")
    }

    override fun getAllFavouriteTracksUseCase(): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

    override fun getFavouriteTracksIds(): Flow<List<Int>> {
        TODO("Not yet implemented")
    }
}