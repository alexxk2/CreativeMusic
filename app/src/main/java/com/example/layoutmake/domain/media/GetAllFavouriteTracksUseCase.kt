package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavouriteTracksUseCase(private val mediaRepository: MediaRepository) {

    fun execute(): Flow<List<Track>> = mediaRepository.getAllFavouriteTracks()
}