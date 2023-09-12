package com.example.layoutmake.data.externals.settings

interface ExternalNavigator {
    fun createBrowserIntent()
    fun createMessageIntent()
    fun createShareIntent()
    fun sharePlaylist(playlist: String)
}