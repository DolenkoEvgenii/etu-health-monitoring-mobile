package ru.etu.monitoring.presentation.presenter.illness

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import org.koin.core.component.inject
import ru.etu.monitoring.R
import ru.etu.monitoring.model.network.patient.PatientRepository
import ru.etu.monitoring.presentation.presenter.BasePresenter
import ru.etu.monitoring.presentation.view.illness.CreateIllnessView
import ru.etu.monitoring.utils.helpers.showErrorToast
import ru.etu.monitoring.utils.helpers.showSuccessToast
import ru.terrakok.cicerone.Router

@InjectViewState
class CreateIllnessPresenter : BasePresenter<CreateIllnessView>() {
    private val patientRepository: PatientRepository by inject()
    private val router: Router by inject()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

    fun onCreateIllnessClick(temperature: Float, symptoms: String) {
        createIllness(temperature, symptoms)
    }

    private fun createIllness(temperature: Float, symptoms: String) {
        viewState.showLoadingDialog()
        unsubscribeOnDestroy(
            patientRepository
                .createIllRequest(temperature, symptoms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.closeLoadingDialog()
                    router.exit()
                    showSuccessToast(R.string.data_successfully_sent)
                }, {
                    viewState.closeLoadingDialog()
                    showErrorToast(it.localizedMessage)
                })
        )
    }
}
