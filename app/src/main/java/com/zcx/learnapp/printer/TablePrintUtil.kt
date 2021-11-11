package com.zcx.learnapp.printer

import android.graphics.Point
import kotlin.math.max
import kotlin.math.min

class TablePrintUtil {
    fun printTable(
        printer: IPrinter,
        columns: List<Column>,
        position: Point,
        labelInfo: LabelInfo,
        pageable: Boolean = true
    ) {
        val textSize = 24
        var tableWidth = 0
        var tableHeight = 0
        val paddingHorizontal = 2
        val paddingVertical = 2
        var rowCount = 0
        val columnCount = columns.size
        var printedRowCount = 0
        val rowHeight = textSize + paddingVertical * 2
        for (column in columns) {
            column.computeColumnWidth()
            tableWidth += column.columnWidth + paddingHorizontal * 2
            rowCount = max(column.list.size, rowCount)
        }

        var finalY: Int = 0

        printer.apply {
            var currentRowIndex = 0
            val height = labelInfo.height
            while (currentRowIndex < rowCount) {
                // 打印表格线
                val heightAvailable = height - position.y
                val maxCount = heightAvailable / rowHeight // 剩余的高度可以画几行，包括列名那一行
                val count = min(maxCount, rowCount - currentRowIndex + 1) // 本页需要打印的行数，包括列名那一行
                val currentTableHeight = count * rowHeight

                var startPoint = Point(position)
                var endPoint = Point(position).apply { offset(tableWidth, 0) }

                // 打印横线
                printLine(startPoint, endPoint, 1)
                for (i in 0 until count) {
                    startPoint.offset(0, rowHeight)
                    endPoint.offset(0, rowHeight)
                    printLine(startPoint, endPoint, 1)
                }

                finalY = endPoint.y

                startPoint = Point(position)
                endPoint = Point(position).apply { offset(0, currentTableHeight) }

                // 打印竖线及列名
                printLine(startPoint, endPoint, 1)
                for (i in 0 until columnCount) {
                    val column = columns[i]
                    val columnWidth = column.columnWidth + paddingHorizontal * 2
                    val textStartPoint = Point(startPoint).apply { offset(0, paddingVertical) }
                    // 打印列名
                    printTextCenter(IPrinter.TEXT_0, textStartPoint, columnWidth, column.name)

                    // 打印每一列数据
                    val textPosition = Point(startPoint).apply {
                        offset(
                            paddingHorizontal,
                            rowHeight + paddingVertical
                        )
                    }
                    for (j in currentRowIndex until rowCount) {
                        // 打印表格数据
                        printText(IPrinter.TEXT_0, 0, textPosition, column.list[j])
                        textPosition.offset(0, rowHeight)
                    }

                    // 打印竖线
                    startPoint.offset(columnWidth, 0)
                    endPoint.offset(columnWidth, 0)
                    printLine(startPoint, endPoint, 1)
                }

                currentRowIndex += count - 1

                if (currentRowIndex < rowCount - 1) {
                    printer.form()
                    printer.print()
                    printer.prepare(760)
                    position.y = 16
                }
            }
        }

        position.y = finalY
    }
}