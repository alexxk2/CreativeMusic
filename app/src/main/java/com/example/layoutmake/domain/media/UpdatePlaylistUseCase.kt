package com.example.layoutmake.domain.media

import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository

class UpdatePlaylistUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(track: Track, playlist: Playlist) {

        val tempList = mutableListOf<Int>()
        tempList.addAll(playlist.tracksIds)
        tempList.add(track.trackId)

        val updatedPlaylist = playlist.copy(
            tracksIds = tempList.toList(),
            tracksNumber = tempList.size
        )

        mediaRepository.updatePlaylist(updatedPlaylist)
    }
}