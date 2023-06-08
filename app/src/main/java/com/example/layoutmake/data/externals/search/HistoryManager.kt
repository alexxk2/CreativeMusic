package com.example.layoutmake.data.externals.search

import com.example.layoutmake.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryManager {
    fun getSearchHistory(): MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun doesHistoryExist(): Boolean
    fun clearSearchHistory()
    fun isHistoryChanged(): Flow<Boolean>
    fun startHistoryListener()
}