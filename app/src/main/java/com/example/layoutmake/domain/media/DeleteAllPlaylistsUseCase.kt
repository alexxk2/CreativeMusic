package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.repositories.MediaRepository

class DeleteAllPlaylistsUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute() = mediaRepository.deleteAllPlaylists()
}