package com.example.seed.data

import java.sql.Timestamp

data class Post(
        var uid: String = "",
        var tag: Short = 0,
        var authorid: String = "",
        var comments: Array<Comment> = arrayOf<Comment>(),
        var title: String = "",
        var body: String = "",
        var timestamp: Timestamp? = null, // TODO: Automatically instantiate when object is created
        var likedBy: Array<String> = arrayOf<String>(),
)