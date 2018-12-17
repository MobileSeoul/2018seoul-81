package com.jhmk.stampme.Module.Dialog

import android.graphics.Bitmap
import com.jhmk.stampme.Model.User

interface Barcode {
    interface view{
        fun onResultMakeBarcode(user : User, bitmap : Bitmap?)
    }
    interface presenter{
        fun requestMakeBarcode(user : User)
    }
}