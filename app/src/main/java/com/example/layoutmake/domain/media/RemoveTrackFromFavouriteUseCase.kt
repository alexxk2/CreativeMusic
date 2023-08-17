package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository

class RemoveTrackFromFavouriteUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(track: Track) = mediaRepository.removeTrackFromFavourite(track)
}