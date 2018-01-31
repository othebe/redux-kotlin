package api

interface IStoreEnhancer<S, A> : (IStore<S, A>) -> IStore<S, A>