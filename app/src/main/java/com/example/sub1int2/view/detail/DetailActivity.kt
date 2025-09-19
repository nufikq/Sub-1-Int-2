package com.example.sub1int2.view.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sub1int2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable shared element transitions
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        // Set up the shared element transition
        val transition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val storyId = intent.getStringExtra("extra_story_id")
        val storyTitle = intent.getStringExtra("extra_story_name")
        val storyDescription = intent.getStringExtra("extra_story_description")
        val storyImageUrl = intent.getStringExtra("extra_story_photo_url")

        // Set data to views
        binding.tvStoryTitle.text = storyTitle
        binding.tvStoryDescription.text = storyDescription

        // Load image
        Glide.with(this)
            .load(storyImageUrl)
            .into(binding.ivStoryImage)

        // Set up back button
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun finish() {
        // Don't call finishAfterTransition() here as it causes recursion
        // Instead, use supportFinishAfterTransition() directly or just call super.finish()
        super.finish()
    }

    // Instead of overriding finish(), use a separate method
    private fun finishWithTransition() {
        supportFinishAfterTransition()
    }

    // Call finishWithTransition() instead of finish() when you want the animation

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
        const val EXTRA_STORY_NAME = "extra_story_name"
        const val EXTRA_STORY_DESCRIPTION = "extra_story_description"
        const val EXTRA_STORY_PHOTO_URL = "extra_story_photo_url"
    }
}