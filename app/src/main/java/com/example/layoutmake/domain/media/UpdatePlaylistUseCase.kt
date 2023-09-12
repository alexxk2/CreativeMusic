package com.example.layoutmake.domain.media

import android.net.Uri
import android.text.Editable
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository

class UpdatePlaylistUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(track: Track, playlist: Playlist) {

        mediaRepository.updatePlaylist(track, playlist)
    }

    suspend fun execute(
        playlistId: Int,
        playlistName: Editable?,
        playlistDescription: Editable?,
        uri: Uri?
    ) {

        mediaRepository.updatePlaylist(
            playlistId = playlistId,
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            uri = uri
        )
    }
}