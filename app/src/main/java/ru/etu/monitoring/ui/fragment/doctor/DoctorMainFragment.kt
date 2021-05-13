package ru.etu.monitoring.ui.fragment.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.toolbar_logout.view.*
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.presentation.presenter.doctor.DoctorMainPresenter
import ru.etu.monitoring.presentation.view.doctor.DoctorMainView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment
import ru.etu.monitoring.utils.helpers.click

class DoctorMainFragment : BaseMvpFragment(), DoctorMainView {
    @InjectPresenter
    lateinit var presenter: DoctorMainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doctor_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun bindProfile(profile: User) {

    }

    override fun setupToolbar(appBar: AppBarLayout): Toolbar {
        val toolbar = inflateToolbar(R.layout.toolbar_logout, appBar)
        toolbar.tvTitle.text = getString(R.string.requests_list)
        toolbar.btLogout.click {
            presenter.onLogoutClick()
        }
        appBar.addView(toolbar)

        val tabLayout = TabLayout(requireContext()).apply {
            addTab(newTab().setText(context.getString(R.string.new_)))
            addTab(newTab().setText(context.getString(R.string.current)))
            addTab(newTab().setText(context.getString(R.string.closed)))
        }
        appBar.addView(tabLayout)

        return toolbar
    }

    companion object {
        const val TAG = "DoctorMainFragment"
        fun newInstance(): DoctorMainFragment {
            return DoctorMainFragment()
        }
    }
}
