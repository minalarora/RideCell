package com.minal.ridecell.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minal.ridecell.extensions.getData
import com.minal.ridecell.models.RequestUser
import com.minal.ridecell.models.Token
import com.minal.ridecell.networking.ApiInterface
import com.minal.ridecell.networking.Result
import com.minal.ridecell.networking.RetrofitClient
import com.minal.ridecell.networking.SafeApiRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SplashViewModel() : ViewModel(), SafeApiRequest {
    private var _scheduleJob: Job? = null
    private val apiClient : ApiInterface = RetrofitClient.apiInterface

    fun checkCurrentUser(context: Context, onComplete: (RequestUser?) -> Unit)
    {
        _scheduleJob?.cancel()
        _scheduleJob = viewModelScope.launch {
                context.getData("token")
                    ?.let {
                        val result = safeApiCall(call = { apiClient.getCurrentUser(it) })
                        when(result)
                        {
                            is Result.Success -> onComplete(result.data)
                            else -> onComplete(null)
                        }
                } ?: onComplete(null)
        }
    }
}