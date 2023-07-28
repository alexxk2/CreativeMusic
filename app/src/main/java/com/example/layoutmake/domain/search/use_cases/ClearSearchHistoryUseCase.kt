package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.domain.repositories.SearchRepository

class ClearSearchHistoryUseCase(private val searchRepository: SearchRepository) {

    fun execute() = searchRepository.clearSearchHistory()
}