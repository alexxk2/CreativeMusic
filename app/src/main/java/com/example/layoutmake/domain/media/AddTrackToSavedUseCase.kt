package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository

class AddTrackToSavedUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(track: Track) = mediaRepository.addTrackToSaved(track)
}