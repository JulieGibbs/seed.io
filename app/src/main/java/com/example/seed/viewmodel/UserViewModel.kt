package com.example.seed.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.seed.data.User
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val COLLECTION = "users"
        private const val LOG_TAG = "USER_VIEW_MODEL"

        val userCollection = FirebaseFirestore.getInstance().collection(COLLECTION)

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
    }
}