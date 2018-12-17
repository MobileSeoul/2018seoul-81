package com.jhmk.stampme.Module.NearbyShops

import android.content.Context
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Module.Base.BasePresenter

interface NearbyShops {
    interface view {
        fun onResultAroundShopList(list: MutableList<Shops?>)
    }

    interface presenter{
        fun requestAroundShopList(context: Context)
    }
}