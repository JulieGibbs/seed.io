package com.example.seed.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Comment(
    var authorid: String = "",
    var postId: String = "",
    var text: String = "",
    @ServerTimestamp
    var timestamp: Date? = null
)

