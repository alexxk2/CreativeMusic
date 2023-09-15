package com.example.layoutmake.data.externals.db

import com.example.layoutmake.data.externals.db.dto.PlaylistDto
import com.example.layoutmake.data.externals.db.dto.SavedTrackDto
import com.example.layoutmake.data.externals.search.dto.TrackDto

interface RoomStorage {

    suspend fun addTrackToFavourite(trackDto: TrackDto)

    suspend fun removeTrackFromFavourite(trackDto: TrackDto)

    suspend fun getAllFavouriteTracks(): List<TrackDto>

    suspend fun getFavouriteTracksIds(): List<Int>


    suspend fun addNewPlaylist(playlistDto: PlaylistDto)

    suspend fun deleteAllPlaylists()

    suspend fun deletePlaylist(playlistDto: PlaylistDto)

    suspend fun updatePlaylist(playlistDto: PlaylistDto)

    suspend fun getAllPlaylists(): List<PlaylistDto>

    suspend fun getPlaylist(playlistId: Int): PlaylistDto


    suspend fun addTrackToSaved(savedTrackDto: SavedTrackDto)

    suspend fun getAllSavedTracks(): List<SavedTrackDto>

    suspend fun deleteTrackFromSaved(savedTrackDto: SavedTrackDto)
}