package com.zcx.learnapp

import android.graphics.Point
import com.zcx.learnapp.printer.Column
import com.zcx.learnapp.printer.IPrinter
import com.zcx.learnapp.printer.LabelInfo
import com.zcx.learnapp.printer.TablePrintUtil

class TemplateB(printer: IPrinter) : BaseTemplate(printer) {
    override fun print(map: Map<String, Any>) {
        val printer = this.printer

        val labelInfo = LabelInfo(580, 760)
        val labelWidth = labelInfo.width
        printer.prepare(labelInfo.height)
        val textSize = 24

        var text: String
        var offsetY: Int
        val startPoint = Point(0, 0)

        printer.apply {
            // 外贴箱单
            setBold(1)
            val mag = 2
            setMag(mag, mag)
            text = "外贴箱单"
            var tempPoint = Point(startPoint)
            val magnifiedTextSize = textSize * mag
            tempPoint.offset(magnifiedTextSize, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, text)
            setMag(1, 1)
            setBold(0)

            val textWidth = textWidth(text, textSize) * mag
            startPoint.offset(textWidth + magnifiedTextSize * 2, 0)

            // PO 号
            val textStartPoint = Point(startPoint)
            printText(IPrinter.TEXT_0, 0, textStartPoint, "PO  号")
            tempPoint = Point(textStartPoint).apply { offset(textSize * 4, 0) }
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "outDeliveryOrderNo"))

            offsetY = textSize + 8
            textStartPoint.offset(0, offsetY)

            // 箱序号
            printText(IPrinter.TEXT_0, 0, textStartPoint, "箱序号")
            tempPoint = Point(textStartPoint).apply { offset(textSize * 4, 0) }
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "boxNumAndTotal"))

            startPoint.x = 0
            startPoint.offset(0, magnifiedTextSize + textSize)

            tempPoint = Point(startPoint).apply { offset(magnifiedTextSize, 0) }

            // 打印发货箱号
            printBarcodeCommon(true, tempPoint, true, getString(map, "deliveryBoxCode"))

            startPoint.x = 0
            startPoint.offset(0, textSize * 4)

            // 打印表格
            try {
                val detailList = map["detailList"] as? List<Map<String, Any>> ?: emptyList()
                val columns = ArrayList<Column>()

                var list: ArrayList<String> = ArrayList()
                detailList.forEach { list.add(getString(it, "barcode")) }
                columns.add(Column().apply { name = "货品条码";this.list = list })

                list = ArrayList()
                detailList.forEach { list.add(getInt(it, "goodsQty").toString()) }
                columns.add(Column().apply { name = "数量";this.list = list })

                startPoint.offset(magnifiedTextSize, 0)
                TablePrintUtil().printTable(printer, columns, startPoint, labelInfo)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            startPoint.x = 0
            startPoint.offset(0, textSize)

            // 打印直线
            tempPoint = Point(startPoint)
            tempPoint.offset(labelWidth, 0)
            printLine(startPoint, tempPoint, 3)

            startPoint.offset(0, textSize)

            // SKU 数
            tempPoint = Point(startPoint)
            printText(IPrinter.TEXT_0, 0, tempPoint, "SKU 数：")
            tempPoint.offset(textSize * 4, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, getInt(map, "boxSkuNum").toString())

            tempPoint.x = labelWidth / 3

            // 操作员
            tempPoint.offset(textSize, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, "操作员：")
            tempPoint.offset(textSize * 5, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "printPerson"))

            startPoint.offset(0, textSize + 8)

            // 件数
            tempPoint = Point(startPoint)
            printText(IPrinter.TEXT_0, 0, tempPoint, "件  数：")
            tempPoint.offset(textSize * 4, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, getInt(map, "boxSkuNum").toString())

            tempPoint.x = labelWidth / 3

            // 打印时间
            printText(IPrinter.TEXT_0, 0, tempPoint, "打印时间：")
            tempPoint.offset(textSize * 5, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "printTime"))
        }

        printer.print()
    }
}