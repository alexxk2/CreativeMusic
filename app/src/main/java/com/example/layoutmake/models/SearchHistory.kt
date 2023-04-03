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
}