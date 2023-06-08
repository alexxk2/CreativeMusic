package com.example.layoutmake.domain.search


import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.domain.models.Track
import java.util.concurrent.Executors

class NetworkSearchUseCase(private val searchRepository: SearchRepository) {


    suspend fun execute(searchInput: String): List<Track> {

       return searchRepository.startSearch(searchInput)

    }

}