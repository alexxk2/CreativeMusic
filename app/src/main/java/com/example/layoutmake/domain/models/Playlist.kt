package com.example.layoutmake.domain.models



data class Playlist(
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val coverSrc: String?,
    val tracksIds: List<Int>,
    val tracksNumber: Int
)
