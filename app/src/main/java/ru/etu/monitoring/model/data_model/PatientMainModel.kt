package ru.etu.monitoring.model.data_model

import ru.etu.monitoring.model.data.RequestTask

class PatientMainModel {
    val activeTasks: MutableList<RequestTask> = ArrayList()
    val doneTasks: MutableList<RequestTask> = ArrayList()
}