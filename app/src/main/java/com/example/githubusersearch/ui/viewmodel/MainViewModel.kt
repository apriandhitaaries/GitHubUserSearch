package com.example.githubusersearch.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusersearch.data.remote.response.GitHubResponse
import com.example.githubusersearch.data.remote.response.ItemsItem
import com.example.githubusersearch.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUser = MutableLiveData<List<ItemsItem?>?>()
    val listUser: LiveData<List<ItemsItem?>?> = _listUser

    init {
        getUser()
    }

    internal fun getUser(username: String? = null) {
        _isLoading.value = true
        val client = if (username != null) {
            ApiConfig.getApiService().getUsers(username)
        } else {
            // default parameter
            ApiConfig.getApiService().getUsers("apriandhita")
        }

        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.postValue(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}