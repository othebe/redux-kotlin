package api

import java.util.Optional

class Action<T: Enum<*>, D> constructor(override val type: T,
                                        override val payload: Optional<D>,
                                        override val isError: Boolean,
                                        override val meta: Optional<Any>) : IAction<T, D> {
    companion object {
        fun <T : Enum<*>> from(type: T): Action<T, Void> {
            return Action<T, Void>(type, Optional.empty(), false, Optional.empty())
        }

        fun <T : Enum<*>, D> from(type: T, payload: D): Action<T, D> {
            return Action(type, Optional.of(payload), false, Optional.empty())
        }

        fun <T : Enum<*>, D> from(type: T, payload: D, meta: Any): Action<T, D> {
            return Action(type, Optional.of(payload), false, Optional.of(meta))
        }

        fun <T : Enum<*>> error(type: T): Action<T, Void> {
            return Action<T, Void>(type, Optional.empty(), true, Optional.empty())
        }

        fun <T : Enum<*>> error(type: T, payload: Any): Action<T, Any> {
            return Action<T, Any>(type, Optional.of(payload), true, Optional.empty())
        }

        fun <T : Enum<*>> error(type: T, payload: Any, meta: Any): Action<T, Any> {
            return Action<T, Any>(type, Optional.of(payload), true, Optional.of(meta))
        }
    }
}