package com.hazman.simpletodo.callback

import com.hazman.simpletodo.model.ToDo

interface CreateToDoCallback {
    fun onSubmit(toDo: ToDo)
    fun onDismiss()
}