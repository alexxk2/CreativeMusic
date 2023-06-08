package com.example.layoutmake.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.domain.search.AddTrackToHistoryUseCase
import com.example.layoutmake.domain.search.CheckHistoryExistenceUseCase
import com.example.layoutmake.domain.search.ClearSearchHistoryUseCase
import com.example.layoutmake.domain.search.GetSearchHistoryUseCase
import com.example.layoutmake.domain.search.NetworkSearchUseCase
import com.example.layoutmake.domain.search.SearchHistoryListenerUseCase
import com.example.layoutmake.domain.search.StartHistoryListenerUseCase
import com.example.layoutmake.presentation.search.SearchingState
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val searchRepository: SearchRepository): ViewModel() {


    private val addTrackToHistoryUseCase = AddTrackToHistoryUseCase(searchRepository)
    private val getSearchHistoryUseCase = GetSearchHistoryUseCase(searchRepository)
    private val checkHistoryExistenceUseCase = CheckHistoryExistenceUseCase(searchRepository)
    private val networkSearchUseCase = NetworkSearchUseCase(searchRepository)
    private val clearSearchHistoryUseCase = ClearSearchHistoryUseCase(searchRepository)
    private val startHistoryListenerUseCase = StartHistoryListenerUseCase(searchRepository)
    private val searchHistoryListenerUseCase = SearchHistoryListenerUseCase(searchRepository)

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
                val response = networkSearchUseCase.execute(searchInput)
                if (response.isNotEmpty()){
                    _searchingState.value = SearchingState.Done
                    _trackList.value = response
                }
                else {
                    _searchingState.value = SearchingState.NoResults
                    _trackList.value = emptyList()
                }
            }
            catch (e: Exception){
                _searchingState.value = SearchingState.Error
            }
        }
    }


    companion object {
        fun getViewModelFactory(
            searchRepository: SearchRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(searchRepository)
                }
            }
    }
}