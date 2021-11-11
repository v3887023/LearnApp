package com.zcx.learnapp.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.learnapp.printer.HMA300Printer
import com.zcx.learnapp.printer.IPrinter
import com.zcx.learnapp.utils.BluetoothHelper
import com.zcx.lib_annotations.Subject
import kotlinx.android.synthetic.main.activity_print_test.*

@Subject("打印测试")
class PrintTestActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_print_test

    private val printer: IPrinter = HMA300Printer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_ADMIN), 1)

        connectButton.setOnClickListener {
            doConnect()
        }

        printButton.setOnClickListener {
            doPrint()
        }
    }

    private fun doConnect() {
        val state = printer.connect("FC:58:FA:33:6F:BE")
        Log.d(TAG, "state: $state")

        if (state != 0) {
            "打印机连接失败".toast()
        }
    }

    private fun doPrint() {
//        val jsonString =
//            "{\"code\":0,\"msg\":\"\",\"data\":{\"additionalPrintTimes\":0,\"boxGoodsNum\":16,\"boxNumAndTotal\":\"1\",\"boxSkuNum\":4,\"brand\":\"京东\",\"carrier\":\"顺丰\",\"deliveryBoxCode\":\"12345678\",\"detailList\":[{\"barcode\":\"69001\",\"brandName\":\"京东\",\"goodsCode\":\"JD123456\",\"goodsQty\":20,\"specification\":\"商品1号\"},{\"barcode\":\"69002\",\"brandName\":\"京东\",\"goodsCode\":\"JD6346242\",\"goodsQty\":13,\"specification\":\"商品2号\"},{\"barcode\":\"69003\",\"brandName\":\"淘宝\",\"goodsCode\":\"TB27509137590\",\"goodsQty\":7,\"specification\":\"商品3号\"},{\"barcode\":\"69004\",\"brandName\":\"拼夕夕\",\"goodsCode\":\"PXX8175907151\",\"goodsQty\":100,\"specification\":\"商品4号\"},{\"barcode\":\"69005\",\"brandName\":\"天猫\",\"goodsCode\":\"TM7097890171\",\"goodsQty\":2,\"specification\":\"商品5号\"}],\"orderCode\":\"YJ12345678\",\"outDeliveryOrderNo\":\"PO12345678\",\"ownerAddress\":\"宝安区西乡街道\",\"ownerCode\":\"000000\",\"ownerContactPerson\":\"张三\",\"ownerContactPhone\":\"13888888888\",\"ownerName\":\"张先生\",\"printPerson\":\"李四\",\"printTemplateCode\":\"1\",\"printTime\":\"2021-9-2 16:59\",\"printTimes\":1,\"provinceCityDistrict\":\"广东省深圳市\",\"receiverName\":\"王五\",\"scheduleEndDayTime\":\"2021-9-5 12:00\",\"sourceOrderCode\":\"JD12345678\",\"storeName\":\"无良店铺\",\"tableCol\":\"\",\"taskCode\":\"RW12345678\",\"trackingNumer\":\"SF12345678\",\"warehouseAddress\":\"南山区\",\"warehouseCode\":\"10005\",\"warehouseContactMobile\":\"10086\",\"warehouseContactPerson\":\"赵六\",\"warehouseName\":\"切仓A仓\"}}"
//
//        val bo = jsonString.jsonTo<Bo>()
//        TemplateA(printer).print(bo.data)
//        TemplateB(printer).print(bo.data)
//        TemplateC(printer).print(bo.data)

        bluetoothHelper.discoveryBluetoothDevice()
    }

    override fun onDestroy() {
        super.onDestroy()

        bluetoothHelper.unregisterReceiver()
    }

    private val bluetoothHelper = BluetoothHelper(this)

    class Bo {
        var data: Map<String, Any> = emptyMap()
    }

    companion object {
        val TAG: String = PrintTestActivity::class.java.simpleName
    }
}