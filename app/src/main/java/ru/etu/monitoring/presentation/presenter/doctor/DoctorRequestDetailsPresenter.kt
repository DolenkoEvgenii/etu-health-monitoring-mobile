package ru.etu.monitoring.presentation.presenter.doctor

import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.model.data_model.DoctorRequestDetailsModel
import ru.etu.monitoring.model.event.CreateTaskDataInputEvent
import ru.etu.monitoring.model.event.UpdateHomePointEvent
import ru.etu.monitoring.model.event.UpdateRequestsEvent
import ru.etu.monitoring.model.interactor.EventInteractor
import ru.etu.monitoring.model.network.doctor.DoctorRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.doctor.DoctorRequestDetailsView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class DoctorRequestDetailsPresenter(val request: Request) : BasePresenter<DoctorRequestDetailsView>() {
    private val doctorRepository: DoctorRepository by inject()
    private val router: Router by inject()

    private val model = DoctorRequestDetailsModel()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        listenForEvents()
        viewState.bindRequest(request)

        if (request.isOpen || request.isClosed) {
            loadTasks()
        }
    }

    fun onAcceptRequestClick() {
        acceptRequest()
    }

    fun onAddTaskClick() {
        router.navigateTo(Screens.CreateTaskFragmentScreen().apply { inNewActivity = true })
    }

    fun onDeleteTaskClick(task: RequestTask) {
        deleteTask(task)
    }

    fun onSetHomePointClick() {
        val lat = request.latitude
        val lng = request.longitude

        if (lat != null && lng != null) {
            router.navigateTo(Screens.PickHomePointActivityScreen(LatLng(lat.toDouble(), lng.toDouble())))
        } else {
            router.navigateTo(Screens.PickHomePointActivityScreen(null))
        }
    }

    private fun acceptRequest() {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            doctorRepository.acceptRequest(request.orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()
                    EventInteractor.publishEvent(UpdateRequestsEvent())
                    router.exit()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun loadTasks() {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            doctorRepository.loadAllTasks(request.orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model.activeTasks.clear()
                    model.activeTasks.addAll(it.active)

                    model.doneTasks.clear()
                    model.doneTasks.addAll(it.done)

                    model.removedTasks.clear()
                    model.removedTasks.addAll(it.removed)

                    viewState.showTasks(model.activeTasks, model.doneTasks, model.removedTasks)
                    viewState.closeLoadingDialog()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun createTask(data: CreateTaskDataInputEvent) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            doctorRepository.createTask(request.orderId, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model.addActiveTask(it)
                    viewState.showTasks(model.activeTasks, model.doneTasks, model.removedTasks)
                    viewState.closeLoadingDialog()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun setHomePoint(latLng: LatLng) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            doctorRepository.setHomePoint(request.orderId, latLng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    request.latitude = latLng.latitude.toFloat()
                    request.longitude = latLng.longitude.toFloat()
                    viewState.closeLoadingDialog()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun deleteTask(task: RequestTask) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            doctorRepository.deleteTask(task.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model.activeTasks.remove(task)
                    model.removedTasks.add(task)
                    viewState.showTasks(model.activeTasks, model.doneTasks, model.removedTasks)
                    viewState.closeLoadingDialog()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun listenForEvents() {
        unsubscribeOnDestroy(
            EventInteractor.getEventObservable<CreateTaskDataInputEvent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    createTask(it)
                }, {
                    it.printStackTrace()
                })
        )

        unsubscribeOnDestroy(
            EventInteractor.getEventObservable<UpdateHomePointEvent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setHomePoint(it.latLng)
                }, {
                    it.printStackTrace()
                })
        )
    }
}
