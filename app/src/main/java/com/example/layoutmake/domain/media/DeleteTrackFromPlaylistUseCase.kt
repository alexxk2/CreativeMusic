package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository

class DeleteTrackFromPlaylistUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(track: Track, playlistId: Int) = mediaRepository.deleteTrackFromPlaylist(track, playlistId)
}