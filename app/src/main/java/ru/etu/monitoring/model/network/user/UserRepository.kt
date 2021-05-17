package ru.etu.monitoring.model.network.user

import io.reactivex.Observable
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.BaseRepository
import ru.etu.monitoring.model.network.data.request.ConfirmLoginRequest
import ru.etu.monitoring.model.network.data.request.LoginRequest
import ru.etu.monitoring.model.network.data.request.SendFirebaseTokenRequest
import ru.etu.monitoring.model.network.data.request.SignUpRequest
import ru.etu.monitoring.model.network.data.response.SendFirebaseTokenResponse
import ru.etu.monitoring.model.network.data.response.auth.ConfirmLoginResponse
import ru.etu.monitoring.model.network.data.response.auth.LoginResponse
import ru.etu.monitoring.model.network.data.response.auth.SignUpResponse

class UserRepository(val api: UserApi) : BaseRepository() {
    val isAuthorized: Boolean
        get() = userPreferences.isAuthorized

    fun getUserLocal(): Observable<User> {
        return userPreferences.getUserLocal()
    }

    fun logout() {
        userPreferences.clearUserData()
    }

    fun login(phone: String): Observable<LoginResponse> {
        return userApi.login(LoginRequest(phone))
            .compose(handleErrors())
    }

    fun confirmLogin(phone: String, code: String): Observable<ConfirmLoginResponse> {
        return userApi.confirmLogin(ConfirmLoginRequest(phone, code))
            .compose(handleErrors())
            .doOnNext {
                userPreferences.authToken = it.authKey
                userPreferences.isDoctor = it.isDoctor
            }
    }

    fun updateFirebaseToken(token: String): Observable<SendFirebaseTokenResponse> {
        return userApi.sendFirebaseToken(SendFirebaseTokenRequest(token))
            .compose(handleErrors())
    }

    fun getProfile(): Observable<User> {
        return userApi.getProfile()
            .compose(handleErrors())
            .doOnNext {
                userPreferences.saveUser(it)
            }
    }

    fun signUp(
        firstName: String,
        lastName: String,
        middleName: String,
        birthday: String,
        code: String,
        phone: String
    ): Observable<SignUpResponse> {
        return userApi.signUp(SignUpRequest(phone, firstName, lastName, middleName, birthday, code))
            .compose(handleErrors())
            .doOnNext {
                userPreferences.authToken = it.authKey
                userPreferences.isDoctor = it.isDoctor
            }
    }
}
