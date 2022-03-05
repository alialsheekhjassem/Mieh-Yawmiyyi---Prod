package com.magma.miyyiyawmiyyi.android.data.repository

import com.magma.miyyiyawmiyyi.android.data.local.repository.LocalRepository
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ResponseWrapper
import com.magma.miyyiyawmiyyi.android.data.remote.requests.LoginRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.RegisterRequest
import com.magma.miyyiyawmiyyi.android.data.remote.requests.ResetPasswordRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.LoginResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import javax.inject.Inject

class DataRepository
@Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : DataSource {

    //Api
    override suspend fun doServerLogin(loginRequest: LoginRequest): Resource<LoginResponse> {
        return remoteRepository.doServerLogin(loginRequest)
    }

    override suspend fun doServerRegister(registerRequest: RegisterRequest): Resource<ResponseWrapper<String>> {
        return remoteRepository.doServerRegister(registerRequest)
    }

    override suspend fun doServerResetPassword(request: ResetPasswordRequest): Resource<ResponseWrapper<String>> {
        return remoteRepository.doServerResetPassword(request)
    }

    override suspend fun getTasks(limit: Int, offset: Int): Resource<TasksResponse> {
        return remoteRepository.getTasks(limit, offset)
    }

    override fun loadAllTasks(): List<TaskObj> {
        return localRepository.loadAllTasks()
    }

    override fun loadAllTasks(type: String): List<TaskObj> {
        return localRepository.loadAllTasks(type)
    }

    override fun loadTask(id: String): TaskObj {
        return localRepository.loadTask(id)
    }

    override fun insertTaskList(items: List<TaskObj>) {
        localRepository.insertTaskList(items)
    }

    override fun insertTask(item: TaskObj) {
        localRepository.insertTask(item)
    }

    override fun updateTask(item: TaskObj) {
        localRepository.updateTask(item)
    }

    override fun updateTasks(items: List<TaskObj>) {
        localRepository.updateTasks(items)
    }

    override fun deleteTask(item: TaskObj) {
        localRepository.deleteTask(item)
    }

    override fun deleteAllTasks() {
        localRepository.deleteAllTasks()
    }


    //Pref
    override fun setApiToken(apiToken: String) {
        localRepository.setApiToken(apiToken)
    }

    override fun getApiToken(): String? {
        return localRepository.getApiToken()
    }

    override fun setLang(lang: String) {
        localRepository.setLang(lang)
    }

    override fun getLang(): String? {
        return localRepository.getLang()
    }

    override fun setIsShownOnBoarding(isShown: Boolean) {
        return localRepository.setIsShownOnBoarding(isShown)
    }

    override fun isShownOnBoarding(): Boolean {
        return localRepository.isShownOnBoarding()
    }

}

