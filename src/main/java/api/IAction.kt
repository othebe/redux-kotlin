package api

import java.util.Optional

interface IAction<T, D> {
    val type: T
    val payload: Optional<D>
    val isError: Boolean
    val meta: Optional<Any>
}