package core

import api.Dispatcher
import api.IReducer
import api.IStore
import api.IStoreEnhancer
import rx.Emitter
import rx.Observable
import rx.functions.Func1

class Store<S, A> private constructor(override var state: S,
                                      private val reducer: IReducer<S, A>) : IStore<S, A> {

    private var emitter: Func1<S, Void>? = null

    override val dispatch: Dispatcher<A> = { action ->
        state = reducer(state, action)
        emitter?.call(state)
        action
    }

    override val observable: Observable<S> = Observable.create<S>({ emitter ->
        this.emitter = Func1 { state ->
            emitter.onNext(state)
            null
        }
    }, Emitter.BackpressureMode.BUFFER).share()

    companion object {
        fun <S, A> create(reducer: IReducer<S, A>, initialState: S, vararg enhancer: IStoreEnhancer<S, A>): IStore<S, A> {
            val baseStore: IStore<S, A> = Store(initialState, reducer)

            if (!enhancer.isEmpty()) {
                return enhancer.fold(baseStore) { enhanced, enhancer -> enhancer(enhanced) }
            } else {
                return baseStore
            }
        }
    }
}