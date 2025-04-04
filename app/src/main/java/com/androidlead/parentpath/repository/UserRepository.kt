package com.androidlead.parentpath.repository

import android.content.Context
import com.androidlead.parentpath.data.local.AppDatabase
import com.androidlead.parentpath.data.local.User
import com.androidlead.parentpath.data.local.UserEntity

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()

    fun register(user: User): Boolean {
        if (userDao.getUserByEmail(user.email) != null) return false
        userDao.insertUser(UserEntity(user.email, user.password))
        return true
    }

    fun login(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return user?.password == password
    }

    fun getUser(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}
