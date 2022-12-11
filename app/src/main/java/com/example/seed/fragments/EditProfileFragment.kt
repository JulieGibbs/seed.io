package com.example.seed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.seed.data.User
import com.example.seed.databinding.FragmentEditProfileBinding
import com.example.seed.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var userId : String = ProfileFragment.NOT_LOGGED_IN_USER_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            userId = firebaseAuth.currentUser!!.uid
        }

        // prepopulate the fields
        UserViewModel.getUserInfo(
            userId = userId,
            handleUserFound = ::prepopulateFields
        )

        binding.btnEditProfile.setOnClickListener {
            UserViewModel.updateUser(
                userId = userId,
                newUser = User(
                    username = binding.etUsername.text.toString(),
                    uid = userId,
                    bio = binding.etBio.text.toString(),
                    imgURL = binding.etImageUrl.text.toString()
                ),
                handleSuccess = ::navigateToProfileFragment
            )
        }

        binding.btnCancel.setOnClickListener {
            navigateToProfileFragment()
        }

        return binding.root
    }

    private fun prepopulateFields(user: User){
        binding.etUsername.setText(user.username)
        binding.etBio.setText(user.bio)
        binding.etImageUrl.setText(user.imgURL)
    }

    private fun navigateToProfileFragment(){
        val action = EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()
        findNavController().navigate(action)
    }
}