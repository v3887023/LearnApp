package com.zcx.learnapp

import android.graphics.Point
import com.zcx.learnapp.printer.IPrinter
import com.zcx.learnapp.printer.LabelInfo

class TemplateC(printer: IPrinter) : BaseTemplate(printer) {
    override fun print(map: Map<String, Any>) {
        val printer = this.printer

        val labelInfo = LabelInfo(580, 760)
        val labelWidth = labelInfo.width
        printer.prepare(labelInfo.height)
        val textSize = 24

        var text: String
        var offsetY: Int
        val paddingHorizontal = 8
        val startPoint = Point(paddingHorizontal, 16)
        var tempPoint: Point

        printer.apply {
            val firstColumnWidth = 120
            val textColumnHeight = 40
            val barcodeColumnHeight = 100
            val paddingVertical = (textColumnHeight - textSize) / 2

            val tableHeight = 4 * textColumnHeight + 4 * barcodeColumnHeight
            val tableWidth = labelWidth - paddingHorizontal

            // 打印表格外方框
            val rightBottomPoint = Point(startPoint).apply { offset(tableWidth, tableHeight) }
            printer.printBox(startPoint, rightBottomPoint, 2)

            // 打印表格中间的竖线
            tempPoint = Point(startPoint).apply { offset(firstColumnWidth, 0) }
            val middleEndPoint = Point(tempPoint).apply { offset(0, tableHeight) }
            printer.printLine(tempPoint, middleEndPoint, 2)

            val firstColumnPoint = Point(startPoint)
            val secondColumnPoint = Point(firstColumnPoint)
            secondColumnPoint.offset(firstColumnWidth + textSize, paddingVertical)

            // 品牌
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "品牌")
            printer.printText(IPrinter.TEXT_0, 0, secondColumnPoint, getString(map, "brand"))

            firstColumnPoint.offset(0, textColumnHeight)
            secondColumnPoint.offset(0, textColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 送货仓库
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "送货仓库")
            printer.printText(IPrinter.TEXT_0, 0, secondColumnPoint, getString(map, "receiverName"))

            firstColumnPoint.offset(0, textColumnHeight)
            secondColumnPoint.offset(0, textColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 入库单号
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "入库单号")
            printer.printBarcodeCommon(true, secondColumnPoint, true, getString(map, "sourceOrderCode"))

            firstColumnPoint.offset(0, barcodeColumnHeight)
            secondColumnPoint.offset(0, barcodeColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 出库单号
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "出库单号")
            printer.printText(IPrinter.TEXT_0, 0, secondColumnPoint, getString(map, "outDeliveryOrderNo"))

            firstColumnPoint.offset(0, textColumnHeight)
            secondColumnPoint.offset(0, textColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 箱号
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "箱号")
            printer.printBarcodeCommon(true, secondColumnPoint, true, getString(map, "deliveryBoxCode"))

            firstColumnPoint.offset(0, barcodeColumnHeight)
            secondColumnPoint.offset(0, barcodeColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 要求到货时间
            val limitedWidth = 80
            val offsetX = (firstColumnWidth - limitedWidth) / 2
            tempPoint = Point(firstColumnPoint).apply { offset(offsetX, paddingVertical) }
            printer.printTextAutoNewLine(tempPoint, limitedWidth, "要求到货时间")
            printer.printBarcodeCommon(true, secondColumnPoint, true, getString(map, "scheduleEndDayTime"))

            firstColumnPoint.offset(0, barcodeColumnHeight)
            secondColumnPoint.offset(0, barcodeColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 承运商
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "承运商")
            printer.printText(IPrinter.TEXT_0, 0, secondColumnPoint, getString(map, "carrier"))

            firstColumnPoint.offset(0, textColumnHeight)
            secondColumnPoint.offset(0, textColumnHeight)
            tempPoint = Point(firstColumnPoint).apply { offset(tableWidth, 0) }
            printLine(firstColumnPoint, tempPoint, 2)

            // 运单号
            tempPoint = Point(firstColumnPoint).apply { offset(0, paddingVertical) }
            printer.printTextCenter(IPrinter.TEXT_0, tempPoint, firstColumnWidth, "运单号")
            printer.printBarcodeCommon(true, secondColumnPoint, true, getString(map, "trackingNumer"))
        }

        printer.print()
    }
}