package ru.etu.monitoring.model.network.patient


import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.data.request.ConfirmLoginRequest
import ru.etu.monitoring.model.network.data.request.CreateIllnessRequest
import ru.etu.monitoring.model.network.data.request.LoginRequest
import ru.etu.monitoring.model.network.data.request.SignUpRequest
import ru.etu.monitoring.model.network.data.response.BaseResponse
import ru.etu.monitoring.model.network.data.response.auth.ConfirmLoginResponse
import ru.etu.monitoring.model.network.data.response.auth.LoginResponse
import ru.etu.monitoring.model.network.data.response.auth.SignUpResponse

interface PatientApi {
    @POST("order/create")
    fun createIllRequest(@Body request: CreateIllnessRequest): Observable<Response<BaseResponse<LoginResponse>>>
}