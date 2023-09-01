package com.example.layoutmake.data.converters

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
                coverSrc = coverSrc,
                tracksIds = convertToJson(tracksIds),
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
                coverSrc = coverSrc,
                tracksIds = convertFromJson(tracksIds),
                tracksNumber = tracksNumber
            )
        }
    }

    private fun convertFromJson(jsonString: String): List<Int> {
        val typeToken = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson<List<Int>>(jsonString, typeToken)
    }

    private fun convertToJson(list: List<Int>) = Gson().toJson(list)
}