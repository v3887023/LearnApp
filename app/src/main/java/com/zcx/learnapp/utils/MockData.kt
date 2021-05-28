package com.zcx.learnapp.utils

object MockData {
    fun numberStringList(start: Int = 0, count: Int = 10): List<String> {
        return mutableListOf<String>().also {
            repeat(count) { i ->
                it.add("${start + i}")
            }
        }
    }
}