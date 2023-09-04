package com.example.layoutmake.di

import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.media.view_model.FavouriteTrackViewModel
import com.example.layoutmake.presentation.media.view_model.NewPlaylistViewModel
import com.example.layoutmake.presentation.media.view_model.PlaylistsViewModel
import com.example.layoutmake.presentation.player.view_model.PlayerViewModel
import com.example.layoutmake.presentation.search.view_model.SearchViewModel
import com.example.layoutmake.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<PlayerViewModel> { (track: Track) ->
        PlayerViewModel(
            track = track,
            getTrackCurrentPositionUseCase = get(),
            pauseSongUseCase = get(),
            playSongUseCase = get(),
            preparePlayerUseCase = get(),
            releasePlayerUseCase = get(),
            getPlayerStateUseCase = get(),
            addTrackToFavouriteUseCase = get(),
            removeTrackFromFavouriteUseCase = get(),
            getAllPlaylistsUseCase = get(),
            updatePlaylistUseCase = get(),
            addTrackToSavedUseCase = get(),
            checkIfPlaylistContainsTrackUseCase = get()
        )
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            addTrackToHistoryUseCase = get(),
            getSearchHistoryUseCase = get(),
            checkHistoryExistenceUseCase = get(),
            networkSearchUseCase = get(),
            clearSearchHistoryUseCase = get(),
            startHistoryListenerUseCase = get(),
            searchHistoryListenerUseCase = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            checkingDarkModeUseCase = get(),
            createBrowserIntentUseCase = get(),
            createMessageIntentUseCase = get(),
            createShareIntentUseCase = get()
        )
    }

    viewModel<FavouriteTrackViewModel> {
        FavouriteTrackViewModel(getAllFavouriteTracksUseCase = get())
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(getAllPlaylistsUseCase = get())
    }

    viewModel<NewPlaylistViewModel> {
        NewPlaylistViewModel(
            addNewPlaylistUseCase = get(),
            saveImageAndReturnPathUseCase = get()
        )
    }
}