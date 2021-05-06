package com.mvp.mvptemplate.model.network.user

import com.mvp.mvptemplate.model.data.User
import com.mvp.mvptemplate.model.network.BaseRepository
import io.reactivex.Observable

class UserRepository(val api: UserApi) : BaseRepository() {
    val isAuthorized: Boolean
        get() = userPreferences.isAuthorized

    fun getUserLocal(): Observable<User> {
        return userPreferences.getUserLocal()
    }
}
