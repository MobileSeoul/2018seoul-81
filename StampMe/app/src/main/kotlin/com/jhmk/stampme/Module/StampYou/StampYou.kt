package com.jhmk.stampme.Module.StampYou

import android.app.Activity
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User

interface StampYou {
    interface view {
        fun onResultMyStore(result: Boolean, msg: Int, store: Shops?)
    }

    interface presenter {
        fun requestGetMyStore(user: User)
        fun requestSetBarcodeScan(store : Shops, activity : Activity)
        fun requestLogout(user : User)
    }
}