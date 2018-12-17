package com.jhmk.stampme.Module.Register

import android.view.View
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Base.BasePresenter

interface Register {
    interface view{
        fun onResultNextStep(flag : Boolean, msg : Int)
        fun onResultCreateAccount(flag : Boolean, msg : Int)
    }
    interface presenter : BasePresenter.requestBackstack{
        fun requestNextStep(id : String, pw : String, confirmPw : String)
        fun requestCreateAccount(fullname : String, phoneNumber : String, isSeller : Boolean, store : String)
        fun requestFinishRegister(user : User)
        fun requestBackButton(view : View)
    }
}