package com.hazman.simpletodo.retrofit

import com.hazman.simpletodo.enumerator.HttpCode
import retrofit2.Response
import java.io.Reader

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val messageInt: Int? = null,
    val code: Int? = null,
    val errorReader: Reader? = null,
) {
    class Initialize<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(
        message: String? = null,
        messageInt: Int? = null,
        data: T? = null,
        code: Int? = null,
        errorReader: Reader? = null
    ) :
        Resource<T>(data, message, messageInt, code, errorReader)

    companion object {
        suspend fun <T> getResponse(call: suspend () -> Response<T>): Resource<T> {
            val response = call.invoke()
            when {
                response.isSuccessful -> {
                    response.body()?.let { resultResponse ->
                        return Success(resultResponse)
                    }
                }
                response.code() == HttpCode.NOT_FOUND.code -> {
                    return Error(
                        messageInt = HttpCode.NOT_FOUND.messageInt,
                        code = HttpCode.NOT_FOUND.code,
                        errorReader = response.errorBody()!!.charStream()
                    )
                }
                response.code() == HttpCode.SERVER_ERROR.code -> {
                    return Error(
                        messageInt = HttpCode.SERVER_ERROR.messageInt,
                        code = HttpCode.SERVER_ERROR.code,
                        errorReader = response.errorBody()!!.charStream()
                    )
                }
                else -> return Error(
                    messageInt = HttpCode.UNKNOWN_ERROR.messageInt,
                    code = HttpCode.UNKNOWN_ERROR.code,
                    errorReader = response.errorBody()!!.charStream()
                )
            }
            return Error(errorReader = response.errorBody()!!.charStream())
        }
    }
}