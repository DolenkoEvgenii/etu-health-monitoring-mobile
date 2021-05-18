package ru.etu.monitoring.model.network.doctor

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.model.event.CreateTaskDataInputEvent
import ru.etu.monitoring.model.network.BaseRepository
import ru.etu.monitoring.model.network.data.request.AcceptOrderRequest
import ru.etu.monitoring.model.network.data.request.ChangeTaskRequest
import ru.etu.monitoring.model.network.data.request.CreateTaskRequest
import ru.etu.monitoring.model.network.data.request.SetHomePointRequest
import ru.etu.monitoring.model.network.data.response.request.ConfirmAcceptOrderResponse
import ru.etu.monitoring.model.network.data.response.request.SetHomePointResponse
import ru.etu.monitoring.model.network.data.response.task.ChangeTaskResponse
import ru.etu.monitoring.model.network.data.response.task.RequestTasksResponse

class DoctorRepository(private val api: DoctorApi) : BaseRepository() {

    fun getNewRequests(offset: Int, limit: Int): Observable<List<Request>> {
        return api.getNewRequests(offset, limit)
            .compose(handleErrors())
    }

    fun getActiveRequests(offset: Int, limit: Int): Observable<List<Request>> {
        return api.getActiveRequests(offset, limit)
            .compose(handleErrors())
    }

    fun getFinishedRequests(offset: Int, limit: Int): Observable<List<Request>> {
        return api.getFinishedRequests(offset, limit)
            .compose(handleErrors())
    }

    fun loadAllTasks(requestId: String): Observable<RequestTasksResponse> {
        return api.getAllRequestTasks(requestId)
            .compose(handleErrors())
    }

    fun acceptRequest(requestId: String): Observable<ConfirmAcceptOrderResponse> {
        return api.acceptRequest(AcceptOrderRequest(requestId))
            .compose(handleErrors())
    }

    fun createTask(requestId: String, data: CreateTaskDataInputEvent): Observable<List<RequestTask>> {
        return api.createTask(CreateTaskRequest(requestId, data.title, data.dateTo, data.perDay))
            .compose(handleErrors())
    }

    fun setHomePoint(requestId: String, latLng: LatLng): Observable<SetHomePointResponse> {
        return api.setHomePoint(SetHomePointRequest(requestId, latLng.latitude, latLng.longitude))
            .compose(handleErrors())
    }

    fun deleteTask(taskId: String): Observable<ChangeTaskResponse> {
        return api.markTaskDelete(ChangeTaskRequest(taskId))
            .compose(handleErrors())
    }
}
