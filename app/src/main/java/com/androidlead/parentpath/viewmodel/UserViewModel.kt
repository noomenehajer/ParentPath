package com.androidlead.parentpath.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.androidlead.parentpath.data.local.User
import com.androidlead.parentpath.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository(application)

    fun registerUser(user: User): Boolean {
        return repo.register(user)
    }

    fun loginUser(email: String, password: String): Boolean {
        return repo.login(email, password)
    }
}