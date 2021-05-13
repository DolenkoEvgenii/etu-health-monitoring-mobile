package ru.etu.monitoring.model.data_model

import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.presentation.presenter.doctor.DoctorMainPresenter

class DoctorRequestsModel {
    val newRequests: MutableList<Request> = ArrayList()
    val currentRequests: MutableList<Request> = ArrayList()
    val finishedRequests: MutableList<Request> = ArrayList()

    fun isEmpty(type: DoctorMainPresenter.RequestType): Boolean {
        return when (type) {
            DoctorMainPresenter.RequestType.NEW -> newRequests.isEmpty()
            DoctorMainPresenter.RequestType.ACTIVE -> currentRequests.isEmpty()
            DoctorMainPresenter.RequestType.FINISHED -> finishedRequests.isEmpty()
        }
    }

    fun clear(type: DoctorMainPresenter.RequestType) {
        when (type) {
            DoctorMainPresenter.RequestType.NEW -> newRequests.clear()
            DoctorMainPresenter.RequestType.ACTIVE -> currentRequests.clear()
            DoctorMainPresenter.RequestType.FINISHED -> finishedRequests.clear()
        }
    }

    fun offset(type: DoctorMainPresenter.RequestType): Int {
        return when (type) {
            DoctorMainPresenter.RequestType.NEW -> newRequests.size
            DoctorMainPresenter.RequestType.ACTIVE -> currentRequests.size
            DoctorMainPresenter.RequestType.FINISHED -> finishedRequests.size
        }
    }

    fun addAll(type: DoctorMainPresenter.RequestType, requests: List<Request>) {
        when (type) {
            DoctorMainPresenter.RequestType.NEW -> newRequests.addAll(requests)
            DoctorMainPresenter.RequestType.ACTIVE -> currentRequests.addAll(requests)
            DoctorMainPresenter.RequestType.FINISHED -> finishedRequests.addAll(requests)
        }
    }
}