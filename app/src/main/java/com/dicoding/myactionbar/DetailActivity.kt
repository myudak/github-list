package com.dicoding.myactionbar

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.myactionbar.adapter.SectionsPagerAdapter
import com.dicoding.myactionbar.api.GithubDetailResponse
import com.dicoding.myactionbar.databinding.ActivityDetailBinding
import com.dicoding.myactionbar.db.DatabaseContract
import com.dicoding.myactionbar.db.FavoriteHelper
import com.dicoding.myactionbar.entity.Favorite
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_IMAGE = "image"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteHelper: FavoriteHelper
    private var favorite: Favorite? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = intent.getStringExtra(EXTRA_NAME)

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        detailViewModel.user.observe(this) { user ->
            setUsersData(user)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.extraName = intent.getStringExtra(EXTRA_NAME).toString()
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.errorMsg.observe(this){
            showError(it)
        }

        intent.getStringExtra(EXTRA_NAME)?.let { detailViewModel.findUser(it) }



        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@DetailActivity, FavoriteAddUpdateActivity::class.java)
            startActivity(intent)
        }
//
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        favorite = intent.getParcelableExtra(EXTRA_NAME)

        binding.btnContact.setOnClickListener{
            val values = ContentValues()
            values.put(DatabaseContract.FavoriteColumns.NAME, intent.getStringExtra(EXTRA_NAME))
            values.put(DatabaseContract.FavoriteColumns.IMAGE, intent.getStringExtra(EXTRA_IMAGE))


            val result = favoriteHelper.insert(values)
            if (result > 0) {
                favorite?.id = result.toInt()
            } else {
                Toast.makeText(this@DetailActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
            }
            val moveWithDataIntent = Intent(this@DetailActivity, FavoriteActivity::class.java)
            startActivity(moveWithDataIntent)
        }
        }

    private fun setUsersData(userDetail: GithubDetailResponse) {

        Glide.with(binding.tvImg.context)
            .load(userDetail.avatarUrl)
            .into(binding.tvImg)


        binding.apply {
            tvUsername.text =userDetail.login
            tvName.text =userDetail.name
            tvFollower.text =resources.getString(R.string.follower,userDetail.followers.toString())
            tvFollowing.text =resources.getString(R.string.following,userDetail.following.toString())
            tvCompany.text =resources.getString(R.string.company,userDetail.company)
            tvLocation.text =resources.getString(R.string.location,userDetail.location)
            tvRepository.text =resources.getString(R.string.repository,userDetail.htmlUrl)
            binding.tvDetail.text = userDetail.bio

            tvImg.setOnClickListener(){
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse(userDetail.htmlUrl))
                startActivity(intent)
            }
        }

    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun showError(errorMsg: String) {
        if (errorMsg == "invalid") {
            binding.errorMsg.visibility = View.GONE
        } else {
            binding.errorMsg.visibility = View.VISIBLE
            binding.errorMsg.text = errorMsg
        }
    }
}