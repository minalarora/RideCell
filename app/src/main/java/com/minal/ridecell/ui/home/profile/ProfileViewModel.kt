package com.minal.ridecell.ui.home.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minal.ridecell.models.RequestUser
import com.minal.ridecell.networking.ApiInterface
import com.minal.ridecell.networking.Result
import com.minal.ridecell.networking.RetrofitClient
import com.minal.ridecell.networking.SafeApiRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime

class ProfileViewModel() : ViewModel(), SafeApiRequest {

    private var _scheduleJob: Job? = null
    private val apiClient: ApiInterface = RetrofitClient.apiInterface
    private val _user = MutableLiveData<RequestUser>()
    private val _error = MutableLiveData<String>()

    val user: LiveData<RequestUser> = _user
    val error: LiveData<String> = _error

    fun checkCurrentUser(token: String?) {
        _scheduleJob?.cancel()
        _scheduleJob = viewModelScope.launch {
            token?.let {
                    val result = safeApiCall(call = { apiClient.getCurrentUser(it) })
                    when (result) {
                        is Result.Success -> _user.value = result.data
                        is Result.Error -> _error.value = result.message
                    }
                }
        }
    }

    fun prettyPrint(dateString: String?) : String
    {
        return dateString?.let {
            return try {
                val days = Duration.between(OffsetDateTime.parse(it)
                    .toLocalDateTime(), LocalDateTime.now()).toDays()

                "$days days"
            } catch (e: Exception) {
                ""
            }
        } ?: ""
    }
}