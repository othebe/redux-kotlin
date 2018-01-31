package example

import api.Dispatcher
import api.IAction
import api.IReducer
import api.IStore
import core.Store
import core.enhancers.IMiddleware
import core.enhancers.applyMiddleware

import example.ActionType.*
import java.util.Timer
import kotlin.concurrent.timerTask

object App {
    @JvmStatic
    fun main(vararg args: String) {
        // Handle available Actions.
        val reducer: IReducer<Int, ActionType> = object : IReducer<Int, ActionType> {
            override fun invoke(state: Int, action: IAction<ActionType, *>): Int {
                when (action.type) {
                    ADD -> return state + (action as AddAction).payload.get()
                    SUBTRACT -> return state - (action as SubtractAction).payload.get()
                    RESET -> return 0
                    ASYNC_ADD -> return state
                }
            }
        }

        // Logs the Action type.
        val loggerMiddleware = object : IMiddleware<Int, ActionType> {
            override fun invoke(state: Int, dispatcher: Dispatcher<ActionType>, next: Dispatcher<ActionType>): Dispatcher<ActionType> {
                return { action ->
                    System.out.println(String.format("%s - %s", "Logger Middleware", action.type.name));
                    next(action)
                }
            }
        }

        // Dispatches new Actions to the store.
        val thunkMiddleware = object : IMiddleware<Int, ActionType> {
            override fun invoke(state: Int, dispatcher: Dispatcher<ActionType>, next: Dispatcher<ActionType>): Dispatcher<ActionType> {
                return { action ->
                    if (action.type == ASYNC_ADD) {
                        Timer().schedule(timerTask {
                            val asyncAddAction: AsyncAddAction = action as AsyncAddAction
                            dispatcher(ActionCreator.createAddAction(asyncAddAction.payload.get()))
                        }, 2000)
                    }
                    next(action)
                }
            }
        }


        /***********************
         * BEGIN
         **********************/
        val store: IStore<Int, ActionType> = Store.create(reducer, 0, applyMiddleware(
                loggerMiddleware,
                thunkMiddleware))


        store.observable.subscribe({ state ->
            System.out.println(state)
        })

        store.dispatch(ActionCreator.createAddAction(4))
        store.dispatch(ActionCreator.createSubtractAction(2))
        store.dispatch(ActionCreator.createAsyncAddAction(10))
        store.dispatch(ActionCreator.createResetAction())
    }
}