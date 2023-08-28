package com.example.layoutmake.data.repositories.media

import com.example.layoutmake.data.converters.FavouriteDbConverter
import com.example.layoutmake.data.externals.db.RoomStorage
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaRepositoryImpl(
    private val roomStorage: RoomStorage,
    private val converter: FavouriteDbConverter
) : MediaRepository {

    override suspend fun addTrackToFavourite(track: Track) {
        val mappedTrack = converter.mapTrackToData(track)
        roomStorage.addTrackToFavourite(trackDto = mappedTrack)
    }

    override suspend fun removeTrackFromFavourite(track: Track) {
        val mappedTrack = converter.mapTrackToData(track)
        roomStorage.removeTrackFromFavourite(trackDto = mappedTrack)
    }

    override fun getAllFavouriteTracks(): Flow<List<Track>> = flow {
        val resultFromData = roomStorage.getAllFavouriteTracks()

        emit(resultFromData.map { trackDto ->
            converter.mapTrackToDomain(trackDto)
        })
    }

    override fun getFavouriteTracksIds(): Flow<List<Int>>  = flow {
        emit(roomStorage.getFavouriteTracksIds())
    }
}