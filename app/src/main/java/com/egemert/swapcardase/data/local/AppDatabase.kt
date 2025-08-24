package com.egemert.swapcardcase.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.egemert.swapcardcase.data.local.dao.BookmarkedUserDao
import com.egemert.swapcardcase.data.local.entity.BookmarkedUser

@Database(
    entities = [BookmarkedUser::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkedUserDao(): BookmarkedUserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "swapcard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
