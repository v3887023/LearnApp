package com.zcx.learnapp.printer

import android.content.Context
import android.graphics.Point
import cpcl.PrinterHelper

class HMA300Printer(private val context: Context) : IPrinter {
    override fun connect(address: String): Int {
        return PrinterHelper.portOpenBT(context, address)
    }

    override fun prepare(height: Int): Int {
        return PrinterHelper.printAreaSize("0", "0", "0", "$height", "1")
    }

    override fun printLine(start: Point, end: Point, width: Int): Int {
        return PrinterHelper.Line("${start.x}", "${start.y}", "${end.x}", "${end.y}", "$width")
    }

    override fun printBox(leftTop: Point, rightBottom: Point, width: Int): Int {
        return PrinterHelper.Box(
            "${leftTop.x}",
            "${leftTop.y}",
            "${rightBottom.x}",
            "${rightBottom.y}",
            "$width"
        )
    }

    override fun printText(
        @IPrinter.TextDirection direction: Int,
        size: Int,
        position: Point,
        text: String
    ): Int {
        val command = when (direction) {
            IPrinter.TEXT_90 -> PrinterHelper.TEXT90
            IPrinter.TEXT_180 -> PrinterHelper.TEXT180
            IPrinter.TEXT_270 -> PrinterHelper.TEXT270
            else -> PrinterHelper.TEXT
        }
        return PrinterHelper.Text(command, "7", "0", "${position.x}", "${position.y}", text)
    }

    override fun printTextCenter(
        @IPrinter.LimitedTextDirection direction: Int,
        position: Point,
        width: Int,
        text: String
    ): Int {
        val command =
            if (direction == IPrinter.TEXT_270) PrinterHelper.TEXT270 else PrinterHelper.TEXT
        return PrinterHelper.AutCenter(command, "${position.x}", "${position.y}", width, 8, text)
    }

    override fun printTextAutoNewLine(position: Point, width: Int, text: String) {
        PrinterHelper.AutLine("${position.x}", "${position.y}", width, 8, false, false, text)
    }

    /**
     * @param bold 加粗系数（范围：1-5），0 表示不加粗
     */
    override fun setBold(bold: Int): Int {
        return PrinterHelper.SetBold("$bold")
    }

    /**
     * @param width 字体宽度的放大倍数，1 表示不放大
     * @param height 字体高度的放大倍数，1 表示不放大
     */
    override fun setMag(width: Int, height: Int): Int {
        return PrinterHelper.SetMag("$width", "$height")
    }

    override fun preFeed(dots: Int): Int {
        return PrinterHelper.Prefeed("$dots")
    }

    override fun printBarcode(
        horizontal: Boolean,
        type: String,
        width: Int,
        ratio: Int,
        height: Int,
        position: Point,
        textVisible: Boolean,
        number: Int,
        size: Int,
        offset: Int,
        barcode: String
    ): Int {
        val command = if (horizontal) PrinterHelper.BARCODE else PrinterHelper.VBARCODE
        return PrinterHelper.Barcode(
            command, type, "$width", "$ratio", "$height", "${position.x}",
            "${position.y}", textVisible, "$number", "$size", "$offset", barcode
        )
    }

    override fun printBarcodeCommon(
        horizontal: Boolean,
        position: Point,
        textVisible: Boolean,
        barcode: String
    ): Int {
        return printBarcode(
            horizontal, PrinterHelper.code128, 1, 1, 50, position,
            textVisible, 7, 0, 5, barcode
        )
    }

    override fun form(): Int {
        return PrinterHelper.Form()
    }

    override fun print(): Int {
        return PrinterHelper.Print()
    }
}