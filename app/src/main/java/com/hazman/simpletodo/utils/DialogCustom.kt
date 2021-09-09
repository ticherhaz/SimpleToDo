package com.hazman.simpletodo.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import com.hazman.simpletodo.R
import com.hazman.simpletodo.callback.CreateToDoCallback
import com.hazman.simpletodo.callback.EditToDoCallback
import com.hazman.simpletodo.callback.FilterToDoCallback
import com.hazman.simpletodo.databinding.DialogCreateTodoBinding
import com.hazman.simpletodo.databinding.DialogEditTodoBinding
import com.hazman.simpletodo.databinding.DialogFilterBinding
import com.hazman.simpletodo.model.ToDo

object DialogCustom {

    fun createToDo(activity: Activity, createToDoCallback: CreateToDoCallback) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogCreateTodoBinding.inflate(activity.layoutInflater)
        val root = binding.root
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        binding.btnCreate.setOnClickListener {
            handleCreate(binding, createToDoCallback)
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            createToDoCallback.onDismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun handleCreate(
        binding: DialogCreateTodoBinding,
        createToDoCallback: CreateToDoCallback
    ) {
        val title = binding.tietTitle.text.toString()
        if (title.isEmpty()) {
            binding.tietTitle.error = "Please fill in the blank"
        } else {
            binding.tietTitle.error = null
        }

        val toDo = ToDo(
            title = title,
            completed = false,
            userId = "fakeUid"
        )
        createToDoCallback.onSubmit(toDo = toDo)
    }

    fun editToDo(activity: Activity, toDo: ToDo, editToDoCallback: EditToDoCallback) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogEditTodoBinding.inflate(activity.layoutInflater)
        val root = binding.root
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val title = toDo.title
        binding.tietTitle.setText(title)

        setRadioButton(binding, toDo)

        binding.btnUpdate.setOnClickListener {
            handleUpdate(binding, toDo, editToDoCallback)
            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            handleDelete(binding, toDo, editToDoCallback)
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            editToDoCallback.onDismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private var mCompleted = false
    private fun setRadioButton(binding: DialogEditTodoBinding, toDo: ToDo) {
        //Init view of the radio button
        if (toDo.completed != null) {
            if (toDo.completed!!) {
                binding.rbCompleted.isChecked = true
            } else {
                binding.rbNotCompleted.isChecked = true
            }
        }

        binding.rgCompleted.setOnCheckedChangeListener { _, radioButton ->
            when (radioButton) {
                R.id.rb_completed ->
                    mCompleted = true

                R.id.rb_not_completed ->
                    mCompleted = false

            }
        }
    }

    private fun handleUpdate(
        binding: DialogEditTodoBinding,
        toDo: ToDo,
        editToDoCallback: EditToDoCallback
    ) {
        val title = binding.tietTitle.text.toString()
        if (title.isEmpty()) {
            binding.tietTitle.error = "Please fill in the blank"
        } else {
            binding.tietTitle.error = null
        }

        toDo.title = title
        toDo.completed = mCompleted

        editToDoCallback.onEdit(toDo = toDo)
    }

    private fun handleDelete(
        binding: DialogEditTodoBinding,
        toDo: ToDo,
        editToDoCallback: EditToDoCallback
    ) {
        val title = binding.tietTitle.text.toString()
        if (title.isEmpty()) {
            binding.tietTitle.error = "Please fill in the blank"
        } else {
            binding.tietTitle.error = null
        }
        editToDoCallback.onDelete(toDo = toDo)
    }


    fun filterToDo(activity: Activity, filterToDoCallback: FilterToDoCallback) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogFilterBinding.inflate(activity.layoutInflater)
        val root = binding.root
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        binding.btnFilter.setOnClickListener {
            handleFilter(binding, filterToDoCallback)
            dialog.dismiss()
        }

        binding.btnReset.setOnClickListener {
            filterToDoCallback.onReset()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            filterToDoCallback.onDismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private var filterCompleted: String? = null
    private fun handleFilter(binding: DialogFilterBinding, filterToDoCallback: FilterToDoCallback) {
        filterCompleted = handleCheckBoxCompleted(binding)

        if (filterCompleted!!.endsWith(",")) {
            filterCompleted = filterCompleted!!.substring(0, filterCompleted!!.length - 1)
        }
        filterToDoCallback.onFilter(filterCompleted!!)
    }

    private fun handleCheckBoxCompleted(bind: DialogFilterBinding): String {
        var filtered = ""
        if (bind.cbCompleted.isChecked) {
            filtered += "true,"
        }
        if (bind.cbNotCompleted.isChecked) {
            filtered += "false,"
        }
        return filtered
    }

}