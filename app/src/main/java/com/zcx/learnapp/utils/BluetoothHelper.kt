package com.zcx.learnapp.utils

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.app.ActivityCompat

class BluetoothHelper(val context: Context) {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var callback: Callback? = null
    private var isReceiverRegister = false

    fun registerReceiver() {
        val intent = IntentFilter()
        intent.addAction(BluetoothDevice.ACTION_FOUND) // 用BroadcastReceiver来取得搜索结果

        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(mReceiver, intent)

        isReceiverRegister = true
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(mReceiver)
    }

    fun discoveryBluetoothDevice() {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "该设备不支持蓝牙")
            callback?.onBluetoothAdapterNotFound()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            Log.d(TAG, "蓝牙未开启")
            callback?.onBluetoothDisabled()
        }

        if (!isReceiverRegister) {
            registerReceiver()
        }

        ActivityCompat.requestPermissions((context as Activity), arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION), 1)
        bluetoothAdapter.startDiscovery()
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var device: BluetoothDevice? = null
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null) {
                        callback?.onBluetoothDeviceFound(device)
                        Log.d(TAG, "发现设备：${device.name} ${device.bluetoothClass.majorDeviceClass}")
                    }
                }
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {

                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {

                }
            }
        }
    }

    abstract class Callback {
        open fun onBluetoothAdapterNotFound() {

        }

        open fun onBluetoothDisabled() {

        }

        open fun onBluetoothDeviceBonding(bluetoothDevice: BluetoothDevice) {

        }

        open fun onBluetoothDeviceBonded(bluetoothDevice: BluetoothDevice) {

        }

        open fun onBluetoothDeviceNotBond(bluetoothDevice: BluetoothDevice) {

        }

        open fun onBluetoothDeviceFound(bluetoothDevice: BluetoothDevice) {

        }

        open fun onBluetoothDeviceDiscoveryFinished() {

        }
    }

    companion object {
        val TAG: String = BluetoothHelper::class.java.simpleName
    }
}