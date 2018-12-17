package com.jhmk.stampme.Model

import java.io.Serializable

open class Stamps : Serializable {
    open var stampDate = ""
    open var stampReason = ""
    open var stampSource = ""
    open var stampTargetUser = ""

    constructor(stampDate: String, stampReason: String, stampSource: String, stampTargetUser: String) {
        this.stampDate = stampDate
        this.stampReason = stampReason
        this.stampSource = stampSource
        this.stampTargetUser = stampTargetUser
    }

    constructor()
}