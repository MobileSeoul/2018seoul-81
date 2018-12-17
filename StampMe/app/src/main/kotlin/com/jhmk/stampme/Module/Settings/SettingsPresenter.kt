package com.jhmk.stampme.Module.Settings

import android.util.Log
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.User
import org.greenrobot.eventbus.EventBus

class SettingsPresenter : Settings.presenter {
    private val TAG = this.javaClass.simpleName

    override fun requestLogout(user: User) {
        Log.d(TAG, "##### requestLogout #####")
        EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_REQUEST_LOGOUT, user as Any))
    }

    override fun requestBackstack() {
        Log.d(TAG, "##### requestBackstack #####")
        super.requestBackstack()
    }
}