package com.example.projecte_android

data class MyItem(
    val id: Int = 0,
    val title: String,
    val category: String = "General",
    val imagePath: String? = null,
    val completed: Boolean = false,
    val dataCreated: String? = null,
    val dataUpdated: String? = null
)