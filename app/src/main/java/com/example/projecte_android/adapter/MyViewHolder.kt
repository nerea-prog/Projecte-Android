package com.example.projecte_android.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.projecte_android.R
import com.example.projecte_android.data.MyItem
import com.example.projecte_android.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)

    fun bind(item: MyItem, position: Int, onItemClick: (MyItem, Int) -> Unit) {
        tvTitle.text = item.title

        cbCompleted.setOnCheckedChangeListener(null)
        cbCompleted.isChecked = item.completed

        cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            val updatedTask = item.copy(completed = isChecked)
            (itemView.context as AppCompatActivity)
                .lifecycleScope.launch {
                    try {
                        withContext(Dispatchers.IO) {
                            RetrofitClient.getApi().updateTask(item.id, updatedTask)
                        }
                    } catch (e: Exception) {
                        cbCompleted.isChecked = !isChecked
                    }
                }
        }

        itemView.setOnClickListener {
            onItemClick(item, position)
        }
    }
}