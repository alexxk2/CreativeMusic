package com.example.layoutmake.presentation.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.GetAllPlaylistsUseCase
import com.example.layoutmake.domain.models.Playlist
import com.example.layoutmake.presentation.media.model.FavouriteScreenState
import com.example.layoutmake.presentation.media.model.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase

): ViewModel() {


    private val _screenState = MutableLiveData<PlaylistsScreenState>()
    val screenState: LiveData<PlaylistsScreenState> = _screenState

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists


    fun getAllPlaylists(){
        viewModelScope.launch {

            getAllPlaylistsUseCase.execute().collect{newList->
                _playlists.value = newList
                _screenState.value =
                    if (playlists.value?.isEmpty()==true) PlaylistsScreenState.Empty else PlaylistsScreenState.Content
            }
        }
    }

}