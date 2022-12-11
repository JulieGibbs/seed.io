package com.example.seed.fragments

import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.seed.R
import com.example.seed.adapter.CommentAdapter
import com.example.seed.data.Comment
import com.example.seed.data.User
import com.example.seed.databinding.FragmentPostDetailBinding
import com.example.seed.viewmodel.CommentViewModel
import com.example.seed.viewmodel.PostViewModel
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostDetailFragment : Fragment() {

    private lateinit var binding: FragmentPostDetailBinding
    private var postId: String? = null

    private lateinit var postViewModel: PostViewModel
    private lateinit var commentViewModel: CommentViewModel

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adapter : CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = PostDetailFragmentArgs.fromBundle(it)
            postId = args.postId
        }
        adapter = CommentAdapter(
            this,
            FirebaseFirestore.getInstance().collection(CommentViewModel.COLLECTION)
                .whereEqualTo("postId", postId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]
        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        binding.recyclerComment.adapter = adapter

        firebaseAuth = FirebaseAuth.getInstance()

        postId?.let {
            postViewModel.listenForPostUpdates(it).observe(viewLifecycleOwner) { post ->
                binding.tvTitle.text = post.title
                binding.tvContents.text = post.body
                binding.tvLikeCount.text = post.likedBy.size.toString()+" Drops"
                binding.tvCommentCount.text = post.numberOfComments.toString()+" Comments"

                val longDate = post.timestamp?.time
                val ago = longDate?.let { DateUtils.getRelativeTimeSpanString(it, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS) }
                binding.tvTime.text = ago.toString()
                binding.tvDate.text = post.timestamp?.toString()

                UserViewModel.getUserInfo(
                    userId = post.authorid,
                    handleUserFound = ::setPostUsernameAndProfile
                )
            }
        }

        setNewCommentListener()
        setLikePostListener()

        return binding.root
    }

    private fun setNewCommentListener() {
        binding.ivPostComment.setOnClickListener {
            if (binding.commentText.text.isNotEmpty()) {
                if (firebaseAuth.currentUser != null){
                    val newComment = postId?.let { id ->
                        Comment(
                            postId = id,
                            text = binding.commentText.text.toString(),
                            authorid = firebaseAuth.currentUser!!.uid,
                        )
                    }
                    if (newComment != null) {
                        commentViewModel.addComment(newComment)
                        binding.commentText.text.clear()
                    }
                    if (postId != null) {
                        reloadFragment(it)
                    }
                }
            }
        }
    }

    private fun reloadFragment(it: View) {
        val action = PostDetailFragmentDirections.actionPostDetailFragmentToPostDetailFragment(
            postId!!
        )
        it.findNavController().navigate(action)
    }

    private fun setLikePostListener() {
        binding.ivLike.setOnClickListener {
            postId?.let { it1 -> postViewModel.likePostByUser(it1, firebaseAuth.currentUser!!.uid) }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun setPostUsernameAndProfile(user: User){
        binding.tvUsername.text = user.username
        displayPostProfileImage(user.imgURL)
    }

    private fun displayPostProfileImage(profileImgURL: String) {
        if (profileImgURL.isEmpty()) return
        Glide.with(this@PostDetailFragment)
            .load(profileImgURL)
            .into(binding.ivProfile)
    }
}