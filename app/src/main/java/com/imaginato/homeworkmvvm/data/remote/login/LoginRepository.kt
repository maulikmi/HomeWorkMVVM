package com.imaginato.homeworkmvvm.data.remote.login

import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun callLogin(request: LoginRequest): Flow<String>
}