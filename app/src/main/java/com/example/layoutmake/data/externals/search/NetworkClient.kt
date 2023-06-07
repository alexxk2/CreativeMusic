package com.example.layoutmake.data.externals.search

import com.example.layoutmake.data.externals.search.dto.Response
import com.example.layoutmake.data.externals.search.dto.TrackDto

interface NetworkClient {
    suspend fun startSearch(dto: Any): Response
}