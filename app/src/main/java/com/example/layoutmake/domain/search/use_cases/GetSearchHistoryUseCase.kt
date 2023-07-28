package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.domain.repositories.SearchRepository
import com.example.layoutmake.domain.models.Track

class GetSearchHistoryUseCase(private val searchRepository: SearchRepository) {

    fun execute(): MutableList<Track> = searchRepository.getSearchHistory()
}