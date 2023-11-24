package com.eltex.androidschool.model

enum class AttachmentType {
    IMAGE,
    AUDIO,
    VIDEO,
    ;

    override fun toString(): String {
        return when (this) {
            IMAGE -> "Image"
            AUDIO -> "Audio"
            VIDEO -> "Video"
        }
    }

    companion object {
        fun fromString(type: String): AttachmentType {
            return when (type) {
                "IMAGE", "Image", "image" -> IMAGE
                "AUDIO", "Audio", "audio" -> AUDIO
                "VIDEO", "Video", "video" -> VIDEO
                else -> error("Error attachment type: $type")
            }
        }
    }
}