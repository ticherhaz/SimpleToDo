package com.hazman.simpletodo.callback

import com.hazman.simpletodo.model.ToDo

interface ToDoAdapterCallback {
    fun onItemClick()
    fun onItemLongClick(toDo: ToDo)
}