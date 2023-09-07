package com.example.layoutmake.data.converters

import com.example.layoutmake.data.externals.db.dto.SavedTrackDto
import com.example.layoutmake.domain.models.Track

class SavedDbConverter {

    fun mapTrackToData(track: Track): SavedTrackDto {
        with(track) {
            return SavedTrackDto(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
                isFavourite = true,
                date = date
            )
        }
    }
}