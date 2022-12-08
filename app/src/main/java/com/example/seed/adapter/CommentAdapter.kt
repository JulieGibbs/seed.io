package com.example.seed.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.seed.data.Comment
import com.example.seed.data.Post
import com.example.seed.databinding.CommentRowBinding
import com.example.seed.databinding.PostRowBinding
import com.example.seed.fragments.PostDetailFragment
import com.example.seed.fragments.TimelineFragment
import com.example.seed.fragments.TimelineFragmentDirections
import com.google.firebase.firestore.Query

class CommentAdapter(private val context: PostDetailFragment, query: Query?) : FirestoreAdapter<CommentAdapter.ViewHolder>(query){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("comment adapter", "on create view holder")
        val binding = CommentRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("adapter", "on bind view holder")
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: CommentRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val snapshot = getSnapshot(position)
            val comment = snapshot.toObject(Comment::class.java)
            if (comment != null) {
                binding.tvContents.text = comment.text
            }
        }
    }



}