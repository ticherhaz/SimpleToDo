package com.hazman.simpletodo.callback

import com.hazman.simpletodo.model.ToDo

interface FilterToDoCallback {
    fun onFilter(filter : String)
    fun onReset()
    fun onDismiss()
}