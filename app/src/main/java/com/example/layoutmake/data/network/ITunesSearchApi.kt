package com.example.layoutmake.data.network

import com.example.layoutmake.data.network.ResponseEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {

    @GET("/search?entity=song")
    fun findSong(@Query("term") text: String):Call<ResponseEntity>
}