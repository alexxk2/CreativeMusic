package com.example.layoutmake.presentation.search

sealed interface SearchingState{
    object Loading: SearchingState

    object Done: SearchingState

    object NoResults: SearchingState

    object Error: SearchingState
}