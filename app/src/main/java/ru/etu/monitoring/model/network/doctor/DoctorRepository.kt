package ru.etu.monitoring.model.network.doctor

import io.reactivex.Observable
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.network.BaseRepository

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
}
