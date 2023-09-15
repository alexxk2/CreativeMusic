package com.example.layoutmake.data.converters

import androidx.core.net.toUri
import com.example.layoutmake.data.externals.db.dto.PlaylistDto
import com.example.layoutmake.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter {

    fun mapPlaylistToData(playlist: Playlist): PlaylistDto {
        with(playlist) {
            return PlaylistDto(
                playlistId = playlistId,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                coverSrc = coverSrc?.toString(),
                tracksIds = convertListOfIdsToJson(tracksIds),
                tracksNumber = tracksNumber
            )
        }
    }

    fun mapPlaylistToDomain(playlistDto: PlaylistDto): Playlist {
        with(playlistDto) {
            return Playlist(
                playlistId = playlistId,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                coverSrc = coverSrc?.toUri(),
                tracksIds = convertListOfIdsFromJson(tracksIds),
                tracksNumber = tracksNumber
            )
        }
    }

    fun convertListOfIdsFromJson(jsonString: String): List<Pair<Int, Long>> {
        val typeToken = object : TypeToken<List<Pair<Int, Long>>>() {}.type
        return Gson().fromJson<List<Pair<Int, Long>>>(jsonString, typeToken)
    }

     fun convertListOfIdsToJson(list: List<Pair<Int, Long>>) = Gson().toJson(list)



}