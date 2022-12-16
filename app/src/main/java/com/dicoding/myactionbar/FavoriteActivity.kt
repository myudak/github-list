package com.dicoding.myactionbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myactionbar.adapter.FavoriteAdapter
import com.dicoding.myactionbar.databinding.ActivityFavoriteBinding
import com.dicoding.myactionbar.db.FavoriteHelper
import com.dicoding.myactionbar.entity.Favorite
import com.dicoding.myactionbar.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATES = "EXTRA_STATUSSSS"
    }

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {
            // Akan dipanggil jika request codenya ADD
            when (result.resultCode) {
                FavoriteAddUpdateActivity.RESULT_ADD -> {
                    val note =
                        result.data?.getParcelableExtra<Favorite>(FavoriteAddUpdateActivity.EXTRA_NOTE) as Favorite
                    adapter.addItem(note)
                    binding.rvReview.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                FavoriteAddUpdateActivity.RESULT_UPDATE -> {
                    val note =
                        result.data?.getParcelableExtra<Favorite>(FavoriteAddUpdateActivity.EXTRA_NOTE) as Favorite
                    val position =
                        result?.data?.getIntExtra(FavoriteAddUpdateActivity.EXTRA_POSITION, 0) as Int
                    adapter.updateItem(position, note)
                    binding.rvReview.smoothScrollToPosition(position)
                    showSnackbarMessage("Satu item berhasil diubah")
                }
                FavoriteAddUpdateActivity.RESULT_DELETE -> {
                    val position =
                        result?.data?.getIntExtra(FavoriteAddUpdateActivity.EXTRA_POSITION, 0) as Int
                    adapter.removeItem(position)
                    showSnackbarMessage("Satu item berhasil dihapus")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Notes"
        binding.rvReview.layoutManager = LinearLayoutManager(this)
        binding.rvReview.setHasFixedSize(true)

        adapter = FavoriteAdapter(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedFavorite: Favorite?, position: Int?) {
                val intent = Intent(this@FavoriteActivity, FavoriteAddUpdateActivity::class.java)
                intent.putExtra(FavoriteAddUpdateActivity.EXTRA_NOTE, selectedFavorite)
                intent.putExtra(FavoriteAddUpdateActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })
        binding.rvReview.adapter = adapter
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@FavoriteActivity, FavoriteAddUpdateActivity::class.java)
            resultLauncher.launch(intent)
        }



        if (savedInstanceState == null) {
            // proses ambil data
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATES)
            showSnackbarMessage("JANCOK")
            if (list != null) {
                adapter.listFavorites = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(EXTRA_STATES, adapter.listFavorites)
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATES, adapter.listFavorites)
    }

    private fun loadNotesAsync() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val noteHelper = FavoriteHelper.getInstance(applicationContext)
            noteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                showSnackbarMessage(notes.toString())
                adapter.listFavorites = notes
            } else {
                adapter.listFavorites = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            noteHelper.close()
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvReview, message, Snackbar.LENGTH_SHORT).show()
    }
}