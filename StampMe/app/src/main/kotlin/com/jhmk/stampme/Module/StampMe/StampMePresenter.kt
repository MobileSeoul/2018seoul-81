package com.jhmk.stampme.Module.StampMe

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jhmk.stampme.Model.*
import com.jhmk.stampme.Module.DataBase.DataBaseReference
import com.jhmk.stampme.Module.Utils.MakeBarcode
import org.greenrobot.eventbus.EventBus
import srjhlab.com.myownbarcode.Dialog.StampInfoDialog

class StampMePresenter(view: StampMe.view) : StampMe.presenter {
    private val TAG = this.javaClass.simpleName
    private val mView = view

    override fun requestMakeBarcode(user: User) {
        Log.d(TAG, "##### requestMakeBarcode #####")
        val gson = Gson()
        val jsonObject = gson.toJson(user)
        mView.onResultMakeBarcode(user, MakeBarcode.getQRCode(jsonObject))
    }

    override fun requestGetMyStamp(user: User) {
        Log.d(TAG, "##### requestGetMyStamp userId : ${user.userId}#####")
        val resultList: MutableList<Stamps?> = ArrayList()
        DataBaseReference.mStampsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### requestGetMyStamp ##### onCancelled")
                DataBaseReference.mStampsDatabaseReference.removeEventListener(this)
                mView.onResultGetMyStamp(null)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### requestGetMyStamp ##### onDataChange")
                val child = dataSnapShot.child(user.userId).children.toMutableList()

                for (value in child.withIndex()) {
                    resultList.add(value.value.getValue(Stamps().javaClass))
                }

                getCurrentShopList(resultList)
                DataBaseReference.mStampsDatabaseReference.removeEventListener(this)
            }
        })
    }

    private fun getCurrentShopList(stampList: MutableList<Stamps?>): MutableList<Shops?> {
        val shopList: MutableList<Shops?> = ArrayList()
        val myStampResultList: MutableList<MyStamps?> = ArrayList()
        DataBaseReference.mShopsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### getCurrentShopList ##### onCancelled")
                DataBaseReference.mShopsDatabaseReference.removeEventListener(this)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### getCurrentShopList #####")
                val child = dataSnapShot.children.toMutableList()

                for (value in child.withIndex()) {
                    shopList.add(value.value.getValue(Shops().javaClass))
                }

                Log.d(TAG, "##### getCurrentShopList ##### resultList.size :  ${shopList.size} stampList.size : ${stampList.size}")

                for ((index, value) in stampList.withIndex()) {
                    Log.d(TAG, "##### getCurrentShopList ##### ${value?.stampSource}")
                    for (value in shopList.withIndex()) {
                        if (value.value!!.shopName == stampList[index]!!.stampSource) {
                            myStampResultList.add(MyStamps(stampList[index]!!.stampSource, value.value!!.shopType, value.value!!.shopImageUrl, stampList[index]!!.stampReason, value.value!!.shopAddress, stampList[index]!!.stampDate))
                            Log.d(TAG, "##### getCurrentShopList ##### ${stampList[index]!!.stampSource}   ${value.value!!.shopImageUrl} ")
                        }
                    }

                }
                mView.onResultGetMyStamp(myStampResultList)
                DataBaseReference.mShopsDatabaseReference.removeEventListener(this)
            }
        })
        return shopList
    }

    override fun requestSeperateMyStamp(myStampList: MutableList<MyStamps?>) {
        Log.d(TAG, "##### getSeperateType #####")
        val cafeList: MutableList<MyStamps?> = ArrayList()
        val restaurantcList: MutableList<MyStamps?> = ArrayList()
        val storeList: MutableList<MyStamps?> = ArrayList()
        val martList: MutableList<MyStamps?> = ArrayList()
        val publicList: MutableList<MyStamps?> = ArrayList()
        val etcList: MutableList<MyStamps?> = ArrayList()

        for ((index, value) in myStampList.withIndex()) {
            when (value!!.stampSourceType) {
                ConstVariables.SHOP_TYPE_CAFE -> cafeList.add(myStampList[index])
                ConstVariables.SHOP_TYPE_RESTRAUNT -> restaurantcList.add(myStampList[index])
                ConstVariables.SHOP_TYPE_STORE -> storeList.add(myStampList[index])
                ConstVariables.SHOP_TYPE_MART -> martList.add(myStampList[index])
                ConstVariables.SHOP_TYPE_PUBLIC -> publicList.add(myStampList[index])
                ConstVariables.SHOP_TYPE_ETC -> etcList.add(myStampList[index])
            }
        }
        mView.onResultSeperateMyStamp(cafeList, restaurantcList, storeList, martList, publicList, etcList)
    }

    override fun requestSettings(user : User) {
        Log.d(TAG, "##### requestSettings #####")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_SHOW_SETTINGS, user))
    }

    override fun requestLogout(user: User) {
        Log.d(TAG, "##### requestLogout #####")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_REQUEST_LOGOUT, user as Any))
    }
}