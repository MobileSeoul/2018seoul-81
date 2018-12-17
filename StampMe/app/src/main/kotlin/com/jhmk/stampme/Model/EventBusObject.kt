package com.jhmk.stampme.Model

class EventBusObject {
    var key = -1
    var val1: Any? = null
    var val2: Any? = null

    constructor(key: Int) {
        this.key = key
    }

    constructor(key: Int, va1: Any) {
        this.key = key
        this.val1 = va1
    }

    constructor(key: Int, val1: Any, val2: Any) {
        this.key = key
        this.val1 = val1
        this.val2 = val2
    }

}