package com.example.layoutmake.domain.search

import com.example.layoutmake.data.repositories.search.SearchRepository

class ClearSearchHistoryUseCase(private val repository: SearchRepository) {

    fun execute() = repository.clearSearchHistory()
}