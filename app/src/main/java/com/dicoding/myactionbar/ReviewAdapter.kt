package com.dicoding.myactionbar

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ReviewAdapter(private val listReview: List<String>, private val listFull: List<ItemsItem>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_review, viewGroup, false))
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val listImage = ArrayList<String>()
        val listName = ArrayList<String>()
        val listUrl = ArrayList<String>()

        for (data in listFull){
            listImage.add(data.avatarUrl)
            listName.add(data.login)
            listUrl.add(data.url)
        }

        Glide.with(viewHolder.tvLayout.context)
            .load(listImage[position])
            .into(viewHolder.tvImage)
        viewHolder.tvItem.text = listReview[position]
        viewHolder.tvLayout.setOnClickListener{v ->
            val moveWithDataIntent = Intent(v.context,DetailActivity::class.java)

            moveWithDataIntent.putExtra(DetailActivity.EXTRA_IMG,listImage[position])
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_URL,listUrl[position])
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_NAME,listName[position])

            v.context.startActivity(moveWithDataIntent)
        }
    }
    override fun getItemCount() = listReview.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tvItem)
        val tvImage: ImageView = view.findViewById(R.id.tvImage)
        val tvLayout: View = view.findViewById(R.id.relativeLayout)
    }
}

