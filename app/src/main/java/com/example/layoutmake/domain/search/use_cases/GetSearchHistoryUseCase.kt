package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.domain.models.Track

class GetSearchHistoryUseCase(private val searchRepository: SearchRepository) {

    fun execute(): MutableList<Track> = searchRepository.getSearchHistory()
}