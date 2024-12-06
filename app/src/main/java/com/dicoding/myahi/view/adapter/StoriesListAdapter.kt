package com.dicoding.myahi.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myahi.R
import com.dicoding.myahi.data.response.ListStoryItem
import com.dicoding.myahi.databinding.ItemStoriesBinding
import com.dicoding.myahi.view.detail.DetailStoriesActivity

class StoryListAdapter : ListAdapter<ListStoryItem, StoryListAdapter.ViewHolder>(DIFFERENCE_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.bindData(storyItem)

        val id = storyItem.id
        var imgPhoto: ImageView = holder.itemView.findViewById(R.id.imageStory)
        var tvName: TextView = holder.itemView.findViewById(R.id.titleStoryTextView)
        var tvDescription: TextView = holder.itemView.findViewById(R.id.descStoryTextView)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailStoriesActivity::class.java)
            intentDetail.putExtra("id_story", id)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(imgPhoto, "image"),
                    Pair(tvName, "name"),
                    Pair(tvDescription, "description"),
                )
            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }
    }

    class ViewHolder(private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(storyItem: ListStoryItem) {
            with(binding) {
                titleStoryTextView.text = storyItem.name
                descStoryTextView.text = storyItem.description
                Glide.with(itemView.context)
                    .load(storyItem.photoUrl)
                    .into(imageStory)
            }
        }
    }

    companion object {
        val DIFFERENCE_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

