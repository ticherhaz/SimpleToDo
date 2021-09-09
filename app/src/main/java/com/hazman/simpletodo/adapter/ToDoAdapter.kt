package com.hazman.simpletodo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hazman.simpletodo.R
import com.hazman.simpletodo.callback.ToDoAdapterCallback
import com.hazman.simpletodo.model.ToDo

class ToDoAdapter(
    private val context: Context,
    private val toDoAdapterCallback: ToDoAdapterCallback
) :
    RecyclerView.Adapter<ToDoAdapter.BaseItemHolder>() {

    companion object {
        private const val VIEW_TYPE_LOADING = 1
        private const val VIEW_TYPE_NORMAL = 2
    }

    private var mShowLoading = false
    private var todoList = mutableListOf<ToDo>()

    fun setData(
        newTodoList: MutableList<ToDo>
    ) {
        val diffUtil = DiffUtilToDo(todoList, newTodoList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)

        this.todoList.clear()
        this.todoList.addAll(newTodoList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder {
        when (viewType) {
            VIEW_TYPE_NORMAL -> return NormalViewHolder.create(parent)
            VIEW_TYPE_LOADING -> return LoadingViewHolder.create(parent)
        }
        return BaseItemHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseItemHolder, position: Int) {
        if (holder is NormalViewHolder) {
            holder.bind(context,todoList[position], toDoAdapterCallback)
        } else if (holder is LoadingViewHolder) {
            //do nothing
            //LoadingViewHolder does not need bind data
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < todoList.size) {
            return VIEW_TYPE_NORMAL
        }
        return VIEW_TYPE_LOADING
    }

    fun hideLoading() {
        mShowLoading = false
        //be careful with the index
        notifyItemRemoved(itemCount)
    }

    fun showLoading() {
        mShowLoading = true
        //be careful with the index
        notifyItemInserted(itemCount - 1)
    }

    override fun getItemCount(): Int {
        // return data list size + loading item size
        return todoList.size + if (mShowLoading) {
            1
        } else {
            0
        }
    }

    fun updateList(list: MutableList<ToDo>) {
        todoList = list
        //notifyDataSetChanged()
    }

    fun setList(list: MutableList<ToDo>) {
        todoList = list
        //notifyDataSetChanged()
    }

    fun addAll(newList: MutableList<ToDo>) {
        val lastIndex: Int = todoList.size - 1
        todoList.addAll(newList)
        notifyItemRangeInserted(lastIndex, newList.size)
    }


    open class BaseItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : BaseItemHolder(itemView) {


        companion object {
            fun create(parent: ViewGroup): LoadingViewHolder {
                //R.layout.layout_loading_item just contain a progress bar or something like that
                return LoadingViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_progress, parent, false)
                )
            }
        }

        // LoadingViewHolder does not need bind data
        //fun bind() {
        //
        //}

    }

    class NormalViewHolder(itemView: View) : BaseItemHolder(itemView) {

        private var clMain: ConstraintLayout = itemView.findViewById(R.id.cl_main)
        private var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private var ivCheck: ImageView = itemView.findViewById(R.id.iv_check)

        companion object {
            fun create(parent: ViewGroup): NormalViewHolder {
                return NormalViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_todo, parent, false)
                )
            }
        }

        fun bind(itemModel: ToDo) {
            //bind your data with ItemModel data
        }

        fun bind(context: Context,toDo: ToDo, toDoAdapterCallback: ToDoAdapterCallback) {
            tvTitle.text = toDo.title
            setIvCheck(context,toDo.completed)
            clMain.setOnClickListener {
                toDoAdapterCallback.onItemClick()
            }
            clMain.setOnLongClickListener {
                toDoAdapterCallback.onItemLongClick(toDo)
                return@setOnLongClickListener true
            }
        }

        private fun setIvCheck(context: Context,completed: Boolean?) {
            if (completed != null) {
                if (completed) {
                    ivCheck.setColorFilter(context.getColor(R.color.color_green))
                } else {
                    ivCheck.setColorFilter(context.getColor(R.color.color_red))
                }
                ivCheck.isVisible = true
            } else {
                ivCheck.isVisible = false
            }
        }


    }
}