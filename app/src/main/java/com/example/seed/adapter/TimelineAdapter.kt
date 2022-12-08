package com.example.seed.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.seed.data.Post
import com.example.seed.databinding.PostRowBinding
import com.example.seed.fragments.TimelineFragment
import com.example.seed.fragments.TimelineFragmentDirections
import com.google.firebase.firestore.Query

class TimelineAdapter(private val context: TimelineFragment, query: Query?) : FirestoreAdapter<TimelineAdapter.ViewHolder>(query){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("adapter", "on create view holder")
        val binding = PostRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("adapter", "on bind view holder")
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: PostRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val snapshot = getSnapshot(position)
            val postId = getSnapshotId(position)
            val post = snapshot.toObject(Post::class.java)
            Log.d("adapter", post!!.body)
            binding.tvContents.text = post.body
            binding.tvLikeCount.text = post.likedBy.size.toString()
            binding.tvTitle.text = post.title
            binding.tvCommentCount.text = post.numberOfComments.toString()
            binding.ivLike.setOnClickListener {
                context.likePost(postId)
            }
            binding.root.setOnClickListener {
                val action = TimelineFragmentDirections.actionTimelineFragmentToPostDetailFragment(postId)
                it.findNavController().navigate(action)
            }
        }
    }



}