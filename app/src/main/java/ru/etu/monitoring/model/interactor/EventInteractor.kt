package ru.etu.monitoring.model.interactor

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object EventInteractor {
    val source: PublishSubject<Any> = PublishSubject.create()

    inline fun <reified T> getEventObservable(): Observable<T> {
        return source
                .filter { it is T }
                .map { it as T }
    }

    fun publishEvent(event: Any) {
        source.onNext(event)
    }
}