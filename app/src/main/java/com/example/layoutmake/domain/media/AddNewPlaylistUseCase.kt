package com.example.layoutmake.domain.media

import android.text.Editable
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.repositories.MediaRepository

class AddNewPlaylistUseCase(private val mediaRepository: MediaRepository) {


    suspend fun execute(playlistName: Editable?, playlistDescription: Editable?, coverSrc: String?) {

        val newPlaylist = Playlist(
            playlistName = playlistName.toString(),
            playlistDescription = playlistDescription.toString(),
            coverSrc = coverSrc,
            tracksIds = emptyList(),
            tracksNumber = 0
        )
        mediaRepository.addNewPlaylist(newPlaylist)
    }
}