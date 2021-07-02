package com.minal.ridecell.networking

import org.json.JSONObject
import retrofit2.Response


interface SafeApiRequest {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {

        return try {
            val response = call.invoke()

            response.body()?.let {
                Result.Success(it)
            } ?: response.errorBody()?.let { error ->
                 Result.Error(JSONObject(error.string()).getString("message"))
            } ?: Result.Error("Unknown Error!")

        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }
}