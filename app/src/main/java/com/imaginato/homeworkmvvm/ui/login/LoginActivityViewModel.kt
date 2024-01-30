package com.imaginato.homeworkmvvm.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imaginato.homeworkmvvm.data.remote.login.LoginRepository
import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import com.imaginato.homeworkmvvm.ui.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LoginActivityViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {

    private val _loginValidation = MutableLiveData<LoginValidation>()
    val loginValidation: LiveData<LoginValidation>
        get() {
            return _loginValidation
        }

    private var _isInProgress: MutableLiveData<Boolean> = MutableLiveData()
    val isInProgress: LiveData<Boolean>
        get() {
            return _isInProgress
        }

    private var _loginResultData: MutableLiveData<String> = MutableLiveData()
    val loginResultData: LiveData<String>
        get() {
            return _loginResultData
        }

    /**
     * Login Api call
     * */
    fun callLogin(userName: String, password: String) {
        viewModelScope.launch {
            val loginRequest = LoginRequest(userName, password)
            loginRepository.callLogin(loginRequest).onStart {
                _isInProgress.value = true
            }.catch {
                _isInProgress.value = false
            }.onCompletion {}.collect {
                _isInProgress.value = false
                _loginResultData.value = it
            }
        }
    }

    /**
     * Check login validation
     */
    fun checkLoginValidation(userName: String, password: String) {
        when {
            userName.isEmpty() -> {
                _loginValidation.value = LoginValidation.IS_USERNAME_BLANK
            }

            password.isEmpty() -> {
                _loginValidation.value = LoginValidation.IS_PASSWORD_BLANK
            }

            else -> {
                _loginValidation.value = LoginValidation.VALID_INPUT
            }
        }
    }
}