package com.dicoding.myactionbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myactionbar.adapter.FollowerAdapter
import com.dicoding.myactionbar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    companion object {
        const val ARG_NAME = "Name"
        const val ARG_SECTION_NUMBER = "SectionNum"
        private const val TAG = "HomeFragment"
    }
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val extraName = arguments?.getString(ARG_NAME)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        val fragmentViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FragmentViewModel::class.java]
        
        fragmentViewModel.user.observe(viewLifecycleOwner) { user ->
            setUsersData(user)
        }

        val layoutManager = LinearLayoutManager(this.requireActivity())
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this.requireActivity(), layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        fragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        fragmentViewModel.errorMsg.observe(viewLifecycleOwner) {
            showError(it)
        }

        if (extraName != null) {
            fragmentViewModel.findUser(extraName,when(index) {
                1 -> "followers"
                2 -> "following"
                else -> {"invalid"}
            })
        }

    }
    private fun setUsersData(consumerReviews: List<GithubFollowersResponseItem>?) {
        val listReview = ArrayList<String>()
        if (consumerReviews != null) {
            for (review in consumerReviews) {
                listReview.add(
                    """
                        ${review.login}
                        - ${review.htmlUrl}
                    """.trimIndent()
                )
            }
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
    private fun showError(paramErr: String) {
        if (paramErr == "invalid") {
            binding.apply {
                rvReview.visibility = View.VISIBLE
                errorMsg.visibility = View.GONE
            }
        }
        else {
            binding.apply {
                errorMsg.visibility = View.VISIBLE
                rvReview.visibility = View.GONE
                errorMsg.text = paramErr
            }
        }
    }
}