package ru.etu.monitoring.model.network.user

import io.reactivex.Observable
import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.BaseRepository

class UserRepository(val api: UserApi) : BaseRepository() {
    val isAuthorized: Boolean
        get() = userPreferences.isAuthorized

    fun getUserLocal(): Observable<User> {
        return userPreferences.getUserLocal()
    }
}
