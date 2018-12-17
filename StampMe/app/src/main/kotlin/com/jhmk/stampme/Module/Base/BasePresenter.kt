package com.jhmk.stampme.Module.Base

import android.util.Log
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import org.greenrobot.eventbus.EventBus

open interface BasePresenter {
    open interface requestBackstack{
        fun requestBackstack(){
            Log.d("BasePresenter", "##### requestBackstack #####")
            EventBus.getDefault().post(EventBusObject(ConstVariables.EVENTBUS_POP_BACKSTACK))
        }
    }
}