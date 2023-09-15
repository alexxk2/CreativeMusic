package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.repositories.MediaRepository

class SharePlaylistUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(playlistId: Int) = mediaRepository.sharePlaylist(playlistId)
}