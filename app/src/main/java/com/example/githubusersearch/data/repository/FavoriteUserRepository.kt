package com.example.githubusersearch.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubusersearch.data.local.database.FavoriteUser
import com.example.githubusersearch.data.local.database.FavoriteUserDao
import com.example.githubusersearch.data.local.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository (application: Application) {
    private val mfavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mfavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mfavoriteUserDao.getAllFavorite()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute {
            mfavoriteUserDao.insert(favoriteUser)
        }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute {
            mfavoriteUserDao.delete(favoriteUser)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mfavoriteUserDao.getFavoriteUserByUsername(username)
    }
}