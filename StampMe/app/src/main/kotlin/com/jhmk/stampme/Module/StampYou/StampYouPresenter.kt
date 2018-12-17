package com.jhmk.stampme.Module.StampYou

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.DisplayMetrics
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.gun0912.tedpermission.TedPermission
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.DataBase.DataBaseReference
import com.jhmk.stampme.R
import org.greenrobot.eventbus.EventBus

class StampYouPresenter(view: StampYou.view) : StampYou.presenter {
    val TAG = this.javaClass.simpleName
    val mView = view

    override fun requestGetMyStore(user: User) {
        Log.d(TAG, "##### requestGetMyStore ######")
        DataBaseReference.mShopsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### reuqestLogin ##### onCancelled")
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
                mView.onResultMyStore(false, R.string.toast_register_seller_failed, null)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### reuqestLogin ##### onDataChange")
                val child = dataSnapShot.children.iterator()

                while (child.hasNext()) {
                    if (child.next().key == user.userStoreName) {
                        val currentStore = dataSnapShot.child(user.userStoreName).getValue(Shops().javaClass)
                        mView.onResultMyStore(true, R.string.toast_register_success, currentStore)
                        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_SEND_CURRENT_STORE, currentStore as Any))
                        return
                    }
                }
                mView.onResultMyStore(false, R.string.toast_register_seller_failed, null)
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
            }
        })
    }

    override fun requestSetBarcodeScan(store: Shops, activity: Activity) {
        Log.d(TAG, "##### requestSetBarcodeScan ######")
        var permissionListener = object : com.gun0912.tedpermission.PermissionListener {
            override fun onPermissionGranted() {
                val integrator = IntentIntegrator(activity)
                integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                integrator.initiateScan()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                // TBD
            }
        }
        TedPermission
                .with(activity)
                .setPermissionListener(permissionListener)
                .setDeniedMessage(activity.resources.getString(R.string.toast_require_camera_permission))
                .setPermissions(Manifest.permission.CAMERA)
                .check()
    }

    override fun requestLogout(user: User) {
        Log.d(TAG, "##### requestLogout ######")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_REQUEST_LOGOUT, user as Any))
    }
}