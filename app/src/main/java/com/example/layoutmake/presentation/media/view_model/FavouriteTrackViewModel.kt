package com.example.layoutmake.presentation.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.GetAllFavouriteTracksUseCase
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.media.model.FavouriteScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouriteTrackViewModel(
    private val getAllFavouriteTracksUseCase: GetAllFavouriteTracksUseCase

) : ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null


    private val _screenState = MutableLiveData<FavouriteScreenState>()
    val screenState: LiveData<FavouriteScreenState> = _screenState

    val favouriteList: LiveData<List<Track>> = getAllFavouriteTracksUseCase.execute().asLiveData()

    private val _trackList = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>> = _trackList

    fun getAllFavouriteTracks() {

        viewModelScope.launch {
            try {
                getAllFavouriteTracksUseCase.execute().collect { tracks ->
                    if (tracks.isNotEmpty()) {
                        _screenState.value = FavouriteScreenState.Content(tracks)
                        _trackList.value = tracks
                    } else {
                        _trackList.value = tracks
                        _screenState.value = FavouriteScreenState.Empty
                    }
                }
            } catch (e: Exception) {
                _screenState.value = FavouriteScreenState.Empty
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            clickJob = viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}