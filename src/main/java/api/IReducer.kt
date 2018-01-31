package api

interface IReducer<S, in A> : (S, IAction<A, *>) -> S