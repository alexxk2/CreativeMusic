package com.example.layoutmake.domain.search.use_cases

import com.example.layoutmake.domain.repositories.SearchRepository

class CheckHistoryExistenceUseCase(private val searchRepository: SearchRepository) {

    fun execute(): Boolean = searchRepository.doesHistoryExist()
}