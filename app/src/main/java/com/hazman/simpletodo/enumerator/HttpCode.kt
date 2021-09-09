package com.hazman.simpletodo.enumerator

import com.hazman.simpletodo.R


enum class HttpCode(val code: Int, val messageInt: Int) {
    NOT_FOUND(404, R.string.not_found),
    SERVER_ERROR(500, R.string.server_error),
    UNKNOWN_ERROR(501, R.string.unknown_error)
}