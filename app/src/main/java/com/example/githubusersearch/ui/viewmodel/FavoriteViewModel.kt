package com.example.githubusersearch.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusersearch.data.local.database.FavoriteUser
import com.example.githubusersearch.data.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) :ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavorite()

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun checkFavoriteUser(username: String, lifecycleOwner: LifecycleOwner) {
        mFavoriteUserRepository.getFavoriteUserByUsername(username).observe(lifecycleOwner) { favoriteUser ->
            if (favoriteUser != null) {
                _isFavorite.postValue(true)
            } else {
                _isFavorite.postValue(false)
            }
        }
    }
}