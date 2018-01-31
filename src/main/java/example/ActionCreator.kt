package example

import api.IAction
import core.Action

import example.ActionType.*

typealias AddAction = IAction<ActionType, Int>
typealias SubtractAction = IAction<ActionType, Int>
typealias ResetAction = IAction<ActionType, Void>
typealias AsyncAddAction = IAction<ActionType, Int>

object ActionCreator {
    fun createAddAction(valueToAdd: Int): AddAction = Action.from(ADD, valueToAdd)

    fun createSubtractAction(valueToSubtract: Int): SubtractAction = Action.from(SUBTRACT, valueToSubtract)

    fun createResetAction(): ResetAction = Action.from(RESET)

    fun createAsyncAddAction(valueToAdd: Int): AsyncAddAction = Action.from(ASYNC_ADD, valueToAdd)
}