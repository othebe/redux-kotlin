import api.Action
import api.IAction
import api.IStoreEnhancer
import java.util.Optional

enum class TestType {
    ANDROID,
    iOS
}

class Main {
    val enhancer = object : IStoreEnhancer<Int> {
//        override fun <T : Enum<*>> next(getState: () -> Int, dispatch: IAction<T, Any>): (() -> Int, IAction<T, Any>) -> IAction<T, Any> {
//            val getStateUpdated: () -> Int = { getState() }
//            val dispatchUpdated:
//        }

//        override fun <T : Enum<*>> next(getState: () -> Int, dispatch: (IAction<T, Any>) -> Unit): (() -> Int, (IAction<T, Any>) -> Unit) -> IAction<T, Any> {
//            val getStateUpdated: () -> Int = { getState() }
//            val dispatchUpdated: (IAction<T, Any>) -> Unit = { action -> Unit }
//
//            return { getStateUpdated, dispatchUpdated -> }
//        }

        fun <T, A: IAction<T, *>> next77(getState: () -> Int, dispatch: (a: T) -> Unit): (IAction<T, Any>) -> IAction<T, Any> {
            val state = getState()

            val action = Action.from(TestType.ANDROID, 123)
dispatch.invoke(action as T)
            dispatch(action)
 
            return { action -> action }
        }
    }

    fun test1() {
        test<Int> { t -> t + 1 }
    }

    fun <T> test(fn: (T) -> Int) {
        fn.invoke(3 as T)

    }
}