package com.mindeurfou.model

enum class GBState {
    INIT,
    STARTING,
    PENDING,
    DONE;

    override fun toString(): String {
        return when (name) {
            INIT.name ->  "init"
            STARTING.name -> "starting"
            PENDING.name -> "pending"
            DONE.name -> "done"
            else -> ""
        }
    }

    companion object {
        fun toState(state: String): GBState? {
            return when (state) {
                INIT.toString() -> INIT
                STARTING.toString() -> STARTING
                PENDING.toString() -> PENDING
                DONE.toString() -> DONE
                else -> null
            }
        }
    }
}