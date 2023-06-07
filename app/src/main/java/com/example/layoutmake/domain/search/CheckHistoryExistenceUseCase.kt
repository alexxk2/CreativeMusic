package com.example.layoutmake.domain.search

import com.example.layoutmake.data.repositories.search.SearchRepository

class CheckHistoryExistenceUseCase(private val searchRepository: SearchRepository) {

    fun execute(): Boolean = searchRepository.doesHistoryExist()
}