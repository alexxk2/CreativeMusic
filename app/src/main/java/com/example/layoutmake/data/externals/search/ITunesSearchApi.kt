package com.example.layoutmake.data.externals.search

import com.example.layoutmake.data.externals.search.dto.TrackResponseEntity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {

    @GET("/search?entity=song")
    suspend fun findSong(@Query("term") searchInput: String):Response<TrackResponseEntity>
}