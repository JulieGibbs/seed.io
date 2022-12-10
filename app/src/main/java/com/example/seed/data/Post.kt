package com.example.seed.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import java.sql.Timestamp
import java.util.*

data class Post(
        var tag: Int = 0,
        var authorid: String = "",
        var numberOfComments: Int = 0,
        var title: String = "",
        var body: String = "",
        @ServerTimestamp
        var timestamp: Date? = null,
        var likedBy: List<String> = arrayListOf<String>(),
)

