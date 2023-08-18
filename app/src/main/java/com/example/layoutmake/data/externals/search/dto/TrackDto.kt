package com.example.layoutmake.data.externals.search.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
data class TrackDto(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "track_id") val trackId: Int,
    @ColumnInfo(name = "track_name") val trackName: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "track_time_millis") val trackTimeMillis: String,
    @ColumnInfo(name = "artwork_url_100") val artworkUrl100: String,
    @ColumnInfo(name = "collection_name") val collectionName: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "primary_genre_name") val primaryGenreName: String?,
    val country: String?,
    @ColumnInfo(name = "preview_url") val previewUrl: String?,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean = false,
    val date: Long = 0L
) {
}