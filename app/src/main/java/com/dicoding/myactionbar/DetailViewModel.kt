package com.dicoding.myactionbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myactionbar.api.ApiConfig
import com.dicoding.myactionbar.api.GithubDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {


    private val _user = MutableLiveData<GithubDetailResponse>()
    val user: LiveData<GithubDetailResponse> = _user
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    fun findUser(paramName : String) {
        _isLoading.value = true

        val client = ApiConfig.getUserDetailApiService().getDetailUsers(paramName)

        client.enqueue(object : Callback<GithubDetailResponse> {
            override fun onResponse(
                call: Call<GithubDetailResponse>,
                response: Response<GithubDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMsg.value = "invalid"
                    _user.value = response.body()
                } else {
                    _errorMsg.value =  "onFailure: ${response.message()}"
                }
            }
            override fun onFailure(call: Call<GithubDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value =  "onFailure: ${t.message}"
            }
        })
    }
}