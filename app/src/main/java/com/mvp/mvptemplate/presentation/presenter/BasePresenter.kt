package com.mvp.mvptemplate.presentation.presenter

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView
import java.util.concurrent.TimeUnit

open class BasePresenter<View : MvpView> : MvpPresenter<View>() {
    private var compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        compositeDisposable.add(Completable.timer(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe { onFirstViewAttachDelayed() })
    }

    open fun onFirstViewAttachDelayed() {

    }

    protected fun unsubscribeOnDestroy(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun unsubscribeAll() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        compositeDisposable = CompositeDisposable()
    }

    override fun onDestroy() {
        unsubscribeAll()
        super.onDestroy()
    }
}