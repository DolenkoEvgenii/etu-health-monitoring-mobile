package ru.etu.monitoring.ui.fragment.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_sign_up.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.presentation.presenter.auth.SignUpPresenter
import ru.etu.monitoring.presentation.view.auth.SignUpView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click
import java.text.SimpleDateFormat
import java.util.*

class SignUpFragment : BaseMvpFragment(), SignUpView {
    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    @ProvidePresenter
    fun providePresenter(): SignUpPresenter {
        return SignUpPresenter(arguments?.getString(ARG_PHONE).orEmpty())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listOf(etCode, etLastName, etFirstName, etMiddleName, etBirthDay).forEach {
            it.doAfterTextChanged {
                validateInput()
            }
        }
        btSignUp.isEnabled = false
        btSignUp.click {
            presenter.onSignUpClick(
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etMiddleName.text.toString(),
                etBirthDay.text.toString(),
                etCode.text.toString()
            )
        }

        btSelectBirthDate.click {
            showPickBirthdayDialog()
        }
    }

    private fun showPickBirthdayDialog() {
        val sdf = SimpleDateFormat(BIRTHDAY_FORMAT, Locale.getDefault())

        val currentInput = etBirthDay.text?.toString().orEmpty()
        val currentDate = if (currentInput.isBlank()) Date() else sdf.parse(currentInput)
        val cal = Calendar.getInstance().apply { time = currentDate }

        val pickListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val dateStr = "%d-%02d-%02d".format(year, month + 1, dayOfMonth)
            etBirthDay.setText(dateStr)
        }

        DatePickerDialog(
            requireContext(),
            pickListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateInput() {
        var isValid = true

        if (etCode.textLength() != CODE_LENGTH) {
            isValid = false
        }
        if (etFirstName.textLength() < 2) {
            isValid = false
        }
        if (etLastName.textLength() < 2) {
            isValid = false
        }
        if (etBirthDay.textLength() < 1) {
            isValid = false
        }

        btSignUp.isEnabled = isValid
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = provideSimpleToolbar(getString(R.string.entrance), appBar)
        appBar.addView(toolbar)
        return toolbar
    }

    private fun EditText.textLength(): Int {
        return text?.toString().orEmpty().length
    }

    companion object {
        const val CODE_LENGTH = 4

        const val TAG = "ConfirmLoginFragment"
        private const val ARG_PHONE = "ConfirmLoginFragment"

        private const val BIRTHDAY_FORMAT = "yyyy-MM-dd"

        fun newInstance(phone: String): SignUpFragment {
            return SignUpFragment().apply {
                arguments = bundleOf(ARG_PHONE to phone)
            }
        }
    }
}
