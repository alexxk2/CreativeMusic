package com.example.layoutmake.domain.search

import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.domain.models.Track

class AddTrackToHistoryUseCase(private val searchRepository: SearchRepository) {

    fun execute(track: Track) = searchRepository.addTrackToHistory(track)

}