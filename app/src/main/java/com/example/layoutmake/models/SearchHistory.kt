package com.example.layoutmake.models

import android.content.SharedPreferences
import com.example.layoutmake.HISTORY_LIST
import com.google.gson.Gson

class SearchHistory(private val sharedPref: SharedPreferences) {

    fun historyList(): MutableList<Track> {
        val jSonList = sharedPref.getString(HISTORY_LIST, null)
        return if (jSonList != null) Gson().fromJson(jSonList, Array<Track>::class.java).toMutableList()
        else mutableListOf()
    }

    fun manageTrackHistory(track: Track){
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
}