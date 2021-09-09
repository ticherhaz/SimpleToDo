package com.hazman.simpletodo.callback

import com.hazman.simpletodo.model.ToDo

interface EditToDoCallback {
    fun onEdit(toDo: ToDo)
    fun onDelete(toDo: ToDo)
    fun onDismiss()
}