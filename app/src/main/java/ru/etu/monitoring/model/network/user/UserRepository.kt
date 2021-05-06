package ru.etu.monitoring.model.network.user

import ru.etu.monitoring.model.data.User
import ru.etu.monitoring.model.network.BaseRepository
import io.reactivex.Observable

class UserRepository(val api: UserApi) : BaseRepository() {
    val isAuthorized: Boolean
        get() = userPreferences.isAuthorized

    fun getUserLocal(): Observable<User> {
        return userPreferences.getUserLocal()
    }
}
