package com.example.seed.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seed.data.Post
import com.example.seed.data.User
import com.example.seed.databinding.PostRowBinding
import com.example.seed.fragments.TimelineFragment
import com.example.seed.fragments.TimelineFragmentDirections
import com.example.seed.util.TagUtil
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat


class TimelineAdapter(private val context: TimelineFragment, query: Query?) : FirestoreAdapter<TimelineAdapter.ViewHolder>(query){

    // cache layer for user (to avoid querying for the same user)
    val userIdToUser = mutableMapOf<String, User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: PostRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            val snapshot = getSnapshot(position)
            val postId = getSnapshotId(position)
            val post = snapshot.toObject(Post::class.java)!!

            binding.tvContents.text = post.body
            binding.tvLikeCount.text = post.likedBy.size.toString()
            binding.tvTitle.text = post.title
            binding.tvLabel.text = TagUtil().intToTag(post.tag)
            binding.tvCommentCount.text = post.numberOfComments.toString()
            val longDate = post.timestamp?.time
            val ago = longDate?.let { DateUtils.getRelativeTimeSpanString(it, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS) }
            binding.tvTime.text = ago.toString()
            binding.ivLike.setOnClickListener {
                context.likePost(postId)
            }
            binding.root.setOnClickListener {
                val action = TimelineFragmentDirections.actionTimelineFragmentToPostDetailFragment(postId)
                it.findNavController().navigate(action)
            }

            if (userIdToUser.containsKey(post.authorid)){
                setPostAuthorInfo(userIdToUser[post.authorid]!!)
            } else {
                UserViewModel.getUserInfo(
                    userId = post.authorid,
                    handleUserFound = ::setPostAuthorInfo
                )
            }
        }

        private fun setPostAuthorInfo(user: User){
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