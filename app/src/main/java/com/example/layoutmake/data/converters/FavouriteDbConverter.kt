package com.example.layoutmake.data.converters

import com.example.layoutmake.data.externals.search.dto.TrackDto
import com.example.layoutmake.domain.models.Track

class FavouriteDbConverter {

    fun mapTrackToData(track: Track): TrackDto {
        with(track) {
            return TrackDto(
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
                date = date,
                artworkUrl60 = artworkUrl60
            )
        }

    }

    fun mapTrackToDomain(trackDto: TrackDto): Track {
        with(trackDto) {
            return Track(
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
                isFavourite = isFavourite,
                date = date,
                artworkUrl60 = artworkUrl60
            )
        }
    }

    fun mapTrackToDomainFavourite(trackDto: TrackDto): Track {
        with(trackDto) {
            return Track(
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
                date = date,
                artworkUrl60 = artworkUrl60
            )
        }
    }
}