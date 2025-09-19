package com.example.sub1int2.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sub1int2.data.response.ListStoryItem
import com.example.sub1int2.databinding.ItemStoryBinding
import com.example.sub1int2.view.detail.DetailActivity
import androidx.core.util.Pair

class MainAdapter: ListAdapter<ListStoryItem, MainAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class StoryViewHolder(val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvStoryTitle.text = story.name
            binding.tvStoryDescription.text = story.description
            Glide.with(binding.ivStoryImage.context)
                .load(story.photoUrl)
                .into(binding.ivStoryImage)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
                    putExtra(DetailActivity.EXTRA_STORY_NAME, story.name)
                    putExtra(DetailActivity.EXTRA_STORY_DESCRIPTION, story.description)
                    putExtra(DetailActivity.EXTRA_STORY_PHOTO_URL, story.photoUrl)
                }
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        Pair(binding.ivStoryImage, "story_image"),
                        Pair(binding.tvStoryTitle, "story_title"),
                        Pair(binding.tvStoryDescription, "story_description")
                    )
                context.startActivity(intent, optionsCompat.toBundle())
                // If you don't want to use shared element transition, use this line instead:
                // context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : androidx.recyclerview.widget.DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}