package com.example.layoutmake.data.externals.db

import com.example.layoutmake.data.externals.search.dto.TrackDto

interface RoomStorage {

    suspend fun addTrackToFavourite(trackDto: TrackDto)

    suspend fun removeTrackFromFavourite(trackDto: TrackDto)

    suspend fun getAllFavouriteTracks(): List<TrackDto>

    suspend fun getFavouriteTracksIds(): List<Int>
}