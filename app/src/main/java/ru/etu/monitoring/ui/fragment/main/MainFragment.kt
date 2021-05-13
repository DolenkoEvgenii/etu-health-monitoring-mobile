package ru.etu.monitoring.ui.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.presentation.presenter.main.MainPresenter
import ru.etu.monitoring.presentation.view.main.MainView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click
import ru.etu.monitoring.utils.helpers.gone
import ru.etu.monitoring.utils.helpers.visible

class MainFragment : BaseMvpFragment(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun bindProfile(profile: User) {
        tvName.text = profile.firstName
        tvLastName.text = profile.lastName
        tvMiddleName.text = profile.middleName
        tvBirthDay.text = profile.birthdayStr
        tvIllInfo.text = if (profile.isIll) getString(R.string.you_ill) else getString(R.string.you_not_ill)

        if (profile.isIll) {
            btImIll.gone()

            if (profile.doctor == null) {
                vDoctorCard.gone()
                tvNoDoctorInfo.visible()

                tvDoctorInfo.text = profile.doctor?.fullName.orEmpty()
                tvDoctorPhone.text = profile.doctor?.phone.orEmpty()

                btCall.click {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + profile.doctor?.phone.orEmpty())))
                }
            } else {
                tvNoDoctorInfo.gone()
                vDoctorCard.visible()
            }
        } else {
            btImIll.visible()
            tvNoDoctorInfo.gone()
            vDoctorCard.gone()
        }
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = provideSimpleToolbar(getString(R.string.info), appBar)
        appBar.addView(toolbar)
        return toolbar
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
