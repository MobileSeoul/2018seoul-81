package com.jhmk.stampme.Module.Dialog

import android.util.Log
import com.google.gson.Gson
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Utils.MakeBarcode

class BarcodeDialogPresenter(view : Barcode.view) : Barcode.presenter {
    val TAG = this.javaClass.simpleName
    val mView = view

    override fun requestMakeBarcode(user: User) {
        Log.d(TAG, "##### requestMakeBarcode #####")
        val gson = Gson()
        val jsonObject = gson.toJson(user)
        mView.onResultMakeBarcode(user, MakeBarcode.getQRCode(jsonObject))
    }
}