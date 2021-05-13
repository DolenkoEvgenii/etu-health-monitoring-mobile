package ru.etu.monitoring.ui.fragment.illness

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.appbar.AppBarLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_create_illness.*
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.presentation.presenter.illness.CreateIllnessPresenter
import ru.etu.monitoring.presentation.view.illness.CreateIllnessView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.applyPhoneInputMask
import ru.etu.monitoring.utils.helpers.click

class CreateIllnessFragment : BaseMvpFragment(), CreateIllnessView {
    @InjectPresenter
    lateinit var presenter: CreateIllnessPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_illness, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        listOf(etSymptoms, etTemperature).forEach {
            it.doAfterTextChanged {
                validateInput()
            }
        }
        btSend.isEnabled = false
        btSend.click {
            presenter.onCreateIllnessClick(
                etTemperature.text.toString().toFloatOrNull() ?: 0f,
                etSymptoms.text.toString()
            )
        }


        etTemperature.apply {
            val listener = MaskedTextChangedListener(
                "[00].[0]",
                true,
                this,
                null,
                null
            )

            addTextChangedListener(listener)
            onFocusChangeListener = listener
        }
    }

    private fun validateInput() {
        var isValid = true

        if (etSymptoms.text?.toString().orEmpty().isBlank()) {
            isValid = false
        }

        if (etTemperature.text?.toString()?.toFloatOrNull() == null) {
            isValid = false
        }

        btSend.isEnabled = isValid
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = provideSimpleToolbar(getString(R.string.create_illness), appBar)
        appBar.addView(toolbar)
        return toolbar
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance(): CreateIllnessFragment {
            return CreateIllnessFragment()
        }
    }
}
