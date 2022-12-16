package com.dicoding.myactionbar

import androidx.lifecycle.*
import com.dicoding.myactionbar.api.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {



    private val _restaurant = MutableLiveData<List<ItemsItem>>()
    val restaurant: LiveData<List<ItemsItem>> = _restaurant
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        findUser("myudak")
    }

    fun findUser(param : String) {
        _isLoading.value = true
        val client = ApiConfig.getUserApiService().getUsers(param)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMsg.value = "invalid"
                    _restaurant.value = response.body()?.items
                } else {
                    _errorMsg.value =  "onFailure: ${response.message()}"
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value =  "onFailure: ${t.message}"
            }
        })
    }
}