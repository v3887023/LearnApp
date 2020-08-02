package com.example.lib_annotations

annotation class Subject(
    val title: String,
    val description: String = "",
    val isTest: Boolean = false
)