package api

import rx.Observable

typealias Dispatcher<A> = (action: IAction<A, *>) -> IAction<A, *>

interface IStore<S, A> {
    val state: S
    val dispatch: Dispatcher<A>
    val observable: Observable<S>
}