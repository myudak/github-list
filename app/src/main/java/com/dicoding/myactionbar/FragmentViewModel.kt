package com.dicoding.myactionbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myactionbar.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentViewModel: ViewModel() {


    private val _user = MutableLiveData<List<GithubFollowersResponseItem>>()
    val user: LiveData<List<GithubFollowersResponseItem>> = _user
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    companion object{
        private const val TAG = "FragmentViewModel"
    }

    fun findUser(paramName : String,paramMethod: String) {
        _isLoading.value = true

        val client = ApiConfig.getUserDetailApiService().getFollowUsers(paramName,paramMethod)

        client.enqueue(object : Callback<List<GithubFollowersResponseItem>>{
            override fun onResponse(
                call: Call<List<GithubFollowersResponseItem>>,
                response: Response<List<GithubFollowersResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _errorMsg.value =  "onFailure: ${response.message()}"
                }
            }
            override fun onFailure(call: Call<List<GithubFollowersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value =  "onFailure: ${t.message}"
            }
        })
    }
}