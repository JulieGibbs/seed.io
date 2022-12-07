package com.example.seed.util

class TagUtil {
    companion object {
        val TAG = mapOf(
            0 to "All",
            1 to "Fintech",
            2 to "Web3",
            3 to "Biotech",
        )
    }

    fun intToTag(i: Int): String {
        if (!TAG.containsKey(i)){
            throw Exception("This tag does not exist!")
        }
        return TAG[i]!!
    }
}