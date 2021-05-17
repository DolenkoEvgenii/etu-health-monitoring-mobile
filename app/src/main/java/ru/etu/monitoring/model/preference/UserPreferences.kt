package ru.etu.monitoring.model.preference

import android.content.Context
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import io.reactivex.Observable
import ru.etu.monitoring.model.data.User

class UserPreferences(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val rxPreferences = RxSharedPreferences.create(preferences)

    val isAuthorized: Boolean
        get() = rxPreferences.getString(TOKEN_ARG).get().isNotBlank()

    var authToken: String
        get() = rxPreferences.getString(TOKEN_ARG).get()
        set(value) {
            preferences.edit().putString(TOKEN_ARG, value).apply()
        }

    var isDoctor: Boolean
        get() = rxPreferences.getBoolean(IS_DOCTOR_ARG).get()
        set(value) {
            preferences.edit().putBoolean(IS_DOCTOR_ARG, value).apply()
        }

    fun getUserLocal(): Observable<User> {
        return rxPreferences.getString(USER_ARG)
            .asObservable()
            .map { userJson ->
                if (userJson.isEmpty()) throw Exception("no saved user")
                return@map Gson().fromJson(userJson, User::class.java)
            }
            .take(1)
    }

    fun getUserLocalBlocking(): User {
        return getUserLocal().blockingFirst()
    }

    fun saveUser(user: User) {
        val userJson = Gson().toJson(user)
        preferences.edit().putString(USER_ARG, userJson).apply()
    }


    fun clearUserData() {
        rxPreferences.clear()
    }

    companion object {
        const val USER_ARG = "user_arg"
        const val TOKEN_ARG = "token_arg"
        const val IS_DOCTOR_ARG = "is_doctor_arg"
    }
}