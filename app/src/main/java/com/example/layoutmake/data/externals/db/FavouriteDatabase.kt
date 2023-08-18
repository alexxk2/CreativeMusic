package com.example.layoutmake.data.externals.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.layoutmake.data.externals.search.dto.TrackDto


@Database(entities = [TrackDto::class], version = 3, exportSchema = false)
abstract class FavouriteDatabase: RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao

    companion object {

        private var INSTANCE: FavouriteDatabase? = null

        fun getDataBase(context: Context): FavouriteDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    FavouriteDatabase::class.java,
                    "favourite_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}