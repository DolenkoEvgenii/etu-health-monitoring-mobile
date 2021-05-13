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
import kotlinx.android.synthetic.main.toolbar_logout.view.*
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

        btImIll.click { presenter.onImIllClick() }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
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
                vTreatments.gone()
                vDoctorCard.gone()
                tvNoDoctorInfo.visible()
            } else {
                tvNoDoctorInfo.gone()
                vDoctorCard.visible()
                vTreatments.visible()

                tvDoctorInfo.text = profile.doctor.fullName
                tvDoctorPhone.text = profile.doctor.phone

                btCall.click {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + profile.doctor.phone)))
                }
            }
        } else {
            btImIll.visible()
            tvNoDoctorInfo.gone()
            vDoctorCard.gone()
        }
    }

    override fun closeLoadingDialog() {
        vLoading.gone()
    }

    override fun showLoadingDialog() {
        vLoading.visible()
    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = inflateToolbar(R.layout.toolbar_logout, appBar)
        toolbar.tvTitle.text = getString(R.string.info)
        toolbar.btLogout.click {
            presenter.onLogoutClick()
        }
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
