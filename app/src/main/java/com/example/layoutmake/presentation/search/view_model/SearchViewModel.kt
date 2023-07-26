package com.example.layoutmake.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.search.use_cases.AddTrackToHistoryUseCase
import com.example.layoutmake.domain.search.use_cases.CheckHistoryExistenceUseCase
import com.example.layoutmake.domain.search.use_cases.ClearSearchHistoryUseCase
import com.example.layoutmake.domain.search.use_cases.GetSearchHistoryUseCase
import com.example.layoutmake.domain.search.use_cases.NetworkSearchUseCase
import com.example.layoutmake.domain.search.use_cases.SearchHistoryListenerUseCase
import com.example.layoutmake.domain.search.use_cases.StartHistoryListenerUseCase
import com.example.layoutmake.presentation.search.SearchingState
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(
    private val addTrackToHistoryUseCase: AddTrackToHistoryUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val checkHistoryExistenceUseCase: CheckHistoryExistenceUseCase,
    private val networkSearchUseCase: NetworkSearchUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val startHistoryListenerUseCase: StartHistoryListenerUseCase,
    private val searchHistoryListenerUseCase: SearchHistoryListenerUseCase,
) : ViewModel() {

    private val _trackList = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>> = _trackList

    private val _searchingState = MutableLiveData<SearchingState>()
    val searchingState: LiveData<SearchingState> = _searchingState

    private val _isHistoryChanged = MutableLiveData<Boolean>()
    val isHistoryChanged: LiveData<Boolean> = _isHistoryChanged

    init {
        startHistoryListenerUseCase.execute()
        viewModelScope.launch {
            searchHistoryListenerUseCase.execute().collect{isHistoryChanged->
                _isHistoryChanged.postValue(isHistoryChanged)
            }
        }
    }

    fun getSearchHistory(): MutableList<Track> = getSearchHistoryUseCase.execute()

    fun addTrackToHistory(track: Track) = addTrackToHistoryUseCase.execute(track)

    fun doesHistoryExist(): Boolean = checkHistoryExistenceUseCase.execute()

    fun clearSearchHistory() = clearSearchHistoryUseCase.execute()

    fun startSearch(searchInput: String) {
        _searchingState.value = SearchingState.Loading

        viewModelScope.launch {
            try {
                networkSearchUseCase.execute(searchInput).collect{
                    if (it.isNotEmpty()){
                        _searchingState.value = SearchingState.Done
                        _trackList.value = it
                    }
                    else {
                        _searchingState.value = SearchingState.NoResults
                        _trackList.value = emptyList()
                    }
                }
            }
            catch (e: Exception){
                _searchingState.value = SearchingState.Error
            }
        }
    }
}