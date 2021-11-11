package com.zcx.learnapp

import android.graphics.Point
import com.zcx.learnapp.printer.Column
import com.zcx.learnapp.printer.IPrinter
import com.zcx.learnapp.printer.LabelInfo
import com.zcx.learnapp.printer.TablePrintUtil

class TemplateA(printer: IPrinter) : BaseTemplate(printer) {

    override fun print(map: Map<String, Any>) {
        val printer = this.printer

        printer.prepare(760)
        val startPoint = Point(0, 0)
        val endPoint = Point(580, 0)

        val labelWidth = 580
        val labelHeight = 760
        val textSize = 24

        var text: String
        var offsetY: Int

        printer.apply {
            // 内置装箱单
            setBold(1)
            val mag = 2
            setMag(mag, mag)
            text = "内置装箱单"
            var tempPoint = Point(startPoint)
            tempPoint.offset((labelWidth - textWidth(text, textSize) * mag) / 2, 0)
            printText(IPrinter.TEXT_0, 0, tempPoint, text)
            setMag(1, 1)
            setBold(0)

            startPoint.offset(0, textSize * mag + 8)

            // 粗横线
            tempPoint = Point(startPoint)
            tempPoint.offset(labelWidth, 0)
            printLine(startPoint, tempPoint, 7)

            startPoint.offset(0, 18)

            // PO 号
            val textStartPoint = Point(startPoint).apply { offset(8, 0) }
            printText(IPrinter.TEXT_0, 0, textStartPoint, "PO  号")
            tempPoint = Point(textStartPoint).apply { offset(textSize * 4, 0) }
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "outDeliveryOrderNo"))

            offsetY = textSize + 8
            startPoint.offset(0, offsetY)
            textStartPoint.offset(0, offsetY)

            // 箱号
            printText(IPrinter.TEXT_0, 0, textStartPoint, "箱  号")
            tempPoint = Point(textStartPoint).apply { offset(textSize * 4, 0) }
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "deliveryBoxCode"))

            offsetY = textSize + 8
            startPoint.offset(0, offsetY)
            textStartPoint.offset(0, offsetY)

            // 箱序号
            printText(IPrinter.TEXT_0, 0, textStartPoint, "箱序号")
            tempPoint = Point(textStartPoint).apply { offset(textSize * 4, 0) }
            printText(IPrinter.TEXT_0, 0, tempPoint, getString(map, "boxNumAndTotal"))

            startPoint.offset(0, offsetY * 2)

            try {
                val detailList = map["detailList"] as? List<Map<String, Any>> ?: emptyList()
                val columns = ArrayList<Column>()

                var list: ArrayList<String> = ArrayList()
                detailList.forEach { list.add(getString(it, "barcode")) }
                columns.add(Column().apply { name = "货品条码";this.list = list })

                list = ArrayList()
                detailList.forEach { list.add(getString(it, "goodsCode")) }
                columns.add(Column().apply { name = "货号";this.list = list })

                list = ArrayList()
                detailList.forEach { list.add(getString(it, "specification")) }
                columns.add(Column().apply { name = "规格名称";this.list = list })

                list = ArrayList()
                detailList.forEach { list.add(getInt(it, "goodsQty").toString()) }
                columns.add(Column().apply { name = "数量";this.list = list })

                list = ArrayList()
                detailList.forEach { list.add(getString(it, "brandName")) }
                columns.add(Column().apply { name = "品牌";this.list = list })

                startPoint.x = 4
                TablePrintUtil().printTable(printer, columns, startPoint, LabelInfo(580, 760))

            } catch (e: Exception) {
                e.printStackTrace()
            }

            tempPoint = Point(startPoint).apply { x = labelWidth / 2;offset(0, offsetY) }
            printText(IPrinter.TEXT_0, 0, tempPoint, "总计件数        ${getInt(map, "boxGoodsNum")}")

            form()
            print()
        }
    }
}