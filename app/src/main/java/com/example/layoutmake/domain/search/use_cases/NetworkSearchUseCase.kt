package com.example.layoutmake.domain.search.use_cases


import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

class NetworkSearchUseCase(private val searchRepository: SearchRepository) {


     fun execute(searchInput: String): Flow<List<Track>> {
       return searchRepository.startSearch(searchInput)
    }

}