package com.minal.ridecell.ui.signup

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

class SignUpViewModel() : ViewModel(), SafeApiRequest {

    private var _scheduleJob: Job? = null
    private val apiClient: ApiInterface = RetrofitClient.apiInterface
    private val _token = MutableLiveData<Token>()
    private val _error = MutableLiveData<String>()

    val token: LiveData<Token> = _token
    val error: LiveData<String> = _error

    fun createUser(email: String, pwd: String, confirmPwd: String, name: String) {
        _scheduleJob?.cancel()

        _scheduleJob = viewModelScope.launch {

            if (email.isEmpty() || pwd.isEmpty() || name.isEmpty()) {
                _error.value = "Please fill the required values!"
            } else if (!email.isValidEmail()) {
                _error.value = "Email is not valid!"
            } else if (pwd != confirmPwd) {
                _error.value = "Password Mismatch!"
            } else {
                val result =
                    safeApiCall(call = { apiClient.createUser(RequestUser(email = email, password = pwd, display_name = name)) })
                when(result)
                {
                    is Result.Success -> _token.value = result.data
                    is Result.Error -> _error.value = result.message
                }
            }
        }
    }
}