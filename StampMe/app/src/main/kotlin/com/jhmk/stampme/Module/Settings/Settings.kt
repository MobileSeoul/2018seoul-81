package com.jhmk.stampme.Module.Settings

import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Base.BasePresenter

interface Settings {
    interface presenter : BasePresenter.requestBackstack{
        fun requestLogout(user : User)
    }
}