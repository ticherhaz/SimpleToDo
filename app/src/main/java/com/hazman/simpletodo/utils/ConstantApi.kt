package com.hazman.simpletodo.utils

object ConstantApi {
    private const val TODO = "todo/"
    const val PATH_ID = "id"

    const val LIST = TODO
    const val DETAIL = "${TODO}{$PATH_ID}"

    const val TITLE = "title"
    const val COMPLETED = "completed"
    const val PAGE = "page"
    const val LIMIT = "limit"
}