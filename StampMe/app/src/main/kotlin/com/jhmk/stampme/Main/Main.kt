package com.jhmk.stampme.Main

import android.content.Context
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User

interface Main {
    interface view{
        fun onResultLogin(result : Boolean, msg : Int, user : User)
        fun onResultRegister(result : Boolean, msg : Int, user : User)
        fun onResultLogout(result : Boolean, msg : Int)
        fun onResultSelectTab(result : Boolean, msg : Int, tabId : Int)
        fun onResultBackPressed(result : Boolean, msg : Int)
    }
    interface presenter{
        fun requestLogin(user : User)
        fun requestRegister(user : User)
        fun requestSaveUserInfo(context : Context, user : User)
        fun requestDeleteUserInfo(context : Context, user : User)
        fun requestSelectTab(user : User, tabId : Int)
        fun requestBackPressed()
        fun requestProcessingScanResult(store : Shops, result : String)
    }
}