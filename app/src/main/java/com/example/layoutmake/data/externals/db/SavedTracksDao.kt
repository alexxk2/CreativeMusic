package com.example.layoutmake.data.externals.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.layoutmake.data.externals.db.dto.SavedTrackDto

@Dao
interface SavedTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewItem(savedTrackDto: SavedTrackDto)
}