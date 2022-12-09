package com.example.seed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.seed.adapter.CommentAdapter
import com.example.seed.data.Comment
import com.example.seed.data.User
import com.example.seed.databinding.FragmentPostDetailBinding
import com.example.seed.viewmodel.CommentViewModel
import com.example.seed.viewmodel.PostViewModel
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        postId?.let {
            postViewModel.getPostById(it).observe(viewLifecycleOwner) { post ->
                binding.tvTitle.text = post.title
                binding.tvContents.text = post.body
                binding.tvLikeCount.text = post.likedBy.size.toString()+" Drops"
                binding.tvCommentCount.text = post.numberOfComments.toString()+" Comments"
                UserViewModel.getUserInfo(
                    userId = post.authorid,
                    handleUserFound = ::setPostUsernameAndProfile
                )
            }
        }

        setNewCommentListener()

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
                            authorid = firebaseAuth.currentUser!!.uid
                        )
                    }
                    if (newComment != null) {
                        commentViewModel.addComment(newComment)
                        binding.commentText.text.clear()
                    }
                }
            }
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