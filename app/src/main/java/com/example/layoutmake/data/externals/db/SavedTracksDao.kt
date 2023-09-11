package com.example.layoutmake.data.externals.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.layoutmake.data.externals.db.dto.SavedTrackDto
import com.example.layoutmake.data.externals.search.dto.TrackDto

@Dao
interface SavedTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewItem(savedTrackDto: SavedTrackDto)

    @Query("SELECT * FROM saved")
    suspend fun getAllItems(): List<SavedTrackDto>

    @Delete
    suspend fun deleteItem(savedTrackDto: SavedTrackDto)
}