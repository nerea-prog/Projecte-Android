package com.example.projecte_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projecte_android.R
import com.example.projecte_android.data.MyItem

class MyAdapter(
    private var items: List<MyItem>,
    private val onItemClick: (MyItem, Int) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position], position, onItemClick)
    }
    fun updateList(newList: List<MyItem>) {
        items = newList
        notifyDataSetChanged()
    }
}