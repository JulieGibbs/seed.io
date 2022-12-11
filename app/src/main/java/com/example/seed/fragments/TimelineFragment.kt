package com.example.seed.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.seed.R
import com.example.seed.adapter.TimelineAdapter
import com.example.seed.data.Comment
import com.example.seed.data.Post
import com.example.seed.databinding.FragmentTimelineBinding
import com.example.seed.viewmodel.CommentViewModel
import com.example.seed.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TimelineFragment : Fragment() {

    private lateinit var binding: FragmentTimelineBinding
    private lateinit var adapter: TimelineAdapter
    private lateinit var postViewModel: PostViewModel
    private var userId : String = ProfileFragment.NOT_LOGGED_IN_USER_ID
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(
            inflater, container, false
        )

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        adapter = TimelineAdapter(
            this,
            FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
        )
        initializeUserId()
        binding.recyclerPost.adapter = adapter
        handleOnClickTag(adapter)
        handleOnClickAddPost()

        return binding.root
    }

    private fun initializeUserId() {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            userId = firebaseAuth.currentUser!!.uid
        }
    }

    private fun handleOnClickAddPost() {
        binding.fabAddPost.setOnClickListener {
            // navigate to create post fragment
            it.findNavController().navigate(R.id.newPostFragment)
        }
    }

    private fun handleOnClickTag(adapter: TimelineAdapter) {
        binding.btnTag0.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag1.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .whereEqualTo("tag", 1)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag2.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .whereEqualTo("tag", 2)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag3.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .whereEqualTo("tag", 3)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
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

    fun likePost(postId: String){
        postViewModel.likePostByUser(postId, userId = userId);
    }
}