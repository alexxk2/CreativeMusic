package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetAllPlaylistsUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(): Flow<List<Playlist>> = mediaRepository.getAllPlaylists()
}