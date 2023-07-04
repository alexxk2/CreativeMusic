package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.data.repositories.search.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchHistoryListenerUseCase(private val searchRepository: SearchRepository) {

    fun execute(): Flow<Boolean> = searchRepository.isHistoryChanged()
}