package com.example.layoutmake.data.externals.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.layoutmake.data.externals.db.dto.PlaylistDto
import com.example.layoutmake.data.externals.search.dto.TrackDto


@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewItem(playlistDto: PlaylistDto)

    @Delete
    suspend fun deleteItem(playlistDto: PlaylistDto)

    @Update
    suspend fun updateItem(playlistDto: PlaylistDto)

    @Query("SELECT * FROM playlists")
    suspend fun gelAllItems(): List<PlaylistDto>

    @Query("SELECT * FROM playlists WHERE playlist_id=:id")
    suspend fun getItem(id: Int): PlaylistDto

    @Query("DELETE FROM playlists")
    suspend fun deleteAllItems()
}