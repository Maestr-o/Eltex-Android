package com.eltex.androidschool.model

enum class EventType {
    ONLINE,
    OFFLINE,
    ;

    override fun toString(): String {
        return when (this) {
            ONLINE -> "Online"
            OFFLINE -> "Offline"
        }
    }

    companion object {
        fun fromString(type: String): EventType {
            return when (type) {
                "ONLINE", "Online", "online" -> ONLINE
                "OFFLINE", "Offline", "offline" -> OFFLINE
                else -> error("Error event type: $type")
            }
        }
    }
}