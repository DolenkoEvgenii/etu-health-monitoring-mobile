package ru.etu.monitoring.presentation.presenter.task

import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.model.event.CreateTaskDataInputEvent
import ru.etu.monitoring.model.interactor.EventInteractor
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.task.CreateTaskView
import ru.terrakok.cicerone.Router

@InjectViewState
class CreateTaskPresenter : BasePresenter<CreateTaskView>() {
    private val router: Router by inject()

    fun onCreateTaskClick(title: String, dateTo: String, perDay: Int) {
        EventInteractor.publishEvent(CreateTaskDataInputEvent(title, dateTo, perDay))
        router.exit()
    }
}
