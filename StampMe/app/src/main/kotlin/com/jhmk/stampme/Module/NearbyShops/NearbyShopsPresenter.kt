package com.jhmk.stampme.Module.NearbyShops

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Module.DataBase.DataBaseReference
import java.util.*

class NearbyShopsPresenter(view: NearbyShops.view) : NearbyShops.presenter {
    private val TAG = this.javaClass.simpleName
    private val mView = view

    override fun requestAroundShopList(context: Context) {
        Log.d(TAG, "##### requestAroundShopList #####")
        val resultList: MutableList<Shops?> = ArrayList()
        DataBaseReference.mShopsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### requestAroundShopList ##### onCancelled")
                DataBaseReference.mShopsDatabaseReference.removeEventListener(this)
                mView.onResultAroundShopList(resultList)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### requestAroundShopList #####")
                val child = dataSnapShot.children.toMutableList()
                Log.d(TAG, "##### requestAroundShopList ##### child.size :  ${child.size}")

                for (value in child.withIndex()) {
                    resultList.add(value.value.getValue(Shops().javaClass))
                }
                mView.onResultAroundShopList(resultList)
                DataBaseReference.mShopsDatabaseReference.removeEventListener(this)
            }
        })
    }
}