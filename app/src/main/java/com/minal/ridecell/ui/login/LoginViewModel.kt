package com.minal.ridecell.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minal.ridecell.extensions.isValidEmail
import com.minal.ridecell.models.RequestUser
import com.minal.ridecell.models.Token
import com.minal.ridecell.networking.ApiInterface
import com.minal.ridecell.networking.Result
import com.minal.ridecell.networking.RetrofitClient
import com.minal.ridecell.networking.SafeApiRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel(), SafeApiRequest {

    private var _scheduleJob: Job? = null
    private val apiClient: ApiInterface = RetrofitClient.apiInterface
    private val _token = MutableLiveData<Token>()
    private val _error = MutableLiveData<String>()

    val token: LiveData<Token> = _token
    val error: LiveData<String> = _error

    fun loginUser(email: String, pwd: String) {
        _scheduleJob?.cancel()

        _scheduleJob = viewModelScope.launch {

            if (email.isEmpty() || pwd.isEmpty()) {
                _error.value = "Please fill the required values!"
            } else if(!email.isValidEmail()) {
                _error.value = "Email is not valid!"
            } else {
                val result = safeApiCall(call = { apiClient.loginUser(RequestUser(email = email, password = pwd)) })
                when(result)
                {
                    is Result.Success -> _token.value = result.data
                    is Result.Error -> _error.value = result.message
                }
            }
        }
    }
}