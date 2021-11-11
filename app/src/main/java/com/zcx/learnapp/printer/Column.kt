package com.zcx.learnapp.printer

import kotlin.math.max

class Column {
    var name: String = ""
    var list: MutableList<String> = ArrayList()
    var columnWidth: Int = 0

    fun computeColumnWidth() {
        var columnWidth = textWidth(name, 24)

        for (text in list) {
            val width = textWidth(text, 24)
            columnWidth = max(width, columnWidth)
        }

        this.columnWidth = columnWidth
    }

    private fun textWidth(text: String, textSize: Int): Int {
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