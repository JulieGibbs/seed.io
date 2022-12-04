package com.example.seed.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.seed.data.Comment
import com.example.seed.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class PostViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val COLLECTION_POSTS = "posts"
        val TAG = mapOf<Short, String>(0.toShort() to "All", // TODO: Move to the correct file accordingly
            1.toShort() to "Fintech",
            2.toShort() to "Web3",
            3.toShort() to "Biotech",
            4.toShort() to "Artificial Intelligence",
            5.toShort() to "Climate Tech",
            6.toShort() to "Education",
            7.toShort() to "Space",
            8.toShort() to "Real Estate"
        )
    }

    fun createPost(post: Post) : Boolean {
        val postsCollection = FirebaseFirestore.getInstance().collection("posts")
        var isSuccessful = true

        // TODO: Check if this is asynch and if it would return before post is added
        postsCollection.add(post)
            .addOnSuccessListener {
                isSuccessful = true
            }
            .addOnFailureListener {
                isSuccessful = false
            }
        return isSuccessful
    }

    var snapshotListener: ListenerRegistration? = null

    // query posts according to tag or userid
    fun queryPosts(adapter: Any, tag: Short, userid: String = "") {
        monitorAndUpdateDatabaseChanges(adapter, tag, "")
    }

    private fun getQueryPostsByTag(adapter: Any, tag: Short): Query {
        val queryPosts = FirebaseFirestore.getInstance()
            .collection(COLLECTION_POSTS).whereEqualTo("tag", tag)

        return queryPosts
    }

    private fun getQueryPostsByUser(adapter: Any, userid: String): Query {
        val queryPosts = FirebaseFirestore.getInstance()
            .collection(COLLECTION_POSTS).whereEqualTo("authorid", userid)

        return queryPosts
    }

    private fun monitorAndUpdateDatabaseChanges(adapter: Any, tag: Short, userid: String) {
        var queryPosts: Query
        if (userid == "") {
            queryPosts = getQueryPostsByTag(adapter, tag)
        } else {
            queryPosts = getQueryPostsByUser(adapter, userid)
        }

        val eventListener = object : EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?,
                                 e: FirebaseFirestoreException?) {
                if (e != null) {
                    // TODO: Add a toast error? Or send error message to view to handle
                    return
                }

                for (docChange in querySnapshot?.getDocumentChanges()!!) {
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val post = docChange.document.toObject(Post::class.java)
                        adapter.addPost(post, docChange.document.id)
                    } else if (docChange.type == DocumentChange.Type.REMOVED) {
                        adapter.removePostByKey(docChange.document.id) // TODO: Do we have remove functionality?
                    } else if (docChange.type == DocumentChange.Type.MODIFIED) { // TODO: update changes to post - handle likes, unlikes, comment counts
                        adapter.updatePostByKey(post, docChange.document.id)
                    }
                }
            }
        }
        snapshotListener = queryPosts.addSnapshotListener(eventListener)
    }

    // TODO: Do we need removePost?
    fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection(
            COLLECTION_POSTS
        ).document(
            postKeys[index]
        ).delete()
    }

    fun addLikeToPost(postid: String, userid: String) { // TODO: Only add to LikesBy if user not in
        val postDocumentId = FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).whereEqualTo("postid", postid).get()
        FirebaseFirestore.getInstance().collection(COLLECTION_POSTS).document(
            postDocumentId.toString() // TODO: Check if this is the right way to do it
        ).update(
            "likedBy", FieldValue.arrayUnion(userid) // TODO: Check if arrayUnion only adds values that do not exist into array
        )
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