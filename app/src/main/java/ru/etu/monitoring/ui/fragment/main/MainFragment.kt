package ru.etu.monitoring.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import moxy.presenter.InjectPresenter
import ru.etu.monitoring.R
import ru.etu.monitoring.presentation.presenter.main.MainPresenter
import ru.etu.monitoring.presentation.view.main.MainView
import ru.etu.monitoring.ui.fragment.BaseMvpFragment

class MainFragment : BaseMvpFragment(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
