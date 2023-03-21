package com.example.layoutmake.sources

import com.example.layoutmake.sources.entities.ResponseEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {

    @GET("/search?entity=song")
    fun findSong(@Query("term") text: String):Call<ResponseEntity>
}