package com.example.layoutmake.data.externals.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists")
data class PlaylistDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("playlist_id") val playlistId: Int = 0,
    @ColumnInfo("playlist_name") val playlistName: String,
    @ColumnInfo("playlist_description") val playlistDescription: String,
    @ColumnInfo("cover_src") val coverSrc: String,
    @ColumnInfo("tracks_ids") val tracksIds: String,
    @ColumnInfo("tracks_number") val tracksNumber: Int
)
