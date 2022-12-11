package com.example.seed.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.seed.data.Comment
import com.google.firebase.firestore.*

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val LOG_TAG = "COMMENT_VIEW_MODEL"
        const val COLLECTION = "comments"

        private val commentCollection = FirebaseFirestore.getInstance().collection(COLLECTION)
    }

    fun addComment(comment: Comment){
        Thread {
            // add comment
            commentCollection.add(comment)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "Successfully added comment")
                    Toast.makeText(getApplication(),
                        "Successfully added comment", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                    Toast.makeText(getApplication(),
                        "Failed to add comment", Toast.LENGTH_SHORT).show()

                }

            // increment like
            PostViewModel.incrementPostCommentCount(comment.postId)
        }.start()
    }
}