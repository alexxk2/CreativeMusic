package com.example.layoutmake.domain.repositories

import android.net.Uri
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun addTrackToFavourite(track: Track)

    suspend fun removeTrackFromFavourite(track: Track)

    fun getAllFavouriteTracks(): Flow<List<Track>>

    fun getFavouriteTracksIds(): Flow<List<Int>>


    suspend fun addNewPlaylist(playlist: Playlist)

    suspend fun deleteAllPlaylists()

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getPlaylist(playlistId: Int): Flow<Playlist>


    suspend fun addTrackToSaved(track: Track)


    suspend fun saveImageAndReturnPath(uri: Uri): String
}