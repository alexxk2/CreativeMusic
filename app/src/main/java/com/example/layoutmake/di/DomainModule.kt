package com.example.layoutmake.di

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

}