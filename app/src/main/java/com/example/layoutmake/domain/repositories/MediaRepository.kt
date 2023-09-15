package com.example.layoutmake.domain.repositories

import android.net.Uri
import android.text.Editable
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun addTrackToFavourite(track: Track)

    suspend fun removeTrackFromFavourite(track: Track)

    fun getAllFavouriteTracks(): Flow<List<Track>>

    fun getFavouriteTracksIds(): Flow<List<Int>>


    suspend fun addNewPlaylist(playlistName: Editable?, playlistDescription: Editable?, uri: Uri?)

    suspend fun deleteAllPlaylists()

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun updatePlaylist(track: Track, playlist: Playlist)

    suspend fun updatePlaylist(playlistId: Int, playlistName: Editable?, playlistDescription: Editable?, uri: Uri?)

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getPlaylist(playlistId: Int): Flow<Playlist>

    suspend fun sharePlaylist(playlistId:Int)


    suspend fun addTrackToSaved(track: Track)

    fun getPlaylistTracks(listOfIds: List<Pair<Int, Long>>): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int)

    suspend fun saveImageAndReturnPath(uri: Uri): Uri


}