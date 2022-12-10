package com.example.seed.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seed.data.Post
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

class PostViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val LOG_TAG = "POST_VIEW_MODEL"
        const val COLLECTION = "posts"

        private val postCollection = FirebaseFirestore.getInstance().collection(COLLECTION)

        fun incrementPostCommentCount(postId: String) {
            Thread {
                postCollection
                    .document(postId).update(
                        mapOf(
                            "numberOfComments" to FieldValue.increment(1)
                        )
                    )
                    .addOnSuccessListener {
                        Log.d(LOG_TAG, "Successfully increment post $postId number of comments")
                    }
                    .addOnFailureListener {
                        Log.d(LOG_TAG, "${it.message}")
                    }
            }.start()
        }
    }

    fun createPost(post: Post) {
        Thread {
            postCollection.add(post)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "Successfully added post")
                    Toast.makeText(
                        getApplication(),
                        "Post succeeded", Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                    Toast.makeText(
                        getApplication(),
                        "Post failed", Toast.LENGTH_SHORT
                    ).show()
                }
        }.start()
    }

    fun removePost(postId: String) {
        Thread {
            postCollection.document(postId)
                .delete()
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "Successfully remove post: $postId")
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                }
        }.start()
    }

    fun likePostByUser(postId: String, userId: String) {
        Thread {
            postCollection.document(postId)
                .get()
                .addOnSuccessListener {
                    val postObject = it.toObject(Post::class.java)!!

                    // have not liked
                    if (postObject.likedBy.indexOf(userId) < 0) {
                        FirebaseFirestore.getInstance().collection(COLLECTION)
                            .document(postId)
                            .update("likedBy", FieldValue.arrayUnion(userId))
                            .addOnSuccessListener {
                                Log.d(LOG_TAG, "$userId likes $postId")
                            }
                            .addOnFailureListener { err ->
                                Log.d(LOG_TAG, "${err.message}")
                            }
                    }
                    // have liked
                    else {
                        FirebaseFirestore.getInstance().collection(COLLECTION)
                            .document(postId)
                            .update("likedBy", FieldValue.arrayRemove(userId))
                            .addOnSuccessListener {
                                Log.d(LOG_TAG, "$userId unlikes $postId")
                            }
                            .addOnFailureListener { err ->
                                Log.d(LOG_TAG, "${err.message}")
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d(LOG_TAG, "${it.message}")
                }
        }.start()
    }

    fun listenForPostUpdates(postId: String): MutableLiveData<Post> {
        val mutableLiveData = MutableLiveData<Post>()
        postCollection.document(postId).addSnapshotListener { value, error ->
            if (value != null) {
                mutableLiveData.value = value.toObject(Post::class.java)
            }
        }
        return mutableLiveData
    }


}