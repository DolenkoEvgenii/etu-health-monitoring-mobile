package ru.etu.monitoring.presentation.presenter.doctor

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.Screens
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.data_model.DoctorRequestsModel
import ru.etu.monitoring.model.network.doctor.DoctorRepository
import ru.etu.monitoring.model.network.user.UserRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.presenter.doctor.DoctorMainPresenter.RequestType.*
import ru.etu.monitoring.presentation.view.doctor.DoctorMainView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.terrakok.cicerone.Router

@InjectViewState
class DoctorMainPresenter : BasePresenter<DoctorMainView>() {
    private val userRepository: UserRepository by inject()
    private val doctorRepository: DoctorRepository by inject()
    private val router: Router by inject()

    private val model = DoctorRequestsModel()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showRequests(model.newRequests, NEW)
        viewState.showRequests(model.currentRequests, ACTIVE)
        viewState.showRequests(model.finishedRequests, FINISHED)
    }

    fun onLogoutClick() {
        userRepository.logout()
        router.newRootScreen(Screens.AuthActivityScreen())
    }

    fun onLoadMore(type: RequestType) {
        loadMoreRequests(type)
    }

    fun onRequestClick(request: Request) {

    }

    fun onRefresh(type: RequestType) {
        val wasEmpty = model.isEmpty(type)
        model.clear(type)
        viewState.clearRequests(type)

        if (wasEmpty) {
            onLoadMore(type)
        }
    }

    private fun loadMoreRequests(type: RequestType) {
        val offset = model.offset(type)
        if (offset > 0) {
            viewState.closeLoadingDialog()
            return
        }

        if (offset == 0) {
            viewState.showLoadingDialog()
        }

        val request = when (type) {
            NEW -> doctorRepository.getNewRequests(offset, PAGE_SIZE)
            ACTIVE -> doctorRepository.getActiveRequests(offset, PAGE_SIZE)
            FINISHED -> doctorRepository.getFinishedRequests(offset, PAGE_SIZE)
        }
        unsubscribeOnDestroy(
            request
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model.addAll(type, it)
                    viewState.addRequests(it, type)
                    viewState.closeLoadingDialog()
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }

    enum class RequestType {
        NEW, ACTIVE, FINISHED
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
