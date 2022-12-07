package com.example.seed.data

import java.sql.Timestamp

data class Post(
        var tag: Int = 0,
        var authorid: String = "",
        var numberOfComments: Int = 0,
        var title: String = "",
        var body: String = "",
        var timestamp: Timestamp? = null, // TODO: Automatically instantiate when object is created
        var likedBy: List<String> = arrayListOf<String>(),
)

