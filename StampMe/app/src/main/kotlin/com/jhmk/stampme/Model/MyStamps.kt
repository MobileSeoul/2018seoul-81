package com.jhmk.stampme.Model

class MyStamps {
    var stampsSource = ""
    var stampSourceType = ""
    var stampSourceImageUrl = ""
    var stampReason = ""
    var stampAddress = ""
    var stampDate = ""

    constructor(stampSource: String, stampSourceType: String, stampSourceImageUrl: String, stampReason: String, stampAddress: String, stampDate : String) {
        this.stampsSource = stampSource
        this.stampSourceType = stampSourceType
        this.stampSourceImageUrl = stampSourceImageUrl
        this.stampReason = stampReason
        this.stampAddress = stampAddress
        this.stampDate = stampDate
    }
}