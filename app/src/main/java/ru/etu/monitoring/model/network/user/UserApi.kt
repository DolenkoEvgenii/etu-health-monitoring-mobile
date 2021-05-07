package ru.etu.monitoring.model.network.user


import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.data.request.ConfirmLoginRequest
import ru.etu.monitoring.model.network.data.request.LoginRequest
import ru.etu.monitoring.model.network.data.request.SignUpRequest
import ru.etu.monitoring.model.network.data.response.BaseResponse
import ru.etu.monitoring.model.network.data.response.auth.ConfirmLoginResponse
import ru.etu.monitoring.model.network.data.response.auth.LoginResponse
import ru.etu.monitoring.model.network.data.response.auth.SignUpResponse

interface UserApi {
    @POST("user/sign-in")
    fun login(@Body request: LoginRequest): Observable<Response<BaseResponse<LoginResponse>>>

    @POST("user/confirm")
    fun confirmLogin(@Body request: ConfirmLoginRequest): Observable<Response<BaseResponse<ConfirmLoginResponse>>>

    @GET("user/profile")
    fun getProfile(): Observable<Response<BaseResponse<User>>>

    @POST("user/confirm")
    fun signUp(@Body request: SignUpRequest): Observable<Response<BaseResponse<SignUpResponse>>>
}