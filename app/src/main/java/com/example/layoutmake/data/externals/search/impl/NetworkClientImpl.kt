package com.example.layoutmake.data.externals.search.impl

import com.example.layoutmake.data.externals.search.ITunesSearchApi
import com.example.layoutmake.data.externals.search.NetworkClient
import com.example.layoutmake.data.externals.search.dto.TrackRequestEntity
import com.example.layoutmake.data.externals.search.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl : NetworkClient {

    private val baseUrl = "http://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val searchingService = retrofit.create(ITunesSearchApi::class.java)


    override suspend fun startSearch(dto: Any): Response {

        if (dto is TrackRequestEntity) {

            val response = searchingService.findSong(dto.searchInput)
            val body = response.body() ?:Response()

            return body.apply { resultCode = response.code() }
        } else return Response().apply { resultCode = 400 }
    }
}