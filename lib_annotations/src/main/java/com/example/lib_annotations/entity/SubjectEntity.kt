package com.example.lib_annotations.entity

data class SubjectEntity(
    val className : String,
    val title: String,
    val description: String = "",
    val isTest: Boolean = false
)