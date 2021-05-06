package ru.etu.monitoring.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_confirm_login.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.presentation.presenter.auth.ConfirmLoginPresenter
import ru.etu.monitoring.presentation.view.auth.ConfirmLoginView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click

class ConfirmLoginFragment : BaseMvpFragment(), ConfirmLoginView {
    @InjectPresenter
    lateinit var presenter: ConfirmLoginPresenter

    @ProvidePresenter
    fun providePresenter(): ConfirmLoginPresenter {
        return ConfirmLoginPresenter(arguments?.getString(ARG_PHONE).orEmpty())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etCode.doAfterTextChanged {
            btConfirm.isEnabled = it?.toString().orEmpty().length == CODE_LENGTH
        }
        btConfirm.isEnabled = false

        btConfirm.click { presenter.onConfirmBtnClicked(etCode.text.toString()) }
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = provideSimpleToolbar(getString(R.string.entrance), appBar)
        appBar.addView(toolbar)
        return toolbar
    }

    companion object {
        const val CODE_LENGTH = 4

        const val TAG = "ConfirmLoginFragment"
        private const val ARG_PHONE = "ConfirmLoginFragment"

        fun newInstance(phone: String): ConfirmLoginFragment {
            return ConfirmLoginFragment().apply {
                arguments = bundleOf(ARG_PHONE to phone)
            }
        }
    }
}
