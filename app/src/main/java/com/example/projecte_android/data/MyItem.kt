package com.example.projecte_android.data

data class MyItem(
    val id: Long = 0L,
    val title: String,
    val category: String = "General",
    val imagePath: String? = null,
    val completed: Boolean = false,
    val dataCreated: String? = null,
    val dataUpdated: String? = null
)