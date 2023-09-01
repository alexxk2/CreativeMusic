package com.example.layoutmake.domain.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Playlist(
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val coverSrc: String,
    val tracksIds: List<Int>,
    val tracksNumber: Int
)
