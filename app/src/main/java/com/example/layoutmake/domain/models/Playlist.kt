package com.example.layoutmake.domain.models

import android.net.Uri


data class Playlist(
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val coverSrc: Uri?,
    val tracksIds: List<Int>,
    val tracksNumber: Int
)
