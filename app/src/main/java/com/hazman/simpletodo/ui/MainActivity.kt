package com.hazman.simpletodo.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.hazman.simpletodo.R
import com.hazman.simpletodo.adapter.ToDoAdapter
import com.hazman.simpletodo.callback.CreateToDoCallback
import com.hazman.simpletodo.callback.EditToDoCallback
import com.hazman.simpletodo.callback.FilterToDoCallback
import com.hazman.simpletodo.callback.ToDoAdapterCallback
import com.hazman.simpletodo.databinding.ActivityMainBinding
import com.hazman.simpletodo.factory.AppViewModelFactory
import com.hazman.simpletodo.model.ToDo
import com.hazman.simpletodo.retrofit.Resource
import com.hazman.simpletodo.utils.DialogCustom
import com.hazman.simpletodo.utils.Tools
import com.hazman.simpletodo.utils.Tools.amountPercentageBeingScrolled
import com.hazman.simpletodo.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { AppViewModelFactory() }
    private var list: MutableList<ToDo> = mutableListOf()

    private var loading = false
    private var isMore = false
    private var page = 1

    companion object {
        private const val TOTAL_ITEM_DISPLAY = 18
    }

    private val toDoAdapter: ToDoAdapter by lazy {
        ToDoAdapter(this@MainActivity, object : ToDoAdapterCallback {
            override fun onItemClick() {
                // Tools.showToast(this@MainActivity, "Click")
            }

            override fun onItemLongClick(toDo: ToDo) {
                handleEditToDo(toDo)
            }
        })
    }

    private fun setupRv() {
        binding.rvTodo.itemAnimator = null //Remove animation when adding new data

        binding.rvTodo.apply {
            adapter = toDoAdapter
        }

        val rvOnScroll = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val percentage = amountPercentageBeingScrolled(binding.rvTodo)
                if (percentage in 80..85) {
                    Log.i("???", "percentage")

                    if (isMore && !loading) {

                        loading = true
                        toDoAdapter.showLoading()
                        page++

                        viewModel.getToDoList(page)
                    }

                }
            }
        }

        binding.rvTodo.addOnScrollListener(rvOnScroll)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        setupRv()
        startLifeCycle()

        setFabCreateToDo()
    }

    private fun startLifeCycle() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    collectToDoList()
                }
                launch {
                    collectCreateToDo()
                }
                launch {
                    collectDeleteToDo()
                }
                launch {
                    collectUpdateToDo()
                }
                launch {
                    collectSearchTitleToDo()
                }
                launch {
                    collectFilterToDo()
                }
            }
        }
    }

    private var updateToDo = false
    private suspend fun collectToDoList() {
        viewModel.toDoList.collect {
            when (it) {
                is Resource.Initialize -> {
                }
                is Resource.Loading -> {
                    loading = true
                    if (page > 0) {
                        toDoAdapter.showLoading()
                    }
                }
                is Resource.Success -> {
                    val toDoList = it.data
                    if (toDoList != null) {
                        list = toDoList

                        loading = false

                        if (page > 1) {
                            Log.i("aaa", "asww")

                            toDoAdapter.addAll(list)
                            if (list.size == TOTAL_ITEM_DISPLAY) {
                                isMore = true
                            }

                            toDoAdapter.hideLoading()
                        } else {

                            Log.i("aaa", "fff:: ${list.size}")

                            if (list.size > 0) {
                                if (list.size == TOTAL_ITEM_DISPLAY) {
                                    isMore = true
                                }
                                toDoAdapter.setData(list)
                            }

                        }


                    }
                }
                is Resource.Error -> {
                    val errorMessage = it
                    val messageInt = errorMessage.messageInt
                    val message = errorMessage.message
                    val code = errorMessage.code

                    if (code != null) {
                        val errorReader = errorMessage.errorReader
                        Tools.showToast(
                            this@MainActivity,
                            "$errorReader"
                        )
                    } else {
                        if (messageInt != null) {
                            Tools.showToast(this@MainActivity, getString(messageInt))
                        }
                        if (message != null) {
                            Tools.showToast(
                                this@MainActivity,
                                "$message"
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun collectCreateToDo() {
        viewModel.createToDo.collect {
            when (it) {
                is Resource.Initialize -> {
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val toDoList = it.data
                    if (toDoList != null) {
                        Tools.showToast(this@MainActivity, "Success")
                        viewModel.getToDoList(1)
                    }
                    binding.fabAdd.isEnabled = true
                }
                is Resource.Error -> {
                    val errorMessage = it
                    val messageInt = errorMessage.messageInt
                    val message = errorMessage.message
                    val code = errorMessage.code
                    Log.i("???", "error:: $message")

                    if (code != null) {
                        val errorReader = errorMessage.errorReader
                        Tools.showToast(
                            this@MainActivity,
                            "$errorReader"
                        )
                    } else {
                        if (messageInt != null) {
                            Tools.showToast(this@MainActivity, getString(messageInt))
                        }
                        if (message != null) {
                            Tools.showToast(
                                this@MainActivity,
                                "$message"
                            )
                        }
                    }
                    binding.fabAdd.isEnabled = true
                }
            }
        }
    }

    private suspend fun collectDeleteToDo() {
        viewModel.deleteToDo.collect {
            when (it) {
                is Resource.Initialize -> {
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val toDoList = it.data
                    if (toDoList != null) {
                        Tools.showToast(this@MainActivity, "Deleted success")
                        viewModel.getToDoList(1)
                    }
                    binding.fabAdd.isEnabled = true
                }
                is Resource.Error -> {
                    val errorMessage = it
                    val messageInt = errorMessage.messageInt
                    val message = errorMessage.message
                    val code = errorMessage.code
                    Log.i("???", "error:: $message")

                    if (code != null) {
                        val errorReader = errorMessage.errorReader
                        Tools.showToast(
                            this@MainActivity,
                            "$errorReader"
                        )
                    } else {
                        if (messageInt != null) {
                            Tools.showToast(this@MainActivity, getString(messageInt))
                        }
                        if (message != null) {
                            Tools.showToast(
                                this@MainActivity,
                                "$message"
                            )
                        }
                    }
                    binding.fabAdd.isEnabled = true
                }
            }
        }
    }

    private suspend fun collectUpdateToDo() {
        viewModel.updateToDo.collect {
            when (it) {
                is Resource.Initialize -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val toDoList = it.data
                    if (toDoList != null) {
                        Tools.showToast(this@MainActivity, "Updated success")
                        updateToDo = true
                        viewModel.getToDoList(1)
                    }
                    binding.fabAdd.isEnabled = true
                }
                is Resource.Error -> {
                    val errorMessage = it
                    val messageInt = errorMessage.messageInt
                    val message = errorMessage.message
                    val code = errorMessage.code
                    if (code != null) {
                        val errorReader = errorMessage.errorReader
                        Tools.showToast(
                            this@MainActivity,
                            "$errorReader"
                        )
                    } else {
                        if (messageInt != null) {
                            Tools.showToast(this@MainActivity, getString(messageInt))
                        }
                        if (message != null) {
                            Tools.showToast(
                                this@MainActivity,
                                "$message"
                            )
                        }
                    }
                    binding.fabAdd.isEnabled = true
                }
            }
        }
    }

    private suspend fun collectSearchTitleToDo() {
        viewModel.searchTitleToDo.collect {
            when (it) {
                is Resource.Initialize -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val tempToDoList = it.data
                    if (tempToDoList != null) {
                        // list = tempToDoList
                        toDoAdapter.setData(tempToDoList)
                    }
                }
                is Resource.Error -> {
                    val errorMessage = it
                    val messageInt = errorMessage.messageInt
                    val message = errorMessage.message
                    val code = errorMessage.code
                    if (code != null) {
                        val errorReader = errorMessage.errorReader
                        Tools.showToast(
                            this@MainActivity,
                            "$errorReader"
                        )
                    } else {
                        if (messageInt != null) {
                            Tools.showToast(this@MainActivity, getString(messageInt))
                        }
                        if (message != null) {
                            Tools.showToast(
                                this@MainActivity,
                                "$message"
                            )
                        }
                    }
                    binding.fabAdd.isEnabled = true
                }
            }
        }
    }

    private suspend fun collectFilterToDo() {
        viewModel.filterCompleted.collect {
            when (it) {
                is Resource.Initialize -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val tempToDoList = it.data
                    if (tempToDoList != null) {
                        // list = tempToDoList
                        toDoAdapter.setData(tempToDoList)
                    }
                }
                is Resource.Error -> {
                    val errorMessage = it
                    val messageInt = errorMessage.messageInt
                    val message = errorMessage.message
                    val code = errorMessage.code
                    if (code != null) {
                        val errorReader = errorMessage.errorReader
                        Tools.showToast(
                            this@MainActivity,
                            "$errorReader"
                        )
                    } else {
                        if (messageInt != null) {
                            Tools.showToast(this@MainActivity, getString(messageInt))
                        }
                        if (message != null) {
                            Tools.showToast(
                                this@MainActivity,
                                "$message"
                            )
                        }
                    }
                    binding.fabAdd.isEnabled = true
                }
            }
        }
    }

    private fun setFabCreateToDo() {
        binding.fabAdd.setOnClickListener {
            binding.fabAdd.isEnabled = false
            DialogCustom.createToDo(this@MainActivity, object : CreateToDoCallback {
                override fun onDismiss() {
                    binding.fabAdd.isEnabled = true
                }

                override fun onSubmit(toDo: ToDo) {
                    viewModel.setToDo(toDo)
                }
            })
        }
    }

    private fun handleEditToDo(toDo: ToDo) {
        DialogCustom.editToDo(this@MainActivity, toDo, object : EditToDoCallback {
            override fun onDismiss() {
            }

            override fun onDelete(toDo: ToDo) {
                viewModel.setDeleteToDo(toDo.id!!)
            }

            override fun onEdit(toDo: ToDo) {
                viewModel.setUpdateToDo(toDo.id!!, toDo)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu!!.findItem(R.id.action_sch)
        val searchView = menuItem.actionView as SearchView

        searchView.queryHint = "Type here to search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.llRecordNotFound.isVisible = false

                if (newText.isNotBlank() && newText.isNotEmpty()) {
                    /* filterToDoList(newText.uppercase())
                       val searchResult = "No result found for '$newText'"
                       binding.tvRecordNotFound.text = searchResult*/


                    viewModel.setSearchTitle(newText)


                    return true
                } else {
                    Log.i("xxx", "tkda")
                    binding.llRecordNotFound.isVisible = false
                    toDoAdapter.setData(list)

                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.action_sch -> {

                true
            }
            R.id.action_filter -> {
                setActionFilter()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*private fun filterToDoList(text: String) {
        val temp: MutableList<ToDo> = ArrayList()
        lifecycleScope.launch {
            for (d in list) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches

                if (d.title != null) {
                    if (d.title!!.uppercase().contains(text)) {
                        temp.add(d)
                    }
                }
            }

            if (temp.size == 0) {
                val searchResult = "No result found for '$text'"
                binding.tvRecordNotFound.text = searchResult
                binding.llRecordNotFound.isVisible = true
            }

            //update recyclerview
            toDoAdapter.setData(temp)
        }
    }*/

    private fun setActionFilter() {
        DialogCustom.filterToDo(this@MainActivity, object : FilterToDoCallback {
            override fun onReset() {
                list.clear()
                viewModel.getToDoList(1)
            }

            override fun onDismiss() {

            }

            override fun onFilter(filter: String) {
                viewModel.setFilterCompleted(filter)
            }
        })
    }
}