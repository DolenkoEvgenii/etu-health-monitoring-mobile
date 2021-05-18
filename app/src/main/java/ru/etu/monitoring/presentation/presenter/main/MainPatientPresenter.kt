package ru.etu.monitoring.presentation.presenter.main

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.data.RequestTask
import ru.etu.monitoring.model.data_model.PatientMainModel
import ru.etu.monitoring.model.network.patient.PatientRepository
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.model.preference.UserPreferences
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.main.MainPatientView
import ru.etu.monitoring.service.LocationTrackingService
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPatientPresenter : BasePresenter<MainPatientView>() {
    private val userRepository: UserRepository by inject()
    private val userPreferences: UserPreferences by inject()
    private val patientRepository: PatientRepository by inject()
    private val router: Router by inject()
    private val context: Context by inject()

    private val model = PatientMainModel()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        sendFirebaseTokenToServer()

        if (isTrackingServiceRunning()) {
            viewState.bindService()
        }
    }

    fun onImIllClick() {
        router.navigateTo(Screens.CreateIllnessFragmentScreen())
    }

    fun onResume() {
        loadProfile()
    }

    fun onLogoutClick() {
        userRepository.logout()
        router.newRootScreen(Screens.AuthActivityScreen())
    }

    fun onMarkTaskDoneClick(task: RequestTask) {
        markTaskDone(task)
    }

    private fun markTaskDone(task: RequestTask) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            patientRepository.markTaskAsDone(task.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model.activeTasks.remove(task)
                    model.doneTasks.add(task)
                    viewState.showTasks(model.activeTasks, model.doneTasks)
                    viewState.closeLoadingDialog()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun loadProfile() {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            userRepository
                .getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isIll) {
                        loadTasks(it.orderId ?: 0)
                        if (!isTrackingServiceRunning()) {
                            viewState.startTrackingService()
                        }
                    } else {
                        viewState.stopTrackingService()
                        viewState.closeLoadingDialog()
                    }
                    viewState.bindProfile(it)
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun loadTasks(requestId: Int) {
        unsubscribeOnDestroy(
            patientRepository
                .loadMyTasks(requestId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()

                    model.activeTasks.clear()
                    model.activeTasks.addAll(it.active)

                    model.doneTasks.clear()
                    model.doneTasks.addAll(it.done)

                    viewState.showTasks(model.activeTasks, model.doneTasks)
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    private fun sendFirebaseTokenToServer() {
        unsubscribeOnDestroy(
            userRepository.updateFirebaseToken(userPreferences.firebaseToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun isTrackingServiceRunning(): Boolean {
        return LocationTrackingService.isServiceRunning(context)
    }
}
