package ru.etu.monitoring.model.network

import android.annotation.SuppressLint
import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response
import ru.etu.monitoring.R
import ru.etu.monitoring.model.exception.APIException
import ru.etu.monitoring.model.network.data.response.BaseResponse
import ru.etu.monitoring.model.network.user.UserApi
import ru.etu.monitoring.model.preference.UserPreferences
import ru.terrakok.cicerone.Router
import java.net.UnknownHostException

open class BaseRepository : KoinComponent {
    protected val context: Context by inject()
    protected val userApi: UserApi by inject()
    protected val userPreferences: UserPreferences by inject()
    protected val router: Router by inject()

    private fun convertExceptionToText(exc: Throwable): String {
        return when (exc) {
            is UnknownHostException -> context.getString(R.string.no_internet_connection)
            is InterruptedException -> context.getString(R.string.server_error)
            is APIException -> exc.localizedMessage
            else -> context.getString(R.string.server_timeout_error)
        }
    }

    @SuppressLint("CheckResult")
    fun <T> handleErrors(): ObservableTransformer<Response<BaseResponse<T>>, T> {
        return ObservableTransformer { observable ->
            observable
                .map { response ->
                    if (response.isSuccessful) {
                        return@map response.body()
                    } else {
                        throw getErrorInstance(response)
                    }
                }
                .map {
                    if (it.status) {
                        return@map it.data
                    } else {
                        throw APIException(it.errorMessage)
                    }
                }
                .onErrorResumeNext(Function { error ->
                    error.printStackTrace()
                    when (error) {
                        is APIException -> return@Function Observable.error(error)
                        else -> return@Function Observable.error(APIException(convertExceptionToText(error)))
                    }
                })
        }
    }

    // OAuth2

    /*@SuppressLint("CheckResult")
    fun <T> handleErrors(needRetry: Boolean = true): ObservableTransformer<Response<T>, T> {
        return ObservableTransformer { observable ->
            observable
                    .onErrorResumeNext(Function { error ->
                        error.printStackTrace()
                        return@Function Observable.error(APIException(convertExceptionToText(error)))
                    })
                    .map { response ->
                        if (response.isSuccessful) {
                            return@map response.body()
                        } else {
                            throw getErrorInstance(response)
                        }
                    }
                    .retry { times, exc ->
                        if (!needRetry) {
                            return@retry false
                        }

                        if (exc is APIException && exc.httpCode == 401) {
                            if (times == 1) {
                                refreshToken().map { 1 }.onErrorResumeNext(Observable.just(1)).blockingFirst()
                                return@retry true
                            } else {
                                Completable.complete()
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe {
                                            utilityWrapper.userPreferences.clearUserData()
                                            utilityWrapper.router.newRootScreen(Screens.AuthActivityScreen())
                                        }
                                return@retry false
                            }
                        } else {
                            return@retry false
                        }
                    }
        }
    }

    fun refreshToken(): Observable<User> {
        val username = utilityWrapper.userPreferences.getUserLocal().blockingFirst().username
        val refreshToken = utilityWrapper.userPreferences.refreshToken
        val request = utilityWrapper.userApi.refresh(RefreshRequest(username, refreshToken))

        return request
                .compose(handleErrors(false))
                .doOnNext {
                    utilityWrapper.userPreferences.saveUser(it)
                }
    }*/

    private fun <T> getErrorInstance(response: Response<T>): APIException {
        val httpCode = response.code()
        val body = response.errorBody()?.string()

        val message = body?.let {
            try {
                val jsonError = JSONObject(it)
                return@let jsonError.optString("message") ?: ""
            } catch (exc: Exception) {
                return@let body
            }
        } ?: ""

        return APIException(message, httpCode, body)
    }
}