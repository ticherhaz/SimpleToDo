package com.hazman.simpletodo.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hazman.simpletodo.model.ToDo

class DiffUtilToDo(
    private val oldList: MutableList<ToDo>,
    private val newList: MutableList<ToDo>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when (oldList[oldItemPosition].id) {
            newList[newItemPosition].id -> {
                false
            }
            else -> {
                true
            }
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            else -> {
                true
            }
        }
    }
}