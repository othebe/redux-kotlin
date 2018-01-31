package core.enhancers

import api.Dispatcher
import api.IStore
import api.IStoreEnhancer
import rx.Observable

interface IMiddleware<in S, A> : (S, Dispatcher<A>, Dispatcher<A>) -> Dispatcher<A>

fun <S, A> applyMiddleware(vararg middlewares: IMiddleware<S, A>) : IStoreEnhancer<S, A> {
    return object : IStoreEnhancer<S, A> {
        override fun invoke(store: IStore<S, A>): IStore<S, A> {
            return object : IStore<S, A> {
                override val state: S = store.state

                override val observable: Observable<S> = store.observable

                override val dispatch: Dispatcher<A> = { action ->
                    val state = store.state
                    val dispatch = store.dispatch

                    val chained = middlewares.foldRight(dispatch) { middleware, next -> middleware(state, dispatch, next) }

                    middlewares.foldRight(dispatch) { middleware, next -> middleware(state, chained, next) }
                            .invoke(action)
                }
            }
        }
    }
}
