package com.example.projecte_android.data

object DataSource {
    val items: MutableList<MyItem> = mutableListOf(
        MyItem(title = "Recordatoris", category = "Personal"),
        MyItem(title = "Projectes", category = "Classe"),
        MyItem(title = "Exàmens", category = "Classe"),
        MyItem(title = "Deures", category = "Classe"),
        MyItem(title = "Dia a dia", category = "Personal"),
        MyItem(title = "Cites", category = "Personal"),
        MyItem(title = "Important", category = "Personal")
    )
}