package com.example.seed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.seed.R
import com.example.seed.data.Post
import com.example.seed.databinding.FragmentNewPostBinding
import com.example.seed.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private lateinit var binding : FragmentNewPostBinding
    private lateinit var viewModel : PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        binding.btnCreatePost.setOnClickListener {
            if (validInput()) {
                val newPost = Post(title = binding.etTitle.text.toString(), body = binding.etContents.text.toString())
                viewModel.createPost(newPost)
                it.findNavController().popBackStack()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewPostFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun validInput() : Boolean {
        return binding.etContents.text.isNotEmpty() && binding.etTitle.text.isNotEmpty()
    }
}