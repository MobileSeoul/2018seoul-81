package com.jhmk.stampme.Module.Login

import android.util.Log
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.User
import org.greenrobot.eventbus.EventBus

class LoginPresenter :Login.presenter {
    private val TAG = this.javaClass.simpleName

    override fun requestLogin(user : User) {
        Log.d(TAG, "##### requestLogin ##### id : ${user.userId} pw : ${user.userPw}")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_REQUELST_LOGIN, user as Any))
    }

    override fun requestRegister() {
        Log.d(TAG, "##### requestRegister #####")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_SHOW_REGISTER))
    }
}