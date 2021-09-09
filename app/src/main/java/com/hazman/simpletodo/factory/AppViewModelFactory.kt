package com.hazman.simpletodo.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hazman.simpletodo.repository.AppRepository
import com.hazman.simpletodo.viewmodel.MainViewModel

class AppViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(AppRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}