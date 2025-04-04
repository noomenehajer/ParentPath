package com.androidlead.parentpath.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): UserEntity?
}