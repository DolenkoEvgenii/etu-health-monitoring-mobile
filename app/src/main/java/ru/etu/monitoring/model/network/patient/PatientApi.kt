package ru.etu.monitoring.model.network.patient


import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.data.request.*
import ru.etu.monitoring.model.network.data.response.BaseResponse
import ru.etu.monitoring.model.network.data.response.auth.ConfirmLoginResponse
import ru.etu.monitoring.model.network.data.response.auth.LoginResponse
import ru.etu.monitoring.model.network.data.response.auth.SignUpResponse
import ru.etu.monitoring.model.network.data.response.task.ChangeTaskResponse
import ru.etu.monitoring.model.network.data.response.task.RequestTasksResponse

interface PatientApi {
    @POST("order/create")
    fun createIllRequest(@Body request: CreateIllnessRequest): Observable<Response<BaseResponse<LoginResponse>>>

    @POST("task/mark-done")
    fun markTaskDone(@Body request: ChangeTaskRequest): Observable<Response<BaseResponse<ChangeTaskResponse>>>

    @GET("task/all")
    fun getAllRequestTasks(@Query("order_id") requestId: String): Observable<Response<BaseResponse<RequestTasksResponse>>>
}