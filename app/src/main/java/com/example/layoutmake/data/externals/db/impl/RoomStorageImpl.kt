package com.example.layoutmake.data.externals.db.impl

import com.example.layoutmake.data.externals.db.FavouriteDatabase
import com.example.layoutmake.data.externals.db.RoomStorage
import com.example.layoutmake.data.externals.db.dto.PlaylistDto
import com.example.layoutmake.data.externals.search.dto.TrackDto

class RoomStorageImpl(favouriteDatabase: FavouriteDatabase): RoomStorage {

    private val favouriteDao = favouriteDatabase.favouriteDao()
    private val playlistDao = favouriteDatabase.playlistDao()

    override suspend fun addTrackToFavourite(trackDto: TrackDto) {
        favouriteDao.addNewItem(trackDto)
    }

    override suspend fun removeTrackFromFavourite(trackDto: TrackDto) {
        favouriteDao.deleteItem(trackDto)
    }

    override suspend fun getAllFavouriteTracks(): List<TrackDto> {
       return favouriteDao.gelAllItems()
    }

    override suspend fun getFavouriteTracksIds(): List<Int> {
        return favouriteDao.getAllIds()
    }

    override suspend fun addNewPlaylist(playlistDto: PlaylistDto) {
        playlistDao.addNewItem(playlistDto)
    }

    override suspend fun deleteAllPlaylists() {
        playlistDao.deleteAllItems()
    }

    override suspend fun deletePlaylist(playlistDto: PlaylistDto) {
        playlistDao.deleteItem(playlistDto)
    }

    override suspend fun updatePlaylist(playlistDto: PlaylistDto) {
        playlistDao.updateItem(playlistDto)
    }

    override suspend fun getAllPlaylists(): List<PlaylistDto> {
        return playlistDao.gelAllItems()
    }

    override suspend fun getPlaylist(playlistId: Int): PlaylistDto {
        return playlistDao.getItem(playlistId)
    }
}