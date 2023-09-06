package com.example.layoutmake.domain.media

import android.net.Uri
import android.text.Editable
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.repositories.MediaRepository

class AddNewPlaylistUseCase(private val mediaRepository: MediaRepository) {

    suspend fun execute(
        playlistName: Editable?,
        playlistDescription: Editable?,
        uri: Uri?
    ) {
        mediaRepository.addNewPlaylist(
            playlistName = playlistName,
            playlistDescription = playlistDescription,
            uri = uri
        )
    }
}