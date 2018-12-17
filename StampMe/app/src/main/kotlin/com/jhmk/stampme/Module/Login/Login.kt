package com.jhmk.stampme.Module.Login

import com.jhmk.stampme.Model.User

interface Login {
    interface presenter{
        fun requestLogin(user : User)
        fun requestRegister()
    }
}