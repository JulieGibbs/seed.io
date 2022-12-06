package com.example.seed.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.seed.data.User
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val COLLECTION = "users"
        const val LOG_TAG = "USER_VIEW_MODEL"

        private val userCollection = FirebaseFirestore.getInstance().collection(COLLECTION)
    }

    fun createUser(user: User){
        Thread {
            userCollection.add(user)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "User successfully added")
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                }
        }.start()
    }

    fun updateUser(userId: String, newUser: User){
        Thread {
            userCollection.document(userId)
                .set(newUser)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "User $userId successfully updated")
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                }
        }.start()
    }
}