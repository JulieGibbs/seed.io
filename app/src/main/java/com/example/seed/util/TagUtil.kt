package com.example.seed.util

class TagUtil {
    companion object {
        val TAG = mapOf(
            0 to "All",
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

    fun intToTag(i: Int): String {
        if (!TAG.containsKey(i)){
            throw Exception("This tag does not exist!")
        }
        return TAG[i]!!
    }
}