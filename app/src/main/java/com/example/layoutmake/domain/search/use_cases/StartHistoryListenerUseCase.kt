package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.data.repositories.search.SearchRepository

class StartHistoryListenerUseCase(private val searchRepository: SearchRepository) {

    fun execute() = searchRepository.startHistoryListener()
}