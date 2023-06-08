package com.example.layoutmake.data.externals.search.impl

import android.content.SharedPreferences
import com.example.layoutmake.HISTORY_LIST
import com.example.layoutmake.data.externals.search.HistoryManager
import com.example.layoutmake.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class HistoryManagerImpl(private val sharedPref: SharedPreferences) : HistoryManager {

    private var isHistoryChanged = MutableStateFlow(false)
    private var tempHistoryChangeStatus = false

    override fun getSearchHistory(): MutableList<Track> {
        val jSonList = sharedPref.getString(HISTORY_LIST, null)
        return if (jSonList != null) Gson().fromJson(jSonList, Array<Track>::class.java)
            .toMutableList()
        else mutableListOf()
    }

    override fun addTrackToHistory(track: Track) {
        val trackListJSon = sharedPref.getString(HISTORY_LIST, null)

        if (trackListJSon != null) {
            val trackList =
                Gson().fromJson(trackListJSon, Array<Track>::class.java).toMutableList()

            if (trackList.contains(track)) trackList.remove(track)
            else if (trackList.size == 10) trackList.removeLast()

            trackList.add(0, track)

            sharedPref.edit()
                .putString(HISTORY_LIST, Gson().toJson(trackList))
                .apply()

        } else {
            val defValue = Gson().toJson(arrayOf(track))
            sharedPref.edit()
                .putString(HISTORY_LIST, defValue)
                .apply()
        }
    }

    override fun doesHistoryExist(): Boolean {
        val trackListJSon = sharedPref.getString(HISTORY_LIST, null)
        return trackListJSon != null
    }


    override fun clearSearchHistory() {
        sharedPref.edit()
            .putString(HISTORY_LIST, null)
            .apply()
    }

    override fun isHistoryChanged(): Flow<Boolean> = isHistoryChanged

    override fun startHistoryListener() {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == HISTORY_LIST) {

                isHistoryChanged.value = true
            }
            isHistoryChanged.value = false
        }
        sharedPref.registerOnSharedPreferenceChangeListener(listener)
    }
}