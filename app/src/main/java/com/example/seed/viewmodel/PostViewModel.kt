package com.example.seed.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.seed.data.Comment
import com.example.seed.data.Post
import com.google.firebase.firestore.*

class PostViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val LOG_TAG = "POST_VIEW_MODEL"
        const val COLLECTION_POSTS = "posts"
        val TAG = mapOf(0 to "All", // TODO: Move to the correct file accordingly
            1 to "Fintech",
            2 to "Web3",
            3 to "Biotech",
            4 to "Artificial Intelligence",
            5 to "Climate Tech",
            6 to "Education",
            7 to "Space",
            8 to "Real Estate"
        )
    }

    fun createPost(post: Post){
        Thread {
            FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).add(post)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "SUCCESS")
                    Toast.makeText(getApplication(),
                        "Post succeeded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                    Toast.makeText(getApplication(),
                        "Post failed", Toast.LENGTH_SHORT).show()
                }
        }.start()
    }

    fun removePost(postId: String) {
        Thread {
            FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).document(postId)
                .delete()
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }.start()
    }

    fun addLikeToPost(postId: String, userId: String) {
        Thread {
            FirebaseFirestore.getInstance().collection(COLLECTION_POSTS)
                .document(postId).get().addOnSuccessListener {
                    val postObject = it.toObject(Post::class.java)!!

                    // have not liked
                    if (postObject.likedBy.indexOf(userId) < 0){
                        FirebaseFirestore.getInstance().collection(COLLECTION_POSTS)
                            .document(postId)
                            .update("likedBy", FieldValue.arrayUnion(userId))
                    }
                    // have liked
                    else {
                        FirebaseFirestore.getInstance().collection(COLLECTION_POSTS)
                            .document(postId)
                            .update("likedBy", FieldValue.arrayRemove(userId))
                    }
                }
                .addOnFailureListener{}
        }.start()
    }

    // TODO: Fix update comments. Need to change arrayOf<comment> to arrayOf<String> (commentID) ?
    fun addComment(postid: String, comment: Comment) {
        val postDocumentId = FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).whereEqualTo("postid", postid).get()
        FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).document(
            postDocumentId.toString() // TODO: Check syntax
        ).update("comments", FieldValue.arrayUnion(comment))
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }


}