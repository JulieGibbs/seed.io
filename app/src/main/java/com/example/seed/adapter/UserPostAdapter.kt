package com.example.seed.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seed.data.Post
import com.example.seed.data.User
import com.example.seed.databinding.PostRowBinding
import com.example.seed.fragments.ProfileFragment
import com.example.seed.fragments.ProfileFragmentDirections
import com.example.seed.fragments.TimelineFragmentDirections
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.firestore.Query


class UserPostAdapter(private val context: ProfileFragment, query: Query?) : FirestoreAdapter<UserPostAdapter.ViewHolder>(query) {

    var currentUser = User()

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

    inner class ViewHolder(private val binding: PostRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val snapshot = getSnapshot(position)
            val postId = getSnapshotId(position)
            val post = snapshot.toObject(Post::class.java)!!

            binding.tvTitle.text = post.title
            binding.tvContents.text = post.body
            binding.tvLikeCount.text = post.likedBy.size.toString()
            binding.tvCommentCount.text = post.numberOfComments.toString()
            binding.ivLike.setOnClickListener {
                context.likePost(postId)
            }

            binding.root.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToPostDetailFragment(postId)
                it.findNavController().navigate(action)
            }

            if (currentUser.uid.isEmpty()){
                UserViewModel.getUserInfo(
                    userId = post.authorid,
                    handleUserFound = ::setPostAuthorInfo
                )
            } else {
                setPostAuthorInfo(currentUser)
            }
        }

        private fun setPostAuthorInfo(user: User){
            binding.tvUsername.text = user.username
            displayPostProfileImage(user.imgURL)
            currentUser = user
        }

        private fun displayPostProfileImage(profileImgURL: String) {
            if (profileImgURL.isEmpty()) return
            Glide.with(context)
                .load(profileImgURL)
                .into(binding.ivProfile)
        }
    }
}



