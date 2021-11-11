package com.zcx.learnapp.activity

import android.os.Bundle
import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject

@Subject("表格测试")
class TableTestActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_table_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = listOf(
            Item("69001", "M FAV DEC HDY", 3, "ZP", "/", "A-01"),
            Item("69001", "M FAV DEC HDY", 1, "CC-AG", "/", "A-01"),
            Item("69002", "CROPPED LS", 2, "ZP", "/", "A-02"),
            Item("69003", "PONCHO", 1, "ZP", "/", "A-02"),
            Item("69004", "W ESNTL LG T 1", 3, "ZP", "/", "A-02"),
            Item("69005", "HOODED TRACKTOP", 6, "ZP", "/", "A-03"),
        )

        val smartTable = findViewById<com.bin.david.form.core.SmartTable<Item>>(R.id.smartTable)
        smartTable.setData(list)
        smartTable.config.apply {
            isShowXSequence = false
            isShowYSequence = false
        }
    }

    @SmartTable
    class Item(
        @SmartColumn(name = "货品条码", id = 0)
        private val barcode: String,
        @SmartColumn(name = "货品名称", id = 2)
        private val name: String,
        @SmartColumn(name = "数量", id = 1)
        var quantity: Int,
        @SmartColumn(name = "库存类型", id = 3)
        private val inventoryType: String,
        @SmartColumn(name = "效期", id = 4)
        private val expiredDate: String,
        @SmartColumn(name = "库位", id = 5)
        private val locationCode: String
    )
}