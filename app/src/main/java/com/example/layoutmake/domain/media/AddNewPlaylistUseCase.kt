package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.repositories.MediaRepository

class AddNewPlaylistUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(playlist: Playlist) = mediaRepository.addNewPlaylist(playlist)
}