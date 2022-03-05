package com.magma.miyyiyawmiyyi.android.data.repository

import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
import com.magma.miyyiyawmiyyi.android.model.TaskObj

interface DataSource {

    //Api
    suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse>
    suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>>
    suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>>
    suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse>

    //Local
    fun loadAllTasks(): List<TaskObj>
    fun loadAllTasks(type: String): List<TaskObj>
    fun loadTask(id: String): TaskObj
    fun insertTaskList(items: List<TaskObj>)
    fun insertTask(item: TaskObj)
    fun updateTask(item: TaskObj)
    fun updateTasks(items: List<TaskObj>)
    fun deleteTask(item: TaskObj)
    fun deleteAllTasks()

    //Pref
    fun setApiToken(apiToken: String)
    fun getApiToken(): String?
    fun setLang(lang: String)
    fun getLang(): String?
    fun setIsShownOnBoarding(isShown: Boolean)
    fun isShownOnBoarding(): Boolean?
}