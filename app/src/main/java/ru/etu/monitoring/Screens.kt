package ru.etu.monitoring

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.etu.monitoring.model.data.Request
import ru.etu.monitoring.model.navigator.BaseAppScreen
import ru.etu.monitoring.ui.activity.MainActivity
import ru.etu.monitoring.ui.activity.auth.AuthActivity
import ru.etu.monitoring.ui.fragment.auth.ConfirmLoginFragment
import ru.etu.monitoring.ui.fragment.auth.LoginFragment
import ru.etu.monitoring.ui.fragment.auth.SignUpFragment
import ru.etu.monitoring.ui.fragment.doctor.DoctorMainFragment
import ru.etu.monitoring.ui.fragment.doctor.DoctorRequestDetailsFragment
import ru.etu.monitoring.ui.fragment.illness.CreateIllnessFragment
import ru.etu.monitoring.ui.fragment.main.MainFragment
import ru.etu.monitoring.ui.fragment.task.CreateTaskFragment
import java.io.Serializable

object Screens {
    class MainActivityScreen : BaseAppScreen() {
        override fun getActivityIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    class AuthActivityScreen : BaseAppScreen() {
        override fun getActivityIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }

    class LoginFragmentScreen : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return LoginFragment.newInstance()
        }
    }

    class ConfirmLoginFragmentScreen(val phone: String) : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return ConfirmLoginFragment.newInstance(phone)
        }
    }

    class SignUpFragmentScreen(val phone: String) : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return SignUpFragment.newInstance(phone)
        }
    }

    class MainFragmentScreen : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return MainFragment.newInstance()
        }
    }

    class DoctorMainFragmentScreen : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return DoctorMainFragment.newInstance()
        }
    }

    class CreateTaskFragmentScreen : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return CreateTaskFragment.newInstance()
        }
    }

    class DoctorRequestDetailsFragmentScreen(val request: Request) : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return DoctorRequestDetailsFragment.newInstance(request)
        }
    }

    class CreateIllnessFragmentScreen : BaseAppScreen() {
        override fun getFragment(): Fragment {
            return CreateIllnessFragment.newInstance()
        }
    }
}
