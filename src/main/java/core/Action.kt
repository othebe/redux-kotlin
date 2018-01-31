package core

import api.IAction
import java.util.Optional

class Action<T, D> constructor(override val type: T,
                                        override val payload: Optional<D>,
                                        override val isError: Boolean,
                                        override val meta: Optional<Any>) : IAction<T, D> {
    companion object {
        fun <T> from(type: T): IAction<T, Void> {
            return Action<T, Void>(type, Optional.empty(), false, Optional.empty())
        }

        fun <T, D> from(type: T, payload: D): IAction<T, D> {
            return Action(type, Optional.of(payload), false, Optional.empty())
        }

        fun <T, D> from(type: T, payload: D, meta: Any): IAction<T, D> {
            return Action(type, Optional.of(payload), false, Optional.of(meta))
        }

        fun <T> error(type: T): IAction<T, Void> {
            return Action<T, Void>(type, Optional.empty(), true, Optional.empty())
        }

        fun <T> error(type: T, payload: Any): IAction<T, Any> {
            return Action<T, Any>(type, Optional.of(payload), true, Optional.empty())
        }

        fun <T> error(type: T, payload: Any, meta: Any): IAction<T, Any> {
            return Action<T, Any>(type, Optional.of(payload), true, Optional.of(meta))
        }
    }
}