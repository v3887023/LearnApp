package com.zcx.learnapp

import com.zcx.learnapp.printer.IPrinter

abstract class BaseTemplate(protected val printer: IPrinter) {
    abstract fun print(map: Map<String, Any>)

    protected fun getString(map: Map<String, Any>, key: String): String {
        return (map[key] as? String).orEmpty()
    }

    protected fun getDouble(map: Map<String, Any>, key: String): Double {
        return (map[key] as? Double) ?: 0.0
    }

    protected fun getInt(map: Map<String, Any>, key: String): Int {
        return getDouble(map, key).toInt()
    }

    protected fun textWidth(text: String, textSize: Int): Int {
        var length = 0f
        for (c in text) {
            if (c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9' || c == ' ') {
                length += 0.5f
            } else {
                length++
            }
        }

        return (length * textSize).toInt()
    }
}