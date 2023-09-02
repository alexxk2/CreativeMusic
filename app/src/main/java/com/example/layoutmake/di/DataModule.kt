package com.example.layoutmake.di

import com.example.layoutmake.data.converters.FavouriteDbConverter
import com.example.layoutmake.data.converters.PlaylistDbConverter
import com.example.layoutmake.data.converters.SavedDbConverter
import com.example.layoutmake.data.externals.db.FavouriteDatabase
import com.example.layoutmake.data.externals.db.RoomStorage
import com.example.layoutmake.data.externals.db.impl.RoomStorageImpl
import com.example.layoutmake.data.externals.player.Player
import com.example.layoutmake.data.externals.player.impl.MediaPlayerImpl
import com.example.layoutmake.data.externals.search.HistoryManager
import com.example.layoutmake.data.externals.search.NetworkClient
import com.example.layoutmake.data.externals.search.impl.HistoryManagerImpl
import com.example.layoutmake.data.externals.search.impl.NetworkClientImpl
import com.example.layoutmake.data.externals.settings.ExternalNavigator
import com.example.layoutmake.data.externals.settings.LocalSettings
import com.example.layoutmake.data.externals.settings.impl.ExternalNavigatorImpl
import com.example.layoutmake.data.externals.settings.impl.LocalSettingsImpl
import com.example.layoutmake.data.repositories.media.MediaRepositoryImpl
import com.example.layoutmake.domain.repositories.PlayerRepository
import com.example.layoutmake.data.repositories.player.impl.PlayerRepositoryImpl
import com.example.layoutmake.domain.repositories.SearchRepository
import com.example.layoutmake.data.repositories.search.impl.SearchRepositoryImpl
import com.example.layoutmake.domain.repositories.SettingsRepository
import com.example.layoutmake.data.repositories.settings.impl.SettingsRepositoryImpl
import com.example.layoutmake.domain.repositories.MediaRepository
import org.koin.dsl.module


val dataModule = module {

    single<Player> { MediaPlayerImpl() }

    single<NetworkClient> { NetworkClientImpl() }

    single<HistoryManager> { HistoryManagerImpl(context = get()) }

    single<LocalSettings> { LocalSettingsImpl(context = get()) }

    single<ExternalNavigator> { ExternalNavigatorImpl(context = get()) }

    single<PlayerRepository> { PlayerRepositoryImpl(mediaPlayer = get()) }

    single<SearchRepository> {
        SearchRepositoryImpl(
            historyManager = get(),
            networkClient = get(),
            roomStorage = get(),
            converter = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            externalNavigator = get(),
            localSettings = get()
        )
    }


    single<RoomStorage> { RoomStorageImpl(favouriteDatabase = get()) }

    single<FavouriteDatabase> { FavouriteDatabase.getDataBase(get()) }

    single<FavouriteDbConverter> { FavouriteDbConverter() }

    single<MediaRepository> {
        MediaRepositoryImpl(
            roomStorage = get(),
            favouriteConverter = get(),
            playlistConverter = get(),
            savedDbConverter = get()
        )
    }


    single<PlaylistDbConverter> { PlaylistDbConverter() }


    single<SavedDbConverter> { SavedDbConverter() }
}