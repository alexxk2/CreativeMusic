package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchHistoryListenerUseCase(private val searchRepository: SearchRepository) {

    fun execute(): Flow<Boolean> = searchRepository.isHistoryChanged()
}