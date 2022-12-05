package com.example.seed.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seed.data.Post
import com.example.seed.databinding.TimelinePostLayoutBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class TimelineAdapter(query: Query?) : FirestoreAdapter<TimelineAdapter.ViewHolder>(query){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("adapter", "on create view holder")
        val binding = TimelinePostLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("adapter", "on bind view holder")
        holder.bind(getSnapshot(position))
    }

    inner class ViewHolder(private val binding: TimelinePostLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(snapshot: DocumentSnapshot) {
            val post = snapshot.toObject(Post::class.java)
            Log.d("adapter", post!!.body)
            binding.tvBody.text = post!!.body
        }
    }

}