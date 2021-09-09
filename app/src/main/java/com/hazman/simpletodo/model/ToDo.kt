package com.hazman.simpletodo.model

import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName

data class ToDo(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("userID")
    val userId: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("completed")
    var completed: Boolean? = null,
) : ViewModel()