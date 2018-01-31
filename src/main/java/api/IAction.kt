package api

import java.util.Optional

interface IAction<out A, D> {
    val type: A
    val payload: Optional<D>
    val isError: Boolean
    val meta: Optional<Any>
}