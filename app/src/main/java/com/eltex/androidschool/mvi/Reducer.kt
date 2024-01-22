package com.eltex.androidschool.mvi

interface Reducer<State, Effect, Message> {
    fun reduce(old: State, message: Message): ReducerResult<State, Effect>
}

data class ReducerResult<State, Effect>(
    val state: State,
    val effects: Set<Effect>,
) {
    constructor(state: State, effect: Effect? = null) : this(state, setOfNotNull(effect))
}