package ru.etu.monitoring.model.network.patient

import io.reactivex.Observable
import ru.etu.monitoring.model.network.BaseRepository
import ru.etu.monitoring.model.network.data.request.ChangeTaskRequest
import ru.etu.monitoring.model.network.data.request.CreateIllnessRequest
import ru.etu.monitoring.model.network.data.request.SendMyGeoRequest
import ru.etu.monitoring.model.network.data.response.auth.LoginResponse
import ru.etu.monitoring.model.network.data.response.request.SendMyGeoResponse
import ru.etu.monitoring.model.network.data.response.task.ChangeTaskResponse
import ru.etu.monitoring.model.network.data.response.task.RequestTasksResponse

class PatientRepository(private val api: PatientApi) : BaseRepository() {

    fun createIllRequest(temperature: Float, symptoms: String): Observable<LoginResponse> {
        return api.createIllRequest(CreateIllnessRequest(temperature, symptoms))
            .compose(handleErrors())
    }

    fun markTaskAsDone(taskId: String): Observable<ChangeTaskResponse> {
        return api.markTaskDone(ChangeTaskRequest(taskId))
            .compose(handleErrors())
    }

    fun loadMyTasks(requestId: String): Observable<RequestTasksResponse> {
        return api.getAllRequestTasks(requestId)
            .compose(handleErrors())
    }

    fun sendMyGeo(latitude: Double, longitude: Double): Observable<SendMyGeoResponse> {
        return api.sendMyGeo(SendMyGeoRequest(latitude, longitude))
            .compose(handleErrors())
    }
}
