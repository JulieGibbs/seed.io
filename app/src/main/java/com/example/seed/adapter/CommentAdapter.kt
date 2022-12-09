package com.example.seed.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seed.data.Comment
import com.example.seed.data.User
import com.example.seed.databinding.CommentRowBinding
import com.example.seed.fragments.PostDetailFragment
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.firestore.Query

class CommentAdapter(private val context: PostDetailFragment, query: Query?) : FirestoreAdapter<CommentAdapter.ViewHolder>(query){

    // cache layer for user (to avoid querying for the same user)
    val userIdToUser = mutableMapOf<String, User>()

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

                if (userIdToUser.containsKey(comment.authorid)){
                    setCommentUsernameAndProfile(userIdToUser[comment.authorid]!!)
                } else {
                    UserViewModel.getUserInfo(
                        userId = comment.authorid,
                        handleUserFound = ::setCommentUsernameAndProfile
                    )
                }
            }
        }

        private fun setCommentUsernameAndProfile(user: User){
            binding.tvUsername.text = user.username
            displayPostProfileImage(user.imgURL)
            userIdToUser[user.uid] = user
        }

        private fun displayPostProfileImage(profileImgURL: String) {
            if (profileImgURL.isEmpty()) return
            Glide.with(context)
                .load(profileImgURL)
                .into(binding.ivProfile)
        }
    }
}