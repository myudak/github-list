package com.dicoding.myactionbar.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myactionbar.DetailActivity
import com.dicoding.myactionbar.R
import com.dicoding.myactionbar.databinding.ItemReviewBinding
import com.dicoding.myactionbar.entity.Favorite

class FavoriteAdapter(private val onItemClickCallback: OnItemClickCallback) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var listFavorites = ArrayList<Favorite>()
        set(listFavorites) {
            if (listFavorites.size > 0) {
                this.listFavorites.clear()
            }
            this.listFavorites.addAll(listFavorites)
        }

    fun addItem(note: Favorite) {
        this.listFavorites.add(note)
        notifyItemInserted(this.listFavorites.size - 1)
    }

    fun updateItem(position: Int, note: Favorite) {
        this.listFavorites[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listFavorites.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorites.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int = this.listFavorites.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemReviewBinding.bind(itemView)
        fun bind(note: Favorite) {

            Glide.with(binding.tvImage.context)
                .load(note.image)
                .into(binding.tvImage)
            binding.tvItem.text = note.name
            binding.relativeLayout.setOnClickListener{v ->
                val moveWithDataIntent = Intent(v.context, DetailActivity::class.java)
                moveWithDataIntent.putExtra(DetailActivity.EXTRA_NAME,note.name)

                v.context.startActivity(moveWithDataIntent)
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(selectedFavorite: Favorite?, position: Int?)
    }
}