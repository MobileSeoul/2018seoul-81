package com.jhmk.stampme.Main

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.DataBase.DataBaseReference
import com.jhmk.stampme.Module.DataBase.PreferencesManager
import com.jhmk.stampme.R
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter(view: Main.view) : Main.presenter {
    private val TAG = this.javaClass.simpleName
    private val mView = view
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var mBackPressedTime: Long = 0

    override fun requestLogin(user: User) {
        DataBaseReference.mUsersDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### reuqestLogin ##### onCancelled")
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
                mView.onResultLogin(false, R.string.toast_login_failed, user)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### reuqestLogin ##### onDataChange")
                val child = dataSnapShot.children.iterator()

                while (child.hasNext()) {
                    if (child.next().key == user.userId) {
                        val s = dataSnapShot.child(user.userId).child("userPw").value
                        if (dataSnapShot.child(user.userId).child("userPw").value == user.userPw) {
                            val currentUser = dataSnapShot.child(user.userId).getValue(User().javaClass)

                            if (currentUser != null) {
                                mView.onResultLogin(true, R.string.toast_login_success, currentUser)
                            } else {
                                mView.onResultLogin(false, R.string.toast_login_failed, user)
                            }
                            DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
                            return
                        }
                    }
                }
                mView.onResultLogin(false, R.string.toast_login_failed, user)
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
            }
        })
    }

    override fun requestRegister(user: User) {
        DataBaseReference.mUsersDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### requestRegister ##### onCancelled")
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
                mView.onResultRegister(false, R.string.toast_register_duplicate_failed, user)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### requestRegister ##### onDataChange")
                val child = dataSnapShot.children.iterator()

                if (user.userId.contentEquals(".")) {
                    mView.onResultRegister(false, R.string.toast_register_failed_include_comma, user)
                }

                while (child.hasNext()) {
                    if (child.next().key.equals(user.userId)) {
                        mView.onResultRegister(false, R.string.toast_register_duplicate_failed, user)
                        DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
                        return
                    }
                }
                DataBaseReference.mUsersDatabaseReference.child(user.userId).child("userId").setValue(user.userId)
                DataBaseReference.mUsersDatabaseReference.child(user.userId).child("userPw").setValue(user.userPw)
                DataBaseReference.mUsersDatabaseReference.child(user.userId).child("userName").setValue(user.userName)
                DataBaseReference.mUsersDatabaseReference.child(user.userId).child("userPhoneNumber").setValue(user.userPhoneNumber)
                DataBaseReference.mUsersDatabaseReference.child(user.userId).child("userStoreName").setValue(user.userStoreName)
                DataBaseReference.mUsersDatabaseReference.child(user.userId).child("userType").setValue(user.userType)
                mView.onResultRegister(true, R.string.toast_register_success, user)
                DataBaseReference.mUsersDatabaseReference.removeEventListener(this)
            }
        })
    }

    override fun requestSaveUserInfo(context: Context, user: User) {
        Log.d(TAG, "##### requestSaveUserInfo ##### id : ${user.userId}")
        PreferencesManager.saveUserInfo(context, user)
    }

    override fun requestDeleteUserInfo(context: Context, user: User) {
        Log.d(TAG, "##### requestDeleteUserInfo ##### id : ${user.userId}")
        PreferencesManager.deleteUserInfo(context, user)
        mView.onResultLogout(PreferencesManager.deleteUserInfo(context, user), R.string.string_logout)
    }

    override fun requestSelectTab(user: User, tabId: Int) {
        Log.d(TAG, "##### requestSelectTab #####")
        when (tabId) {
            ConstVariables.TAB_STAMPYOU -> {
                if (user.userType == ConstVariables.USER_TYPE_BUYER) {
                    mView.onResultSelectTab(false, R.string.toast_cannot_buyer, tabId)
                } else {
                    mView.onResultSelectTab(true, -1, tabId)
                }
            }
            ConstVariables.TAB_HOME -> {
                if (user.userType == ConstVariables.USER_TYPE_SELLER) {
                    mView.onResultSelectTab(false, R.string.toast_cannot_seller, tabId)
                } else {
                    mView.onResultSelectTab(true, -1, tabId)
                }
            }
            ConstVariables.TAB_STAMPME -> {
                if (user.userType == ConstVariables.USER_TYPE_SELLER) {
                    mView.onResultSelectTab(false, R.string.toast_cannot_seller, tabId)
                } else {
                    mView.onResultSelectTab(true, -1, tabId)
                }
            }
        }
    }

    override fun requestProcessingScanResult(store: Shops, result: String) {
        Log.d(TAG, "##### requestProcessingScanResult #####")
        val gson = Gson()
        val user: User
        try {
            user = gson.fromJson(result, User().javaClass)
        } catch (e: Exception) {
            e.printStackTrace()
            EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_FAILED_SUBMIT))
            return
        }

        DataBaseReference.mStampsDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.d(TAG, "##### requestProcessingScanResult ##### onCancelled")
                DataBaseReference.mStampsDatabaseReference.removeEventListener(this)
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                Log.d(TAG, "##### requestProcessingScanResult ##### onDataChange")
                val curTime = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss").format(Calendar.getInstance().getTime())
                DataBaseReference.mStampsDatabaseReference.child(user.userId).child(curTime).child("stampDate").setValue(curTime)
                DataBaseReference.mStampsDatabaseReference.child(user.userId).child(curTime).child("stampReason").setValue(store.shopTargetBehavior)
                DataBaseReference.mStampsDatabaseReference.child(user.userId).child(curTime).child("stampSource").setValue(store.shopName)
                DataBaseReference.mStampsDatabaseReference.child(user.userId).child(curTime).child("stampTargetUser").setValue(user.userId)
                DataBaseReference.mStampsDatabaseReference.removeEventListener(this)
                EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_SUCCESS_SUBMIT))
            }
        })


    }

    override fun requestBackPressed() {
        Log.d(TAG, "##### requestBackPressed #####")
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - mBackPressedTime
        var result = false
        var msg = -1;

        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            result = true

        } else {
            mBackPressedTime = tempTime
            msg = R.string.toast_backkey
        }
        mView.onResultBackPressed(result, msg)
    }
}