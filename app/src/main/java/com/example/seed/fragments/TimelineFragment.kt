package com.example.seed.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
            FirebaseFirestore.getInstance().collection("posts")
        )
        //adapter = TempAdapter()
        binding.timelineRecyclerView.adapter = adapter

        binding.btnAddPost.setOnClickListener {
            postViewModel.createPost(
                Post(body = "Yo template")
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}