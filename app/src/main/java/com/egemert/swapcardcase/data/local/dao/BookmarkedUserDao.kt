package com.egemert.swapcardcase.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.egemert.swapcardcase.data.local.entity.BookmarkedUser
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedUserDao {
    @Query("SELECT * FROM bookmarked_users ORDER BY name ASC")
    fun getAllBookmarkedUsers(): Flow<List<BookmarkedUser>>
    
    @Query("SELECT * FROM bookmarked_users WHERE uuid = :uuid")
    suspend fun getBookmarkedUser(uuid: String): BookmarkedUser?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedUser(user: BookmarkedUser)
    
    @Delete
    suspend fun deleteBookmarkedUser(user: BookmarkedUser)
    
    @Query("DELETE FROM bookmarked_users")
    suspend fun deleteAllBookmarkedUsers()
}
