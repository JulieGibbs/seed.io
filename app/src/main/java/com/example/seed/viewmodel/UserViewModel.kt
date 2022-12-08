package com.example.seed.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.seed.LoginActivity
import com.example.seed.data.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val COLLECTION = "users"
        private const val LOG_TAG = "USER_VIEW_MODEL"

        private val userCollection = FirebaseFirestore.getInstance().collection(COLLECTION)

        fun updateUser(userId: String,
                       newUser: User,
                       handleSuccess: () -> Unit = {},
                       handleFailure: () -> Unit = {}){
            Thread {
                userCollection.document(userId)
                    .set(newUser)
                    .addOnSuccessListener {
                        Log.d(LOG_TAG, "User $userId successfully updated")
                        handleSuccess()
                    }
                    .addOnFailureListener {
                        Log.d(LOG_TAG, "${it.message}")
                        handleFailure()
                    }
            }.start()
        }

        // use this function to get userInfo (username, bio, imgUrl)
        // pass in 2 functions one that tell what to do if you do find
        // an user with the given userId and one when you do not find one
        // look at LoginActivity.handleSuccessfulSignIn for reference
        fun getUserInfo(userId: String,
                    handleUserFound: (DocumentSnapshot) -> Unit = {},
                    handleUserNotFound: (String) -> Unit = {}){
            userCollection.document(userId).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val document = task.result
                        if (document.exists()){
                            handleUserFound(document)
                        } else {
                            handleUserNotFound(userId)
                        }
                    }
                }
        }
    }
}