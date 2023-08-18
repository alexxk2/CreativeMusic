package com.example.layoutmake.data.externals.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.layoutmake.data.externals.search.dto.TrackDto

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewItem(trackDto: TrackDto)

    @Delete
    suspend fun deleteItem(trackDto: TrackDto)

    @Query("SELECT * FROM favourite ORDER BY date DESC")
    suspend fun gelAllItems(): List<TrackDto>

    @Query("SELECT track_id FROM favourite")
    suspend fun getAllIds(): List<Int>

}