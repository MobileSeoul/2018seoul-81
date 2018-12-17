package com.jhmk.stampme.Module.Utils

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

object MakeBarcode {
    private val TAG = this.javaClass.simpleName
    private val WIDTH = 400
    private val HEIGHT = 400

    fun getQRCode(value: String): Bitmap? {
        Log.d(TAG, "##### getQRCode ##### value : $value")
        var bmp: Bitmap? = null

        val c9 = QRCodeWriter()
        try {
            val bm = c9.encode(value, BarcodeFormat.QR_CODE, WIDTH, HEIGHT)
            bmp = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)

            for (i in 0..WIDTH - 1) {
                for (j in 0..HEIGHT - 1) {
                    bmp!!.setPixel(i, j, if (bm.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
            bmp = null
        }
        return bmp
    }
}