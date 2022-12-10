package com.example.seed.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.seed.adapter.UserPostAdapter
import com.example.seed.data.User
import com.example.seed.databinding.FragmentProfileBinding
import com.example.seed.viewmodel.PostViewModel
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    companion object {
        const val NOT_LOGGED_IN_USER_ID = "-1"
        private const val TAG = "ProfileFragment Tag"
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: UserPostAdapter
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    private var userId : String = NOT_LOGGED_IN_USER_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(
            inflater, container, false
        )

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            userId = firebaseAuth.currentUser!!.uid
        }

        // TODO: This still does not work
        adapter = UserPostAdapter(
            this,
            FirebaseFirestore.getInstance().collection(PostViewModel.COLLECTION)
                .whereEqualTo("authorid", userId)
        )
        binding.recyclerPost.adapter = adapter

        binding.btnSetting.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            it.findNavController().navigate(action)
        }

        UserViewModel.getUserInfo(userId, ::displayUserInfo, ::displayUserNotFound)
        return binding.root
    }

    private fun displayProfileImage(profileImgURL: String) {
        if (profileImgURL.isEmpty()) return
        Glide.with(this@ProfileFragment)
            .load(profileImgURL)
            .into(binding.ivProfile)
    }

    private fun displayUserInfo(user: User) {
        binding.tvUsername.text = user.username
        binding.tvBio.text = user.bio
        displayProfileImage(user.imgURL)
    }

    private fun displayUserNotFound(userId: String) {
        binding.tvUsername.text = "User not found"
        binding.tvBio.text = "User not found"
    }

    fun likePost(postId: String){
        postViewModel.likePostByUser(postId, userId);
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