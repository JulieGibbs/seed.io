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
import com.example.seed.data.Post
import com.example.seed.databinding.FragmentTimelineBinding
import com.example.seed.viewmodel.PostViewModel
import com.google.firebase.firestore.FirebaseFirestore

class TimelineFragment : Fragment() {

    private lateinit var binding: FragmentTimelineBinding
    private lateinit var adapter: TimelineAdapter
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(
            inflater, container, false
        )

        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        Log.d("fragment", "on create view")
        adapter = TimelineAdapter(
            this,
            FirebaseFirestore.getInstance().collection("posts")
        )

        binding.recyclerPost.adapter = adapter
        handleOnClickTag(adapter)
        handleOnClickAddPost()

        return binding.root
    }

    private fun handleOnClickAddPost() {
        binding.fabAddPost.setOnClickListener {
            // navigate to create post fragment
            it.findNavController().navigate(R.id.newPostFragment)
        }
    }

    private fun handleOnClickTag(adapter: TimelineAdapter) {
        binding.btnTag0.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection("posts")
            adapter.setQuery(queryByTag)
        }
        binding.btnTag1.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection("posts").whereEqualTo("tag", 1)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag2.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection("posts").whereEqualTo("tag", 2)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag3.setOnClickListener {
            val queryByTag = FirebaseFirestore.getInstance().collection("posts").whereEqualTo("tag", 3)
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
        // TODO: replace "01" with actual userId
        postViewModel.likePostByUser(postId, "01");
    }
}