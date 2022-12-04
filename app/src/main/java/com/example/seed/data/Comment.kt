package com.example.seed.data

import java.sql.Timestamp

data class Comment(
    var authorid: String = "",
    var text: String = "",
    var timestamp: Timestamp? = null, // TODO: Automatically instantiate when object is created
)
