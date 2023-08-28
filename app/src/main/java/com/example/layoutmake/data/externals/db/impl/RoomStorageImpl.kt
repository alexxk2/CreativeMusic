package com.example.layoutmake.data.externals.db.impl

import com.example.layoutmake.data.externals.db.FavouriteDatabase
import com.example.layoutmake.data.externals.db.RoomStorage
import com.example.layoutmake.data.externals.search.dto.TrackDto

class RoomStorageImpl(favouriteDatabase: FavouriteDatabase): RoomStorage {

    private val favouriteDao = favouriteDatabase.favouriteDao()

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
}