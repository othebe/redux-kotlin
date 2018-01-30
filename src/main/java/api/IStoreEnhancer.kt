package api

interface IStoreEnhancer<S> {
    fun <T: Enum<*>> next(getState: () -> S, dispatch: (IAction<T, Any>) -> Unit): (IAction<T, Any>) -> IAction<T, Any>
}