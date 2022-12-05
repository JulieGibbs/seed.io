package com.example.seed.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.seed.data.User
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val COLLECTION_USERS = "users"
    }

    fun createUser(user: User){
        Thread {
            FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        }.start()
    }

    fun updateUser(userId: String, newUser: User){
        Thread {
            FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(userId)
                .set(newUser)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        }.start()
    }

}