package com.dicoding.myactionbar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myactionbar.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    companion object {
        const val ARG_NAME = "Name"
        private const val TAG = "MainActivity"
    }
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val extraName = arguments?.getString(ARG_NAME)

        val layoutManager = LinearLayoutManager(this.requireActivity())
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this.requireActivity(), layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        if (extraName != null) {
            findUser(extraName)
        }

    }
    private fun findUser(param : String) {
        showLoading(true)
        val client = ApiConfig.getFollowersApiService().getFollowers(param)
        client.enqueue(object : Callback<GithubFollowersResponse>{
            override fun onResponse(
                call: Call<GithubFollowersResponse>,
                response: Response<GithubFollowersResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUsersData(responseBody.githubFollowersResponse)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubFollowersResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUsersData(consumerReviews: List<GithubFollowersResponseItem>) {
        val listReview = ArrayList<String>()
            for (review in consumerReviews) {
                listReview.add(
                    """
                    ${review.login}
                    - ${review.htmlUrl}
                    """.trimIndent()
                )
            }
        val adapter = FollowerAdapter(listReview,consumerReviews)
        binding.rvReview.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}