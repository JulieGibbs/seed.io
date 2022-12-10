package com.example.seed.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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

        adapter = TimelineAdapter(
            this,
            FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
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
            binding.btnTag0.setBackgroundColor(Color.parseColor("#EEF2E6"))
            binding.btnTag0.setTextColor(Color.parseColor("#1C6758"))
            binding.btnTag1.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag1.setTextColor(Color.parseColor("#000000"))
            binding.btnTag2.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag2.setTextColor(Color.parseColor("#000000"))
            binding.btnTag3.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag3.setTextColor(Color.parseColor("#000000"))

            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag1.setOnClickListener {
            binding.btnTag1.setBackgroundColor(Color.parseColor("#EEF2E6"))
            binding.btnTag1.setTextColor(Color.parseColor("#1C6758"))
            binding.btnTag0.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag0.setTextColor(Color.parseColor("#000000"))
            binding.btnTag2.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag2.setTextColor(Color.parseColor("#000000"))
            binding.btnTag3.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag3.setTextColor(Color.parseColor("#000000"))

            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .whereEqualTo("tag", 1)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag2.setOnClickListener {
            binding.btnTag2.setBackgroundColor(Color.parseColor("#EEF2E6"))
            binding.btnTag2.setTextColor(Color.parseColor("#1C6758"))
            binding.btnTag0.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag0.setTextColor(Color.parseColor("#000000"))
            binding.btnTag1.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag1.setTextColor(Color.parseColor("#000000"))
            binding.btnTag3.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag3.setTextColor(Color.parseColor("#000000"))

            val queryByTag = FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .whereEqualTo("tag", 2)
                .orderBy("timestamp", Query.Direction.DESCENDING)
            adapter.setQuery(queryByTag)
        }
        binding.btnTag3.setOnClickListener {
            binding.btnTag3.setBackgroundColor(Color.parseColor("#EEF2E6"))
            binding.btnTag3.setTextColor(Color.parseColor("#1C6758"))
            binding.btnTag0.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag0.setTextColor(Color.parseColor("#000000"))
            binding.btnTag1.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag1.setTextColor(Color.parseColor("#000000"))
            binding.btnTag2.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.btnTag2.setTextColor(Color.parseColor("#000000"))

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
        // TODO: replace "01" with actual userId
        postViewModel.likePostByUser(postId, "01");
    }
}