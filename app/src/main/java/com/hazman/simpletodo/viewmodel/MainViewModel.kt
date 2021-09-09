package com.hazman.simpletodo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazman.simpletodo.R
import com.hazman.simpletodo.model.ToDo
import com.hazman.simpletodo.repository.AppRepository
import com.hazman.simpletodo.retrofit.Resource
import com.hazman.simpletodo.utils.QuickSave
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainViewModel(
    private val appRepository: AppRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val quickSave: QuickSave = QuickSave.instance
) :
    ViewModel() {

    private val _toDoList = MutableStateFlow<Resource<ArrayList<ToDo>>>(Resource.Initialize())
    val toDoList: StateFlow<Resource<ArrayList<ToDo>>> = _toDoList

    private val _createToDo = MutableSharedFlow<Resource<ToDo>>(replay = 0)
    val createToDo: SharedFlow<Resource<ToDo>> = _createToDo

    private val _deleteToDo = MutableSharedFlow<Resource<ToDo>>(replay = 0)
    val deleteToDo: SharedFlow<Resource<ToDo>> = _deleteToDo

    private val _updateToDo = MutableSharedFlow<Resource<ToDo>>(replay = 0)
    val updateToDo: SharedFlow<Resource<ToDo>> = _updateToDo

    private val _searchTitleToDo = MutableSharedFlow<Resource<ArrayList<ToDo>>>(replay = 0)
    val searchTitleToDo: SharedFlow<Resource<ArrayList<ToDo>>> = _searchTitleToDo

    private val _filterCompleted = MutableSharedFlow<Resource<ArrayList<ToDo>>>(replay = 0)
    val filterCompleted: SharedFlow<Resource<ArrayList<ToDo>>> = _filterCompleted

    init {
        getToDoList(1)
    }

    fun getToDoList(page: Int) = viewModelScope.launch {
        _toDoList.emit(Resource.Loading())
        try {
            Log.i("???","page:: $page")
            val data = withContext(ioDispatcher) {
                val response = appRepository.getTodoList(page)
                Resource.getResponse { response }
            }
            _toDoList.emit(data)
        } catch (e: Exception) {
            when (e) {
                is IOException -> _toDoList.emit(
                    Resource.Error(messageInt = R.string.no_internet_connection)
                )
                else -> _toDoList.emit(Resource.Error("ToDo List Error: " + e.message))
            }
        }
    }

    fun setToDo(toDo: ToDo) = viewModelScope.launch {
        _createToDo.emit(Resource.Loading())
        try {
            val data = withContext(ioDispatcher) {
                val response = appRepository.setToDo(toDo)
                Resource.getResponse { response }
            }
            _createToDo.emit(data)
        } catch (e: Exception) {
            when (e) {
                is IOException -> _createToDo.emit(
                    Resource.Error(messageInt = R.string.no_internet_connection)
                )
                else -> _createToDo.emit(Resource.Error("Create ToDo Error: " + e.message))
            }
        }
    }

    fun setDeleteToDo(id: String) = viewModelScope.launch {
        _deleteToDo.emit(Resource.Loading())
        try {
            val data = withContext(ioDispatcher) {
                val response = appRepository.deleteToDo(id)
                Resource.getResponse { response }
            }
            _deleteToDo.emit(data)
        } catch (e: Exception) {
            when (e) {
                is IOException -> _deleteToDo.emit(
                    Resource.Error(messageInt = R.string.no_internet_connection)
                )
                else -> _deleteToDo.emit(Resource.Error("Delete ToDo Error: " + e.message))
            }
        }
    }

    fun setUpdateToDo(id: String, toDo: ToDo) = viewModelScope.launch {
        _updateToDo.emit(Resource.Loading())
        try {
            val data = withContext(ioDispatcher) {

                Log.i("???", "todo:: $toDo")
                val response = appRepository.updateToDo(id, toDo)
                Resource.getResponse { response }
            }
            _updateToDo.emit(data)
        } catch (e: Exception) {
            when (e) {
                is IOException -> _updateToDo.emit(
                    Resource.Error(messageInt = R.string.no_internet_connection)
                )
                else -> _updateToDo.emit(Resource.Error("Update ToDo Error: " + e.message))
            }
        }
    }

    fun setSearchTitle(title: String) = viewModelScope.launch {
        _searchTitleToDo.emit(Resource.Loading())
        try {
            val data = withContext(ioDispatcher) {


                val response = appRepository.searchTitle(title)
                Resource.getResponse { response }
            }
            _searchTitleToDo.emit(data)
        } catch (e: Exception) {
            when (e) {
                is IOException -> _searchTitleToDo.emit(
                    Resource.Error(messageInt = R.string.no_internet_connection)
                )
                else -> _searchTitleToDo.emit(Resource.Error("Search Title ToDo Error: " + e.message))
            }
        }
    }

    fun setFilterCompleted(completed: String) = viewModelScope.launch {
        _filterCompleted.emit(Resource.Loading())
        try {
            val data = withContext(ioDispatcher) {

                val response = appRepository.filterCompleted(completed)
                Resource.getResponse { response }
            }
            _filterCompleted.emit(data)
        } catch (e: Exception) {
            when (e) {
                is IOException -> _filterCompleted.emit(
                    Resource.Error(messageInt = R.string.no_internet_connection)
                )
                else -> _filterCompleted.emit(Resource.Error("Filter ToDo Error: " + e.message))
            }
        }
    }


}