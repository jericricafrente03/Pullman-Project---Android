package com.ph.bittelasia.libvlc.model

import java.io.Serializable

class VideoCategory(builder: Builder) : Serializable, Comparable<VideoCategory> {
    private val categoryID: Int
    private val name: String?
    private val description: String?
    private val icon: String?
    fun getName(): String {
        return name ?: "no name"
    }

    fun getDescription(): String {
        return description ?: "no description"
    }

    override fun toString(): String {
        return "VideoCategory{" +
                "id=" + categoryID + "," +
                "name=" + name + "," +
                "description=" + description + "," +
                "icon=" + icon +
                "}"
    }

    override fun compareTo(info: VideoCategory): Int {
        return categoryID - info.categoryID
    }

    class Builder {
        var id = 0
        var name: String? = null
        var description: String? = null
        var icon: String? = null
        fun id(id: Int): Builder {
            this.id = id
            return this
        }

        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        fun desc(desc: String?): Builder {
            description = desc
            return this
        }

        fun icon(icon: String): Builder {
            this.icon = icon
            return this
        }

        fun buildVideoCategory(): VideoCategory {
            return VideoCategory(this)
        }
    }

    init {
        categoryID = builder.id
        name = builder.name
        description = builder.description
        icon = builder.icon
    }
}