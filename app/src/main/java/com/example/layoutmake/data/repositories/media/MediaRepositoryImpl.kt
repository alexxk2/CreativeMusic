package com.example.layoutmake.data.repositories.media

import android.net.Uri
import android.text.Editable
import com.example.layoutmake.data.converters.FavouriteDbConverter
import com.example.layoutmake.data.converters.PlaylistDbConverter
import com.example.layoutmake.data.converters.SavedDbConverter
import com.example.layoutmake.data.externals.db.RoomStorage
import com.example.layoutmake.data.externals.db.dto.PlaylistDto
import com.example.layoutmake.data.externals.db.dto.SavedTrackDto
import com.example.layoutmake.data.externals.media_storage.ImageSaver
import com.example.layoutmake.data.externals.settings.ExternalNavigator
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.repositories.MediaRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.*

class MediaRepositoryImpl(
    private val roomStorage: RoomStorage,
    private val imageSaver: ImageSaver,
    private val favouriteConverter: FavouriteDbConverter,
    private val playlistConverter: PlaylistDbConverter,
    private val savedDbConverter: SavedDbConverter,
    private val externalNavigator: ExternalNavigator
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
    }.flowOn(Dispatchers.IO)

    override fun getFavouriteTracksIds(): Flow<List<Int>> = flow {
        emit(roomStorage.getFavouriteTracksIds())
    }


    override suspend fun addNewPlaylist(
        playlistName: Editable?,
        playlistDescription: Editable?,
        uri: Uri?
    ) {

        val newPlaylistDto = PlaylistDto(
            playlistName = playlistName.toString(),
            playlistDescription = playlistDescription.toString(),
            coverSrc = uri?.toString(),
            tracksIds = Gson().toJson(emptyList<Int>()),
            tracksNumber = 0
        )

        roomStorage.addNewPlaylist(playlistDto = newPlaylistDto)
    }

    override suspend fun deleteAllPlaylists() {
        roomStorage.deleteAllPlaylists()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlistToDelete = roomStorage.getPlaylist(playlistId)
        roomStorage.deletePlaylist(playlistDto = playlistToDelete)


        var isContainsInPlaylists = false

        val allPlaylists = roomStorage.getAllPlaylists()

        val playlistTracks = roomStorage.getAllSavedTracks().filter { track ->
            playlistConverter.convertListOfIdsFromJson(playlistToDelete.tracksIds).map{it.first}
                .contains(track.trackId)
        }

        playlistTracks.forEach loop1@{ savedTrackDto ->

            allPlaylists.forEach loop2@{ anotherPlaylist ->
                if (playlistConverter.convertListOfIdsFromJson(anotherPlaylist.tracksIds).map{it.first}
                        .contains(savedTrackDto.trackId)
                ) {
                    isContainsInPlaylists = true
                    return@loop2
                }
            }
            if (!isContainsInPlaylists) {
                roomStorage.deleteTrackFromSaved(savedTrackDto)
            }
            isContainsInPlaylists = false
        }
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        val dateOfAdding = Calendar.getInstance().timeInMillis

        val tempList = mutableListOf<Pair<Int, Long>>()
        tempList.addAll(playlist.tracksIds)
        tempList.add(Pair(track.trackId, dateOfAdding))


        val updatedPlaylist = playlist.copy(
            tracksIds = tempList.toList(),
            tracksNumber = tempList.size
        )

        val mappedPlaylist = playlistConverter.mapPlaylistToData(updatedPlaylist)
        roomStorage.updatePlaylist(playlistDto = mappedPlaylist)

    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val resultFromData = roomStorage.getAllPlaylists()

        emit(resultFromData.map { playlistDto ->
            playlistConverter.mapPlaylistToDomain(playlistDto)
        })

    }.flowOn(Dispatchers.IO)

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        val resultFromData = roomStorage.getPlaylist(playlistId)

        emit(playlistConverter.mapPlaylistToDomain(playlistDto = resultFromData))
    }.flowOn(Dispatchers.IO)

    override suspend fun addTrackToSaved(track: Track) {
        val mappedTrack = savedDbConverter.mapTrackToData(track)
        roomStorage.addTrackToSaved(savedTrackDto = mappedTrack)
    }

    override fun getPlaylistTracks(listOfIds: List<Pair<Int, Long>>): Flow<List<Track>> = flow {
        val listOfTracksIds = listOfIds.map { it.first }

        val filteredListFromData =
            roomStorage.getAllSavedTracks()
                .filter { listOfTracksIds.contains(it.trackId) }

        val listOfTracksToSort = mutableListOf<SavedTrackDto>()

        listOfIds.forEach { pair ->
            filteredListFromData.forEach { track ->
                if (track.trackId == pair.first) listOfTracksToSort.add(track.copy(date = pair.second))

            }
        }

        emit(listOfTracksToSort.sortedByDescending { it.date }.map { savedTrackDto ->
            savedDbConverter.mapTrackToDomain(savedTrackDto)
        })

    }

    override suspend fun saveImageAndReturnPath(uri: Uri): Uri =
        imageSaver.saveImageAndReturnPath(uri)


    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int) {
        val playlistToUpdate = roomStorage.getPlaylist(playlistId)

        val updatedListOfIds =
            playlistConverter.convertListOfIdsFromJson(playlistToUpdate.tracksIds)
                .filterNot { it.first == track.trackId }


        val updatedPlaylistDto = playlistToUpdate.copy(
            tracksIds = playlistConverter.convertListOfIdsToJson(updatedListOfIds),
            tracksNumber = updatedListOfIds.size
        )
        roomStorage.updatePlaylist(updatedPlaylistDto)


        var isContainsInPlaylists = false
        val allPlaylists = roomStorage.getAllPlaylists()

        allPlaylists.forEach { playlist ->
            if (playlistConverter.convertListOfIdsFromJson(playlist.tracksIds).map { it.first }
                    .contains(track.trackId)
            ) {
                isContainsInPlaylists = true
                return@forEach
            }
        }
        if (!isContainsInPlaylists) {
            roomStorage.deleteTrackFromSaved(savedDbConverter.mapTrackToData(track))
        }

    }

    override suspend fun sharePlaylist(playlistId: Int) {
        var playlistString = ""
        val playlistToShare = roomStorage.getPlaylist(playlistId)
        val listOfIds = playlistConverter.convertListOfIdsFromJson(playlistToShare.tracksIds)
        val listOfTracksToShare = getPlaylistTracks(listOfIds)
        listOfTracksToShare.collect { playlistString = createPlaylistString(playlistToShare, it) }
        externalNavigator.sharePlaylist(playlist = playlistString)
    }

    private fun createPlaylistString(playlistDto: PlaylistDto, listOfTracks: List<Track>): String {
        var numberInList = 1

        var result = "${playlistDto.playlistName}\n${playlistDto.playlistDescription}\n${
            getRightEndingTracks(listOfTracks.size)
        }"
        listOfTracks.forEach {
            result += "\n${numberInList}.${it.artistName} - ${it.trackName} ${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTimeMillis.toLong())
            }"
            numberInList++
        }
        return result
    }

    private fun getRightEndingTracks(numberOfTracks: Int): String {
        val preLastDigit = numberOfTracks % 100 / 10

        if (preLastDigit == 1) {
            return "$numberOfTracks треков"
        }

        return when (numberOfTracks % 10) {
            1 -> "$numberOfTracks трек"
            2 -> "$numberOfTracks трека"
            3 -> "$numberOfTracks трека"
            4 -> "$numberOfTracks трека"
            else -> "$numberOfTracks треков"
        }
    }

    override suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: Editable?,
        playlistDescription: Editable?,
        uri: Uri?
    ) {
        val playlistDtoToUpdate = roomStorage.getPlaylist(playlistId)
        val updatedPlaylist = playlistDtoToUpdate.copy(
            playlistName = playlistName.toString(),
            playlistDescription = playlistDescription.toString(),
            coverSrc = uri.toString()
        )
        roomStorage.updatePlaylist(updatedPlaylist)
    }
}