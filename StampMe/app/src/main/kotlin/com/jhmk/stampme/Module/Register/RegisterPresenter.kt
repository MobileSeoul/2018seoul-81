package com.jhmk.stampme.Module.Register

import android.util.Log
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.DataBase.DataBaseReference
import com.jhmk.stampme.R
import org.greenrobot.eventbus.EventBus

class RegisterPresenter(view: Register.view) : Register.presenter {
    private val TAG = this.javaClass.simpleName
    private val mView = view

    override fun requestNextStep(id: String, pw: String, confirmPw: String) {
        Log.d(TAG, "##### requestNextStep #####")
        if (id.contentEquals(".")) {
            mView.onResultNextStep(false, R.string.toast_register_failed_include_comma)
            return
        }
        if (id == "" || pw == "" || confirmPw == "") {
            mView.onResultNextStep(false, R.string.toast_register_failed)
            return
        }
        if (pw == confirmPw) {
            mView.onResultNextStep(true, R.string.toast_register_success_require_detail_info)
        } else {
            mView.onResultNextStep(false, R.string.toast_register_failed_not_compare_pw)
        }
        return
    }

    override fun requestCreateAccount(fullname: String, phoneNumber: String, isSeller: Boolean, store: String) {
        Log.d(TAG, "##### requestCreateAccount #####")
        if (fullname == "" || phoneNumber == "") {
            mView.onResultCreateAccount(false, R.string.toast_register_failed)
            return
        } else if (isSeller && store == "") {
            mView.onResultCreateAccount(false, R.string.toast_register_seller_failed)
        } else if (isSeller) {
            checkStore(store)
        } else {
            mView.onResultCreateAccount(true, R.string.toast_register_success)
        }
    }

    override fun requestFinishRegister(user: User) {
        Log.d(TAG, "##### requestFinishRegister #####")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_REQUELST_REGISTER, user))
    }

    override fun requestBackButton(view: View) {
        Log.d(TAG, "##### requestBackButton ##### ${view.visibility}")
        if (view.visibility == View.VISIBLE) {
            requestBackstack()
        } else {
            view.visibility = View.VISIBLE
        }
    }

    private fun checkStore(store: String) {
        Log.d(TAG, "##### checkStore #####")
        DataBaseReference.mShopsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### reuqestLogin ##### onCancelled")
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
                mView.onResultCreateAccount(false, R.string.toast_register_seller_failed)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### reuqestLogin ##### onDataChange")
                val child = dataSnapShot.children.iterator()

                while (child.hasNext()) {
                    if (child.next().key == store) {
                        val currentStore = dataSnapShot.child(store).getValue(Shops().javaClass)
                        mView.onResultCreateAccount(true, R.string.toast_register_success)
                        return
                    }
                }
                mView.onResultCreateAccount(false, R.string.toast_register_seller_failed)
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
            }
        })
    }

    override fun requestBackstack() {
        super.requestBackstack()
    }
}