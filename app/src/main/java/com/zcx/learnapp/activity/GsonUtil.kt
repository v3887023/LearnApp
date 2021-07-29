package com.zcx.learnapp.activity

import com.google.gson.Gson

inline fun <reified T> String.jsonTo(): T {
    return Gson().fromJson(this, T::class.java)
}

fun <T> T.toJson(): String {
    return Gson().toJson(this)
}
