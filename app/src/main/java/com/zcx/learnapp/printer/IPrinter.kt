package com.zcx.learnapp.printer

import android.graphics.Point
import androidx.annotation.IntDef

interface IPrinter {

    /**
     * 连接到指定的地址 [address]
     * @param address 设备地址
     */
    fun connect(address: String): Int

    /**
     * 准备打印，设置纸的高度
     * @param height 纸的高度
     */
    fun prepare(height: Int): Int

    /**
     * 打印直线
     * @param start 直线开始的位置
     * @param end 直线结束的位置
     * @param width 直线的宽度
     */
    fun printLine(start: Point, end: Point, width: Int): Int

    /**
     * 打印矩形框
     * @param leftTop 矩形框左上角的位置
     * @param rightBottom 矩形框右下角的位置
     * @param width 矩形框的宽度
     */
    fun printBox(leftTop: Point, rightBottom: Point, width: Int): Int

    /**
     * 打印文本
     * @param direction 文本方向
     * @param size 字体大小
     * @param position 打印位置
     * @param text 要打印的文本
     */
    fun printText(@TextDirection direction: Int, size: Int, position: Point, text: String): Int

    /**
     * 打印文本并居中
     * @param direction 文本方向
     * @param position 打印位置
     * @param width 文本框的宽度（文本框不会打印）
     * @param text 要打印的文本
     */
    fun printTextCenter(
        @LimitedTextDirection direction: Int,
        position: Point,
        width: Int,
        text: String
    ): Int

    /**
     * 打印文本，超过给定的宽度 [width] 时会自动换行
     * @param position 打印位置
     * @param width 一行打印的宽度
     * @param text 要打印的文本
     */
    fun printTextAutoNewLine(position: Point, width: Int, text: String)

    /**
     * 字体加粗
     * @param bold 加粗系数
     */
    fun setBold(bold: Int): Int

    /**
     * 设置字符宽高放大倍数
     * @param width 字体宽度的放大倍数
     * @param height 字体高度的放大倍数
     */
    fun setMag(width: Int, height: Int): Int

    /**
     * 走纸
     * @param dots 走纸的距离，单位为 dot
     */
    fun preFeed(dots: Int): Int

    /**
     * 打印条码
     * @param horizontal true 为水平方向，false 为垂直方向
     * @param type 条码类型
     * @param width 条码窄条的单位宽度
     * @param ratio 条码宽条窄条的比例
     * @param height 条码高度
     * @param position 条码打印位置
     * @param textVisible 条码下方的数据是否可见
     * @param number 字体的类型
     * @param size 字体的大小
     * @param offset 条码与文字间的距离
     * @param barcode 条码数据
     */
    fun printBarcode(
        horizontal: Boolean, type: String, width: Int, ratio: Int, height: Int, position: Point,
        textVisible: Boolean, number: Int, size: Int, offset: Int, barcode: String
    ): Int

    /**
     * 由于 [printBarcode] 方法需要的参数过多，这里增加了一个通用的方法方便调用
     * @param horizontal true 为水平方向，false 为垂直方向
     * @param position 条码打印位置
     * @param textVisible 条码下方的数据是否可见
     * @param barcode 条码数据
     */
    fun printBarcodeCommon(
        horizontal: Boolean,
        position: Point,
        textVisible: Boolean,
        barcode: String
    ): Int

    /**
     * 定位到下一页纸
     */
    fun form(): Int

    /**
     * 发送打印指令
     */
    fun print(): Int

    companion object {
        const val TEXT_0 = 0
        const val TEXT_90 = 1
        const val TEXT_180 = 2
        const val TEXT_270 = 3
    }

    @IntDef(TEXT_0, TEXT_90, TEXT_180, TEXT_270)
    annotation class TextDirection {

    }

    @IntDef(TEXT_0, TEXT_270)
    annotation class LimitedTextDirection
}