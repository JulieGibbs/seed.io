package com.example.seed.data

import java.io.Serializable

data class User (
    var username: String = "",
    var uid: String = "",
    var bio: String = "",
    var imgURL: String = "",
) : Serializable