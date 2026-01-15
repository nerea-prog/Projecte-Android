package com.example.act07_menu_llista_filtre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val itemsOriginal: MutableList<MyItem>,
    private val onItemClick: (MyItem, Int) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {

    private var itemsFiltered: MutableList<MyItem> = itemsOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = itemsFiltered.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemsFiltered[position], position, onItemClick)
    }

    fun filter(query: String) {
        itemsFiltered = if (query.isEmpty()) {
            // Si no hi ha text, mostra tot
            itemsOriginal.toMutableList()
        } else {
            // Filtra els items que contenen el text (ignora majúscules/minúscules)
            itemsOriginal.filter { item ->
                item.title.lowercase().contains(query.lowercase())
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun clearFilter() {
        itemsFiltered = itemsOriginal.toMutableList()
        notifyDataSetChanged()
    }

    // Metodes per editar la llista dinamica
    fun addItem(item: MyItem) {
        itemsOriginal.add(item)
        itemsFiltered.add(item)
        notifyItemInserted(itemsFiltered.size - 1)
    }

    fun removeItem(position: Int) {
        if (position in itemsFiltered.indices) {
            val itemToRemove = itemsFiltered[position]
            itemsOriginal.remove(itemToRemove)
            itemsOriginal.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemsFiltered.size)
        }
    }
}