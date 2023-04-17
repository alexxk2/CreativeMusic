package com.example.layoutmake.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
): Parcelable{

    companion object{
        @JvmStatic val DEFAULT = Track(1,"default","default","default","default","default","default","default","default")
    }
}


