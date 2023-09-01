package com.example.layoutmake.presentation.media.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.layoutmake.domain.media.AddNewPlaylistUseCase
import com.example.layoutmake.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val addNewPlaylistUseCase: AddNewPlaylistUseCase
): ViewModel() {

    fun addNewPlaylist(playlist: Playlist){
        viewModelScope.launch {
            addNewPlaylistUseCase.execute(playlist)
        }
    }
}