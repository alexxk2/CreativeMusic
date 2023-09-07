package com.example.layoutmake.di

import com.example.layoutmake.domain.media.AddNewPlaylistUseCase
import com.example.layoutmake.domain.media.AddTrackToFavouriteUseCase
import com.example.layoutmake.domain.media.AddTrackToSavedUseCase
import com.example.layoutmake.domain.media.CheckIfPlaylistContainsTrackUseCase
import com.example.layoutmake.domain.media.DeleteAllPlaylistsUseCase
import com.example.layoutmake.domain.media.DeletePlaylistUseCase
import com.example.layoutmake.domain.media.GetAllFavouriteTracksUseCase
import com.example.layoutmake.domain.media.GetAllPlaylistsUseCase
import com.example.layoutmake.domain.media.GetFavouriteTracksIdsUseCase
import com.example.layoutmake.domain.media.GetPlaylistUseCase
import com.example.layoutmake.domain.media.RemoveTrackFromFavouriteUseCase
import com.example.layoutmake.domain.media.SaveImageUseCase
import com.example.layoutmake.domain.media.UpdatePlaylistUseCase
import com.example.layoutmake.domain.player.use_cases.GetPlayerStateUseCase
import com.example.layoutmake.domain.player.use_cases.GetTrackCurrentPositionUseCase
import com.example.layoutmake.domain.player.use_cases.PauseSongUseCase
import com.example.layoutmake.domain.player.use_cases.PlaySongUseCase
import com.example.layoutmake.domain.player.use_cases.PreparePlayerUseCase
import com.example.layoutmake.domain.player.use_cases.ReleasePlayerUseCase
import com.example.layoutmake.domain.search.use_cases.AddTrackToHistoryUseCase
import com.example.layoutmake.domain.search.use_cases.CheckHistoryExistenceUseCase
import com.example.layoutmake.domain.search.use_cases.ClearSearchHistoryUseCase
import com.example.layoutmake.domain.search.use_cases.GetSearchHistoryUseCase
import com.example.layoutmake.domain.search.use_cases.NetworkSearchUseCase
import com.example.layoutmake.domain.search.use_cases.SearchHistoryListenerUseCase
import com.example.layoutmake.domain.search.use_cases.StartHistoryListenerUseCase
import com.example.layoutmake.domain.settings.use_cases.CheckingDarkModeUseCase
import com.example.layoutmake.domain.settings.use_cases.CreateBrowserIntentUseCase
import com.example.layoutmake.domain.settings.use_cases.CreateMessageIntentUseCase
import com.example.layoutmake.domain.settings.use_cases.CreateShareIntentUseCase
import org.koin.dsl.module


val domainModule = module {

    factory<GetPlayerStateUseCase> { GetPlayerStateUseCase(playerRepository = get()) }
    factory<GetTrackCurrentPositionUseCase> { GetTrackCurrentPositionUseCase(playerRepository = get()) }
    factory<PauseSongUseCase> { PauseSongUseCase(playerRepository = get()) }
    factory<PlaySongUseCase> { PlaySongUseCase(playerRepository = get()) }
    factory<PreparePlayerUseCase> { PreparePlayerUseCase(playerRepository = get()) }
    factory<ReleasePlayerUseCase> { ReleasePlayerUseCase(playerRepository = get()) }

    factory<AddTrackToHistoryUseCase> { AddTrackToHistoryUseCase(searchRepository = get()) }
    factory<CheckHistoryExistenceUseCase> { CheckHistoryExistenceUseCase(searchRepository = get()) }
    factory<ClearSearchHistoryUseCase> { ClearSearchHistoryUseCase(searchRepository = get()) }
    factory<GetSearchHistoryUseCase> { GetSearchHistoryUseCase(searchRepository = get()) }
    factory<NetworkSearchUseCase> { NetworkSearchUseCase(searchRepository = get()) }
    factory<SearchHistoryListenerUseCase> { SearchHistoryListenerUseCase(searchRepository = get()) }
    factory<StartHistoryListenerUseCase> { StartHistoryListenerUseCase(searchRepository = get()) }

    factory<CheckingDarkModeUseCase> { CheckingDarkModeUseCase(settingsRepository = get()) }
    factory<CreateBrowserIntentUseCase> { CreateBrowserIntentUseCase(settingsRepository = get()) }
    factory<CreateMessageIntentUseCase> { CreateMessageIntentUseCase(settingsRepository = get()) }
    factory<CreateShareIntentUseCase> { CreateShareIntentUseCase(settingsRepository = get()) }

    factory<AddTrackToFavouriteUseCase> { AddTrackToFavouriteUseCase(mediaRepository = get()) }
    factory<RemoveTrackFromFavouriteUseCase> { RemoveTrackFromFavouriteUseCase(mediaRepository = get()) }
    factory<GetAllFavouriteTracksUseCase> { GetAllFavouriteTracksUseCase(mediaRepository = get()) }
    factory<GetFavouriteTracksIdsUseCase> { GetFavouriteTracksIdsUseCase(mediaRepository = get()) }


    factory<AddNewPlaylistUseCase> { AddNewPlaylistUseCase(mediaRepository = get()) }
    factory<DeleteAllPlaylistsUseCase> { DeleteAllPlaylistsUseCase(mediaRepository = get()) }
    factory<DeletePlaylistUseCase> { DeletePlaylistUseCase(mediaRepository = get()) }
    factory<UpdatePlaylistUseCase> { UpdatePlaylistUseCase(mediaRepository = get()) }
    factory<GetPlaylistUseCase> { GetPlaylistUseCase(mediaRepository = get()) }
    factory<GetAllPlaylistsUseCase> { GetAllPlaylistsUseCase(mediaRepository = get()) }

    factory<AddTrackToSavedUseCase> { AddTrackToSavedUseCase(mediaRepository = get()) }

    factory<SaveImageUseCase> { SaveImageUseCase(mediaRepository = get()) }

    factory<CheckIfPlaylistContainsTrackUseCase> { CheckIfPlaylistContainsTrackUseCase() }
}