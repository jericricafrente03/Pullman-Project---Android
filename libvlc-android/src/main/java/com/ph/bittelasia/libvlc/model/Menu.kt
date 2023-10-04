package com.ph.bittelasia.libvlc.model

import android.net.Uri
import java.io.Serializable

class Menu(builder: Builder) : Serializable, Comparable<Menu> {
    private val order: Int
    val name: String?
    private val iconFile: Uri?
    private val aClass: Class<*>?
    override fun toString(): String {
        return "Menu{" +
                "order=" + order +
                ", name='" + name + '\'' +
                ", icon='" + iconFile + '\'' +
                ", aClass=" + aClass +
                '}'
    }

    override fun compareTo(o: Menu): Int {
        return order - o.order
    }

    fun getaClass(): Class<*>? {
        return aClass
    }

    class Builder {
        var orderNo = 0
        var name: String? = null
        var iconFile: Uri? = null
        var aClass: Class<*>? = null
        fun orderNo(orderNo: Int): Builder {
            this.orderNo = orderNo
            return this
        }

        fun name(name: String?): Builder {
            this.name = name
            return this
        }

        fun iconFile(iconFile: Uri?): Builder {
            this.iconFile = iconFile
            return this
        }

        fun aClass(c: Class<*>?): Builder {
            aClass = c
            return this
        }

        fun buildMenu(): Menu {
            return Menu(this)
        }
    }

    init {
        order = builder.orderNo
        name = builder.name
        iconFile = builder.iconFile
        aClass = builder.aClass
    }
}