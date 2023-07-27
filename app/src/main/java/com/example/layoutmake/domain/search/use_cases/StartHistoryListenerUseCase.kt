package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.domain.repositories.SearchRepository

class StartHistoryListenerUseCase(private val searchRepository: SearchRepository) {

    fun execute() = searchRepository.startHistoryListener()
}