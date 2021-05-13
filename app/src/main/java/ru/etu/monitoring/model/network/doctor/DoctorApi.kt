package ru.etu.monitoring.model.network.doctor


import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.network.data.response.BaseResponse

interface DoctorApi {
    @GET("order/new")
    fun getNewRequests(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<Response<BaseResponse<List<Request>>>>

    @GET("order/current")
    fun getActiveRequests(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<Response<BaseResponse<List<Request>>>>

    @GET("order/discharged")
    fun getFinishedRequests(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<Response<BaseResponse<List<Request>>>>
}