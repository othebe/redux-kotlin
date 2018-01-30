package api

interface IReducer<S> {
    fun <T: Enum<*>> reduce(state: S, action: IAction<T, S>)
}