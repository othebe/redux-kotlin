package api

interface IStore<S> {
    fun getState(): S
    fun <T: Enum<*>> dispatch(action: IAction<T, *>)
}