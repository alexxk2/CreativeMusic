package com.example.layoutmake.data.repositories.search

import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchHistory(): MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun doesHistoryExist(): Boolean
    fun startSearch(searchInput: String): Flow<List<Track>>
    fun clearSearchHistory()
    fun isHistoryChanged(): Flow<Boolean>
    fun startHistoryListener()
}