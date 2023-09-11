package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistUseCase(private val mediaRepository: MediaRepository) {

     fun execute(playlistId: Int): Flow<Playlist> = mediaRepository.getPlaylist(playlistId)
}