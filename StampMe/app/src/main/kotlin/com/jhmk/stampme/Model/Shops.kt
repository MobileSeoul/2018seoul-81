package com.jhmk.stampme.Model

import java.io.Serializable

open class Shops : Serializable {
    var shopName = ""
    var shopAddress = ""
    var shopPhoneNumber = ""
    var shopImageUrl = ""
    var shopDistance = ""
    var shopTargetBehavior = ""
    var shopType = ""
    var isFront : Boolean = true

    constructor(shopName: String, shopAddress: String, shopPhoneNumber: String, shopImageUrl: String, shopDistance: String, shopTargetBehavior: String, shopType : String) {
        this.shopName = shopName
        this.shopAddress = shopAddress
        this.shopPhoneNumber = shopPhoneNumber
        this.shopImageUrl = shopImageUrl
        this.shopDistance = shopDistance
        this.shopTargetBehavior = shopTargetBehavior
        this.shopType = shopType
    }

    constructor()
}