package ru.etu.monitoring.model.data_model

import ru.etu.monitoring.model.data.RequestTask

class DoctorRequestDetailsModel {
    val activeTasks: MutableList<RequestTask> = ArrayList()
    val doneTasks: MutableList<RequestTask> = ArrayList()
    val removedTasks: MutableList<RequestTask> = ArrayList()

    fun addActiveTask(tasks: List<RequestTask>) {
        activeTasks.addAll(tasks)
    }
}