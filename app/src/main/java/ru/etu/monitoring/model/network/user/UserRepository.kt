package ru.etu.monitoring.model.network.user

import io.reactivex.Observable
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.BaseRepository
import ru.etu.monitoring.model.network.data.request.ConfirmLoginRequest
import ru.etu.monitoring.model.network.data.request.LoginRequest
import ru.etu.monitoring.model.network.data.response.ConfirmLoginResponse
import ru.etu.monitoring.model.network.data.response.LoginResponse

class UserRepository(val api: UserApi) : BaseRepository() {
    val isAuthorized: Boolean
        get() = userPreferences.isAuthorized

    fun getUserLocal(): Observable<User> {
        return userPreferences.getUserLocal()
    }

    fun login(phone: String): Observable<LoginResponse> {
        return userApi.login(LoginRequest(phone))
            .compose(handleErrors())
    }

    fun confirmLogin(phone: String, code: String): Observable<ConfirmLoginResponse> {
        return userApi.confirmLogin(ConfirmLoginRequest(phone, code))
            .compose(handleErrors())
            .doOnNext {
                userPreferences.authToken = it.token
            }
    }
}
