# redux-kotlin
Redux pattern realized in Kotlin using RxJava for subscriptions.

# Overview
To be added - See [https://redux.js.org/](https://redux.js.org/) for comprehensive documentation on Redux.

## Action
An action is modeled after a [Flux Standard Action](https://github.com/acdlite/flux-standard-action) and have a signature of ``IAction<out A, D>``, where ``A`` represents the action type such as an Enum, and ``D`` represents the payload data type.

## Reducer
A reducer calculates a state as a function of a state and action. Thus, the signature of a reducer is defined as ``IReducer<S, in A> : (S, IAction<A, *>) -> S``, where ``S`` represents the state type, and ``A`` represents the action type.

## Store
A ``Store`` provides its current state, a ``Dispatcher``, and in this implementation an RxJava Observable for subscribing to store changes. The signature is defined as ``IStore<S, A>`` where ``S`` represents the state type held by the store, and ``A`` represents the action type handled by the store. The ``Dispatcher`` is a function to dispatch an action to the store, and has a signature of ``Dispatcher<A> = (IAction<A, *>) -> IAction<A, *>``.

## Store Enhancers
A store enhancer provides the ability to enhance the capabilities of the store around its state, dispatch and observable by returning an enhanced store as a function of an existing store. As such, its signature is ``IStoreEnhancer<S, A> : (IStore<S, A> -> IStore<S, A>)``

### Middleware
See detailed documentation about middleware at [https://redux.js.org/docs/api/applyMiddleware.html](https://redux.js.org/docs/api/applyMiddleware.html). In this implementation, every middleware returns a ``Dispatcher`` as a function of the store's state, the store (root) dispatcher and the next middleware's dispatcher. Thus, the signature used is ``IMiddleWare<in S, A> : (S, Dispatcher<A>, Dispatcher<A>) -> Dispatcher<A>`` In keeping with the Facebook implementation, this version calls the entire dispatch chain when the root dispatcher is called, which is useful for scenarios such as Async actions.

## StoreCreator
A StoreCreator is used to create a store and apply enhancers. Middleware can be applied using the `applyMiddleware(Array<IMiddleware<S, A>)`` enhancer.

# Example
See included examples for more.

## Define action types.
This defines the action type our store will handle. Lets define two actions to add values: one synchronously, and one asynchronously.

    enum class ActionType {
      ADD,
      ASYNC_ADD
    }

## Action creators.
For ease of use, lets delegate the logic of creating actions into an ActionCreator object.

    typealias AddAction = IAction<ActionType, Int>
    typealias AsyncAddAction = IAction<ActionType, Int>
    
    object ActionCreator {
      fun createAddAction(valueToAdd: Int): AddAction = Action.from(ADD, valueToAdd)
      fun createAsyncAddAction(valueToAdd: Int): AsyncAddAction = Action.from(ASYNC_ADD, valueToAdd)
    }

## Reducer.
Lets write a reducer to handle all available actions.

    val reducer: IReducer<Int, ActionType> = object : IReducer<Int, ActionType> {
          override fun invoke(state: Int, action: IAction<ActionType, *>): Int {
              when (action.type) {
                  ADD -> return state + (action as AddAction).payload.get()
                  ASYNC_ADD -> return state
              }
          }
      }
      
## Middleware.
Lets define two middlewares, one for logging

    // Logs the Action type.
    val loggerMiddleware = object : IMiddleware<Int, ActionType> {
        override fun invoke(state: Int, dispatcher: Dispatcher<ActionType>, next: Dispatcher<ActionType>): Dispatcher<ActionType> {
            return { action ->
                System.out.println(String.format("%s - %s", "Logger Middleware", action.type.name));
                next(action)
            }
        }
    }

and one for generating Async actions.

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
    
## Store
Lets create a store that stores an Integer state based on the reducers and middleware we defined.

    val store: IStore<Int, ActionType> = Store.create(reducer, 0, applyMiddleware(
                loggerMiddleware,
                thunkMiddleware))
    
We can now start observing out store:

    store.observable.subscribe({ state ->
        System.out.println(state)
    })
    
Finally lets start dispatching actions to our store.

    store.dispatch(ActionCreator.createAddAction(4))        // Logger: ADD, State: 4
    store.dispatch(ActionCreator.createAsyncAddAction(10))  // Logger: ASYNC_ADD, ADD, State: 14
