package com.example.layoutmake.data.repositories.media

import com.example.layoutmake.data.converters.FavouriteDbConverter
import com.example.layoutmake.data.converters.PlaylistDbConverter
import com.example.layoutmake.data.converters.SavedDbConverter
import com.example.layoutmake.data.externals.db.RoomStorage
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaRepositoryImpl(
    private val roomStorage: RoomStorage,
    private val favouriteConverter: FavouriteDbConverter,
    private val playlistConverter: PlaylistDbConverter,
    private val savedDbConverter: SavedDbConverter
) : MediaRepository {

    override suspend fun addTrackToFavourite(track: Track) {
        val mappedTrack = favouriteConverter.mapTrackToData(track)
        roomStorage.addTrackToFavourite(trackDto = mappedTrack)
    }

    override suspend fun removeTrackFromFavourite(track: Track) {
        val mappedTrack = favouriteConverter.mapTrackToData(track)
        roomStorage.removeTrackFromFavourite(trackDto = mappedTrack)
    }

    override fun getAllFavouriteTracks(): Flow<List<Track>> = flow {
        val resultFromData = roomStorage.getAllFavouriteTracks()

        emit(resultFromData.map { trackDto ->
            favouriteConverter.mapTrackToDomain(trackDto)
        })
    }

    override fun getFavouriteTracksIds(): Flow<List<Int>> = flow {
        emit(roomStorage.getFavouriteTracksIds())
    }


    override suspend fun addNewPlaylist(playlist: Playlist) {
        val mappedPlaylist = playlistConverter.mapPlaylistToData(playlist)
        roomStorage.addNewPlaylist(playlistDto = mappedPlaylist)
    }

    override suspend fun deleteAllPlaylists() {
        roomStorage.deleteAllPlaylists()
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val mappedPlaylist = playlistConverter.mapPlaylistToData(playlist)
        roomStorage.deletePlaylist(playlistDto = mappedPlaylist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val mappedPlaylist = playlistConverter.mapPlaylistToData(playlist)
        roomStorage.updatePlaylist(playlistDto = mappedPlaylist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val resultFromData = roomStorage.getAllPlaylists()

        emit(resultFromData.map { playlistDto ->
            playlistConverter.mapPlaylistToDomain(playlistDto)
        })

    }

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        val resultFromData = roomStorage.getPlaylist(playlistId)

        emit(playlistConverter.mapPlaylistToDomain(playlistDto = resultFromData))
    }

    override suspend fun addTrackToSaved(track: Track) {
        val mappedTrack = savedDbConverter.mapTrackToData(track)
        roomStorage.addTrackToSaved(savedTrackDto = mappedTrack)
    }
}