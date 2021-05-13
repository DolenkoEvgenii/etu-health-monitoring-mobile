package ru.etu.monitoring.model.network.patient

import io.reactivex.Observable
import ru.etu.monitoring.model.network.BaseRepository
import ru.etu.monitoring.model.network.data.request.CreateIllnessRequest
import ru.etu.monitoring.model.network.data.response.auth.LoginResponse

class PatientRepository(private val api: PatientApi) : BaseRepository() {

    fun createIllRequest(temperature: Float, symptoms: String): Observable<LoginResponse> {
        return api.createIllRequest(CreateIllnessRequest(temperature, symptoms))
            .compose(handleErrors())
    }
}
