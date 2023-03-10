package com.rbs.githubuser.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rbs.githubuser.db.model.DetailUserDB

@Dao
interface DetailUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: DetailUserDB)

    @Query("DELETE FROM detailuserdb WHERE username = :username")
    fun delete(username: String)

    @Query("SELECT * from detailuserdb ORDER BY id ASC")
    fun getAllFavoriteData(): LiveData<List<DetailUserDB>>

    @Query("SELECT * FROM detailuserdb WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<DetailUserDB>
}