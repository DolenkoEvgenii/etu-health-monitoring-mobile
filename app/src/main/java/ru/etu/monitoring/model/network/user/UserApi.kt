package ru.etu.monitoring.model.network.user


import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.etu.monitoring.model.network.data.request.ConfirmLoginRequest
import ru.etu.monitoring.model.network.data.request.LoginRequest
import ru.etu.monitoring.model.network.data.request.SignUpRequest
import ru.etu.monitoring.model.network.data.response.ConfirmLoginResponse
import ru.etu.monitoring.model.network.data.response.LoginResponse
import ru.etu.monitoring.model.network.data.response.SignUpResponse

interface UserApi {
    @Headers("Content-Type:application/json")
    @POST("oauth/token")
    fun login(@Body request: LoginRequest): Observable<Response<LoginResponse>>

    @Headers("Content-Type:application/json")
    @POST("oauth/token")
    fun confirmLogin(@Body request: ConfirmLoginRequest): Observable<Response<ConfirmLoginResponse>>

    @Headers("Content-Type:application/json")
    @POST("oauth/token")
    fun signUp(@Body request: SignUpRequest): Observable<Response<SignUpResponse>>
}