package com.example.layoutmake.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.layoutmake.data.externals.player.impl.MediaPlayerImpl
import com.example.layoutmake.data.externals.search.impl.HistoryManagerImpl
import com.example.layoutmake.data.externals.search.impl.NetworkClientImpl
import com.example.layoutmake.data.externals.settings.impl.ExternalNavigatorImpl
import com.example.layoutmake.data.externals.settings.impl.LocalSettingsImpl
import com.example.layoutmake.data.repositories.player.PlayerRepository
import com.example.layoutmake.data.repositories.player.impl.PlayerRepositoryImpl
import com.example.layoutmake.data.repositories.search.SearchRepository
import com.example.layoutmake.data.repositories.search.impl.SearchRepositoryImpl
import com.example.layoutmake.data.repositories.settings.SettingsRepository
import com.example.layoutmake.data.repositories.settings.impl.SettingsRepositoryImpl


object Creator {

    fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayerImpl())
    }

    fun getSearchRepository(sharedPref: SharedPreferences): SearchRepository {
        return SearchRepositoryImpl(
            HistoryManagerImpl(sharedPref = sharedPref),
            NetworkClientImpl()
        )
    }

    fun getSettingsRepository(context: Context, sharedPref: SharedPreferences): SettingsRepository {
        return SettingsRepositoryImpl(
            ExternalNavigatorImpl(context = context),
            LocalSettingsImpl(sharedPref = sharedPref)
        )
    }
}