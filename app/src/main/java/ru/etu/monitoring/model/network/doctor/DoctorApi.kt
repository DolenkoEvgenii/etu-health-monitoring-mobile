package ru.etu.monitoring.model.network.doctor


import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.model.network.data.request.AcceptOrderRequest
import ru.etu.monitoring.model.network.data.request.ChangeTaskRequest
import ru.etu.monitoring.model.network.data.request.CreateTaskRequest
import ru.etu.monitoring.model.network.data.request.SetHomePointRequest
import ru.etu.monitoring.model.network.data.response.BaseResponse
import ru.etu.monitoring.model.network.data.response.request.ConfirmAcceptOrderResponse
import ru.etu.monitoring.model.network.data.response.request.SetHomePointResponse
import ru.etu.monitoring.model.network.data.response.task.ChangeTaskResponse
import ru.etu.monitoring.model.network.data.response.task.RequestTasksResponse

interface DoctorApi {
    @GET("order/new")
    fun getNewRequests(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<Response<BaseResponse<List<Request>>>>

    @GET("order/current")
    fun getActiveRequests(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<Response<BaseResponse<List<Request>>>>

    @GET("order/discharged")
    fun getFinishedRequests(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<Response<BaseResponse<List<Request>>>>

    @GET("task/all")
    fun getAllRequestTasks(@Query("order_id") requestId: String): Observable<Response<BaseResponse<RequestTasksResponse>>>

    @POST("order/accept")
    fun acceptRequest(@Body request: AcceptOrderRequest): Observable<Response<BaseResponse<ConfirmAcceptOrderResponse>>>

    @POST("task/create")
    fun createTask(@Body request: CreateTaskRequest): Observable<Response<BaseResponse<List<RequestTask>>>>

    @POST("order/home-coordinates")
    fun setHomePoint(@Body request: SetHomePointRequest): Observable<Response<BaseResponse<SetHomePointResponse>>>

    @POST("task/mark-remove")
    fun markTaskDelete(@Body request: ChangeTaskRequest): Observable<Response<BaseResponse<ChangeTaskResponse>>>
}